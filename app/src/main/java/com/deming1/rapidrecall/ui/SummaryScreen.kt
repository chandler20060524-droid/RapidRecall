package com.deming1.rapidrecall.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.deming1.rapidrecall.RapidRecallScreens
import com.deming1.rapidrecall.ui.theme.RapidRecallTheme
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.material3.MaterialTheme

@Composable
fun SummaryScreen(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        ) {
            PercentageRing(
                percentage = percentage,
                modifier = modifier.size(180.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {
//            items() {}
        }
    }
}

@Composable
fun PercentageRing(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = { 1.0f },
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray.copy(alpha = 0.4f),
            strokeWidth = 25.dp,
            trackColor = Color.Transparent,
        )
        CircularProgressIndicator(
            progress = { percentage },
            modifier = Modifier.fillMaxSize(),
            color = Color.Green,
            strokeWidth = 25.dp,
            strokeCap = StrokeCap.Square,
        )
        Text(
            text = "${(percentage * 100).toInt()}%",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 35.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    RapidRecallTheme() {
        SummaryScreen(
            percentage = 0.3f
        )
    }
}