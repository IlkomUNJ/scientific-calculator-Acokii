package com.example.calculator.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorDisplay(
    displayText: String,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(277.dp),

        contentAlignment = Alignment.BottomEnd
//
    ) {
        Text(
            text = displayText,
            textAlign = TextAlign.End,
            fontSize = 80.sp,
            color = Color.White,
            maxLines = 1
        )
    }
}

@Composable
@Preview
fun CalculatorDisplayPreview() {
    CalculatorDisplay(displayText = "123")
}