package ru.yandexpraktikum.cardsanimation.compose

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import ru.yandexpraktikum.cardsanimation.model.CardData
import kotlin.math.abs

/**
 * Метод для вычисления поворота карты в конкретной позиции
 */
fun calculateCardRotation(
    cardIndex: Int,
    cardCount: Int,
    isRotated: Boolean
): Float {
    if (cardCount <= 1) return 0f

    return if (isRotated) {
        val angleStep = 180f / (cardCount - 1)
        90f - (cardIndex * angleStep)
    } else {
        val angleStep = 45f / (cardCount - 1)
        22.5f - (cardIndex * angleStep)
    }
}

private const val SwipeThreshold = 100f

@Composable
fun AnimatedCardStack(cards: List<CardData>) {
    val cardCount = cards.size
    var isRotated by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                var horizontalDragOffset = 0f
                var verticalDragOffset = 0f
                detectDragGestures(
                    onDragStart = {
                        horizontalDragOffset = 0f
                        verticalDragOffset = 0f
                        Log.d("CardsGesture", "drag start")
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        horizontalDragOffset += dragAmount.x
                        verticalDragOffset += dragAmount.y
                        Log.d(
                            "CardsGesture",
                            "drag x=$horizontalDragOffset y=$verticalDragOffset"
                        )
                    },
                    onDragEnd = {
                        val horizontalAbs = abs(horizontalDragOffset)
                        val verticalAbs = abs(verticalDragOffset)
                        verticalAbs > horizontalAbs

                        when {
                            verticalAbs > horizontalAbs -> {
                                handleVerticalSwipe(
                                    verticalDragOffset,
                                    SwipeThreshold,
                                    onFanStateChange = { newState ->
                                        isRotated = newState
                                    }
                                )
                            }

                            verticalAbs < horizontalAbs -> {

                            }
                        }

                        horizontalDragOffset = 0f
                        verticalDragOffset = 0f

                    })
            },
        contentAlignment = Alignment.Center
    ) {
        cards.forEachIndexed { i, cardData ->
            key(cardData.imageResId) {
                val targetRotation = calculateCardRotation(i, cardCount, isRotated)

                AnimatedCard(
                    cardIndex = i,
                    targetRotation = targetRotation,
                    cardData = cardData
                    // TODO: [Задание 5] Здесь добавьте параметры анимации карты


                )
            }
        }
    }
}

// Простая функция перестановки карт
fun reorderCards(cards: List<CardData>): List<CardData> {
    return cards.drop(1) + cards.first()
}

private fun handleVerticalSwipe(
    verticalDragOffset: Float,
    threshold: Float,
    onFanStateChange: (Boolean) -> Unit
) {
    when {
        verticalDragOffset < -threshold -> {
            onFanStateChange(true)
            Log.d("CardsGesture", "vertical swipe UP: $verticalDragOffset")
        }

        verticalDragOffset > threshold -> {
            onFanStateChange(false)
            Log.d("CardsGesture", "vertical swipe DOWN: $verticalDragOffset")
        }

        else -> {
            Log.d("CardsGesture", "vertical swipe too small: $verticalDragOffset")
        }
    }
}
