package com.deming1.rapidrecall.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.deming1.rapidrecall.R
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.History
import com.deming1.rapidrecall.AttemptData
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    percentage: Float = 0.0f,
    onBackButtonClicked: () -> Unit = {},
    previousAttempts: List<AttemptData> = listOf()
) {
    Box {
        IconButton(
            onClick = onBackButtonClicked,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "History"
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 50.dp,
                start = 30.dp,
                end = 30.dp,
                bottom = 30.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            Text(
                text = stringResource(R.string.overall_acc),
                fontSize = 20.sp
            )
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
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            Text(
                text = stringResource(R.string.previous_attempts),
                fontSize = 20.sp
            )
            Spacer(modifier = modifier.height(10.dp))
            LazyColumn(
                modifier = modifier
            ) {
                itemsIndexed(
                    items = previousAttempts
                ) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp), // Space between frames
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Attempt ${index + 1}: ")
                                append(item.sequence)
                                append(" Score: ${item.correctDigits}/${item.totalDigits}")
                            }
                        )
                    }
                }
            }
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
            previousAttempts = listOf(
                AttemptData(
                    sequence = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Green)) {
                            append("12345")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("67890")
                        }
                    },
                    correctDigits = 5,
                    totalDigits = 10
                ),
                AttemptData(
                    sequence = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Green)) {
                            append("12345")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("67890")
                        }
                    },
                    correctDigits = 5,
                    totalDigits = 10
                ),
            )
        )
    }
}