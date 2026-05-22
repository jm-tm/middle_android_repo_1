package ru.yandexpraktikum.cardsanimation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.tooling.preview.Preview
import ru.yandexpraktikum.cardsanimation.AnimatedCardScreen
import ru.yandexpraktikum.cardsanimation.R
import ru.yandexpraktikum.cardsanimation.model.CardData
import ru.yandexpraktikum.cardsanimation.ui.theme.CardsAnimationTheme

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

    //временно для проверки кликом
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // TODO: [Задание 2] Добавьте обработку жестов
    // Подсказка: Используйте Modifier.pointerInput() с методом detectDragGestures()

//    var dragOffsetX = 0f
    //временно для проверки кликом, далее заготовка для задания 2
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                isRotated = !isRotated
            },
//            .pointerInput(Unit){
//                detectDragGestures(
//                    onDragStart = {
//                        dragOffsetX = 0f
//                    },
//                    onDrag = { change, dragAmount ->
//                        change.consume()
//                        dragOffsetX += dragAmount.x
//                    },
//                    onDragEnd = {
//                        if (dragOffsetX > 50f) {
//                            isRotated = true
//                        } else if (dragOffsetX < -50f) {
//                            isRotated = false
//                        }
//
//                        dragOffsetX = 0f
//                    })
//            },
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

// превью для отладки - потом уберу
//@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
//@Composable
//fun PreviewAnimatedCardScreen() {
//    CardsAnimationTheme {
//        AnimatedCardScreen(
//            cards = listOf(
//                CardData(R.drawable.card_clover),
//                CardData(R.drawable.card_hearts),
//                CardData(R.drawable.card_spades),
//                CardData(R.drawable.card_diamond)
//            )
//        )
//    }
//}

