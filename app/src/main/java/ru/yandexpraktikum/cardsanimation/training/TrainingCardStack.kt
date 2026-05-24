package ru.yandexpraktikum.cardsanimation.training

import android.R.attr.rotation
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer



@Composable
fun TrainingCardStack(
    isExpanded: Boolean,
    modifier: Modifier = Modifier
){

    val cards = listOf("A", "K", "Q")


    Box(
        modifier =modifier,
        contentAlignment = Alignment.Center
    ){
        cards.forEachIndexed { index, title ->
            val targetRotation = if (isExpanded) {
                when (index) {
                    0 -> -45f
                    1 -> 0f
                    else -> 45f
                }
            } else {
                when (index) {
                    0 -> -10f
                    1 -> 0f
                    else -> 10f
                }
            }

            TrainingCard(
                title = title,
                targetRotation= targetRotation
            )
        }

    }
}