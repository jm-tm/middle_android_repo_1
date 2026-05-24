package ru.yandexpraktikum.cardsanimation.training

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class TrainingCardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrainingCardsScreen()
        }

    }
}

@Composable
fun TrainingCardsScreen() {
    var isExpanded by remember {mutableStateOf(false)}
    Button(onClick = {
        isExpanded =!isExpanded
    }) {
        Text(
            text = if (isExpanded) {
                "Свернуть"
            } else {
                "Раскрыть"
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text(
            text = "Training Cards Lab"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        TrainingCardStack(isExpanded = isExpanded)
    }
}