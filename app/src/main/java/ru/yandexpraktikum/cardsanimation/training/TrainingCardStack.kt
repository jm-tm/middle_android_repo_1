package ru.yandexpraktikum.cardsanimation.training

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

private const val EXPANDED_ANGLE_RANGE = 90f
private const val COLLAPSED_ANGLE_RANGE = 20f

@Composable
fun TrainingCardStack(
    cards: List<String>,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        cards.forEachIndexed { index, title ->
            val targetRotation = calculateTargetRotation(
                index = index,
                isExpanded = isExpanded,
                cardCount = cards.size
            )

            TrainingCard(
                title = title,
                targetRotation = targetRotation
            )
        }
    }
}

private fun calculateTargetRotation(index: Int, isExpanded: Boolean, cardCount: Int): Float {
    if (cardCount <= 1) {
        return 0f
    }
    val angleRange = if (isExpanded) EXPANDED_ANGLE_RANGE else COLLAPSED_ANGLE_RANGE
    val angleStep = angleRange / (cardCount - 1)
    val startAngle = -angleRange / 2f

    return startAngle + index * angleStep
}