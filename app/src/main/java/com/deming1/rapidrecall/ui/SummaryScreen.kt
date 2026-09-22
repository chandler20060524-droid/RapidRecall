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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.deming1.rapidrecall.ui.theme.RapidRecallTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.deming1.rapidrecall.R
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import com.deming1.rapidrecall.AttemptData
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row

/*
* The SummaryScreen function manages all the UI components on the Summary Screen, which shows
* information like overall accuracy and previous attempts.
* */
@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    percentage: Float = 0.0f,
    correctAttempt: Int,
    totalAttempt: Int,
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
            .fillMaxHeight()
            .padding(
                top = 50.dp,
                start = 30.dp,
                end = 30.dp,
                bottom = 40.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                PercentageRing(
                    percentage = percentage,
                    modifier = modifier.size(160.dp).fillMaxWidth(0.5f)
                )
                Spacer(modifier = modifier.width(5.dp))
                Text(
                    text = "${stringResource(R.string.correct_attempt)}\n${correctAttempt}\n\n${stringResource(R.string.total_attempt)}\n${totalAttempt}",
                    fontSize = 20.sp
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = stringResource(R.string.previous_attempts),
                fontSize = 20.sp
            )
            Spacer(modifier = modifier.height(10.dp))
            LazyColumn(
                modifier = modifier.fillMaxSize()
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
                                append("Attempt ${index + 1} - ${item.time}:\n")
                                append("Target Sequence: ")
                                append(item.sequence)
                                append("\nYour Answer: ${item.userInput}")
                                append("\nScore: ${item.correctDigits}/${item.totalDigits}")
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
        val pa = mutableListOf<AttemptData>()
        for (i in 0..20) {
            pa.add(AttemptData(
                time = "Testing Time",
                userInput = "1234512345",
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
            ))
        }
        SummaryScreen(
            percentage = 0.5f,
            correctAttempt = 0,
            totalAttempt = 10,
            previousAttempts = pa
        )
    }
}