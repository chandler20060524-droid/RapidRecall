package com.deming1.rapidrecall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deming1.rapidrecall.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.deming1.rapidrecall.ui.theme.RapidRecallTheme
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History

/*
* The StartScreen function manages all the UI components on the Start Screen. Such as a difficulty
* slider, start button and view history (summary) button.
* */
@Composable
fun StartScreen(
    onStartButtonClicked: (Int) -> Unit,
    onSummaryIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var seqLen by rememberSaveable() { mutableStateOf(1) }
    val seqLenHint = stringResource(R.string.seq_length) + seqLen.toString()

    val startText = when (seqLen) {
        1 -> stringResource(R.string.very_easy)
        2 -> stringResource(R.string.easy)
        3 -> stringResource(R.string.easy)
        4 -> stringResource(R.string.medium)
        5 -> stringResource(R.string.medium)
        6 -> stringResource(R.string.medium)
        7 -> stringResource(R.string.hard)
        8 -> stringResource(R.string.hard)
        9 -> stringResource(R.string.hard)
        10 -> stringResource(R.string.hardcore)
        else -> { throw Exception("Unknown difficulty level", null) }
    }
    val buttonColor = when (seqLen) {
        1 -> Color.Green
        2 -> Color.Cyan
        3 -> Color.Cyan
        4 -> Color.Yellow
        5 -> Color.Yellow
        6 -> Color.Yellow
        7 -> Color.Magenta
        8 -> Color.Magenta
        9 -> Color.Magenta
        10 -> Color.Red
        else -> { throw Exception("Unknown difficulty level", null) }
    }
    val textColor = if (seqLen < 7) {
        Color.Black
    } else {
        Color.White
    }

    Box {
        IconButton(
            onClick = onSummaryIconClicked,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "History"
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.greet),
            modifier = modifier,
            fontSize = 25.sp
        )
        Spacer(modifier = modifier.height(50.dp))
        Text(
            text = seqLenHint,
            modifier = modifier
        )
        Slider(
            value = seqLen.toFloat(),
            onValueChange = { seqLen = it.toInt() },
            valueRange = 1.0f..10.0f,
            steps = 8,
            modifier = modifier.width(300.dp)
        )
        Spacer(modifier = modifier.height(35.dp))
        Button(
            modifier = modifier.size(width = 200.dp, height = 60.dp),
            onClick = { onStartButtonClicked(seqLen) },
            colors = ButtonColors(
                containerColor = buttonColor,
                contentColor = textColor,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray)
        ) {
            Text(
                text = startText,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    RapidRecallTheme() {
        StartScreen(
            onStartButtonClicked = {},
            onSummaryIconClicked = {}
        )
    }
}