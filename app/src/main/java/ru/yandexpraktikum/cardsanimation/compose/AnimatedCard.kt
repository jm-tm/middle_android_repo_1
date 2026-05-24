package ru.yandexpraktikum.cardsanimation.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.yandexpraktikum.cardsanimation.model.CardData
import kotlin.math.cos
import kotlin.math.sin

enum class StepAnimation(
    val step: Int,
    val label: String
){
    MOVE_RIGHT(step = 1, label = "moveRight"),
    MOVE_TO_CENTER(step = 2, label = "moveToCenter"),
    FINAL_ROTATION(3,"finalRotation")
}
private const val MOVE_ANIMATION_DURATION = 300
private const val ROTATION_ANIMATION_DURATION = 800
private const val FINAL_ROTATION_DURATION = 300

@Composable
fun AnimatedCard(
    cardIndex: Int,
    cardData: CardData,
    targetRotation: Float,
    isAnimating: Boolean = false,
    animationStep: Int = 0,
    onAnimationStepComplete: ((Int) -> Unit)? = null,
    finalRotation: Float = targetRotation,
) {
    val animatedRotation by animateFloatAsState(
        targetValue = when {
            animationStep == StepAnimation.FINAL_ROTATION.step -> finalRotation
            isAnimating -> targetRotation
            else -> targetRotation
        },
        animationSpec = tween(durationMillis =
            if (animationStep == StepAnimation.FINAL_ROTATION.step)
                FINAL_ROTATION_DURATION else ROTATION_ANIMATION_DURATION),
        finishedListener = {
            if (animationStep == StepAnimation.FINAL_ROTATION.step && isAnimating)
                onAnimationStepComplete?.invoke(StepAnimation.FINAL_ROTATION.step)
        },
        label = "rotation"
    )
    val density = LocalDensity.current
    val shouldBringToFront = isAnimating && animationStep >= StepAnimation.MOVE_TO_CENTER.step

    val animatedTranslationX by animateFloatAsState(
        targetValue = when {
            isAnimating && animationStep == StepAnimation.MOVE_RIGHT.step -> {
                val moveDistance = with(density) {
                    50.dp.toPx()
                }
                val rotationRad = Math.toRadians(targetRotation.toDouble())

                moveDistance * cos(rotationRad).toFloat()
            }

            isAnimating && animationStep == StepAnimation.MOVE_TO_CENTER.step -> {
                0f
            }

            else -> {
                0f
            }
        },
        animationSpec = tween(durationMillis = MOVE_ANIMATION_DURATION),
        finishedListener = {
            if (isAnimating) {
                when (animationStep) {
                    StepAnimation.MOVE_RIGHT.step -> onAnimationStepComplete?.invoke(1)
                    StepAnimation.MOVE_TO_CENTER.step -> onAnimationStepComplete?.invoke(2)
                }
            }
        },
        label = "translationX"
    )

    val animatedTranslationY by animateFloatAsState(
        targetValue = when {

            (isAnimating && animationStep == StepAnimation.MOVE_RIGHT.step) -> {
                val moveDistance = with(density) { 50.dp.toPx() }
                val rotationRad = Math.toRadians(targetRotation.toDouble())
                moveDistance * sin(rotationRad).toFloat()
            }

            isAnimating && animationStep == StepAnimation.MOVE_TO_CENTER.step -> {
                0f
            }

            else -> {
                0f
            }
        },
        animationSpec = tween(durationMillis = MOVE_ANIMATION_DURATION),
        label = "translationY"
    )

    Card(
        modifier = Modifier
            .size(width = 100.dp, height = 160.dp)
            .graphicsLayer {
                rotationZ = animatedRotation
                transformOrigin = TransformOrigin(0.5f, 1.0f)
                translationX = if (isAnimating) animatedTranslationX else 0f
                translationY = if (isAnimating) animatedTranslationY else 0f
            }
            .let { baseModifier ->
                if (shouldBringToFront) {
                    baseModifier.zIndex(1000f)
                } else {
                    baseModifier
                }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = (4 + cardIndex).dp
        )
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(cardData.imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }


}