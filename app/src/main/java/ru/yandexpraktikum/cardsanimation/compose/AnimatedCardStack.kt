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

@Composable
fun AnimatedCardStack(cards: List<CardData>) {
    val cardCount = cards.size
    var isRotated by remember { mutableStateOf(false) }

    // TODO: [Задание 2] Добавьте обработку жестов
    // Подсказка: Используйте Modifier.pointerInput() с методом detectDragGestures()
    var horizontalDragOffset = 0f
    var verticalDragOffset = 0f
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
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
                        when {
                            verticalAbs > horizontalAbs -> {
                                if (verticalDragOffset < 0f) {
                                    Log.d("CardsGesture", "vertical swipe UP: $verticalDragOffset")
                                } else {
                                    Log.d(
                                        "CardsGesture",
                                        "vertical swipe DOWN: $verticalDragOffset"
                                    )
                                }
                            }

                            horizontalAbs > verticalAbs -> {
                                if (horizontalDragOffset < 0f) {
                                    Log.d(
                                        "CardsGesture",
                                        "horizontal swipe LEFT: $horizontalDragOffset"
                                    )
                                } else {
                                    Log.d(
                                        "CardsGesture",
                                        "horizontal swipe RIGHT: $horizontalDragOffset"
                                    )
                                }
                            }

                            else -> {
                                Log.d("CardsGesture", "unknown swipe")
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


