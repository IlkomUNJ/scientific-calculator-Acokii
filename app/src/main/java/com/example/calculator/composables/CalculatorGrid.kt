// file: composables/CalculatorGrid.kt
package com.example.calculator.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.theme.CometGray
import com.example.calculator.theme.NebulaPurple
import com.example.calculator.theme.PulsarBlue
import com.example.calculator.viewmodel.CalculatorAction

@Composable
fun CalculatorGrid(
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonLayout = listOf(
        listOf("sin", "cos", "tan", "x!", "√"),
        listOf("sin⁻¹", "cos⁻¹", "tan⁻¹", "log", "ln"),
        listOf("C", "⌫", "1/x", ")", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttonLayout.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { symbol ->
                    val weight = if (symbol == "0") 8f else 4f
//                    val aspect = if (symbol == "0") 2f else 1f
                    CalculatorButton(
                        symbol = symbol,
                        modifier = when (symbol) {
                            in listOf(
                                "sin",
                                "cos",
                                "tan",
                                "x!",
                                "√",
                                "sin⁻¹",
                                "cos⁻¹",
                                "tan⁻¹",
                                "log",
                                "ln",
                            ) -> Modifier.weight(2f)

                            else -> Modifier.weight(weight)
                        }.height(64.dp),
                        color = when (symbol) {
                            in "0".."9", "." -> CometGray
                            "C", "⌫" -> PulsarBlue
                            "=" -> NebulaPurple
                            in listOf("+", "-", "×", "÷") -> NebulaPurple
                            else -> PulsarBlue // Scientific functions
                        },
                        onClick = {
                            val action = when (symbol) {
                                in "0".."9" -> CalculatorAction.Number(symbol.toInt())
                                in listOf("+", "-", "×", "÷") -> CalculatorAction.Operation(symbol)
                                in listOf(
                                    "sin",
                                    "cos",
                                    "tan",
                                    "x!",
                                    "√",
                                    "sin⁻¹",
                                    "cos⁻¹",
                                    "tan⁻¹",
                                    "log",
                                    "ln",
                                    "1/x",
                                    ")",
                                ) -> CalculatorAction.Scientific(symbol)

                                "." -> CalculatorAction.Decimal
                                "C" -> CalculatorAction.Clear
                                "⌫" -> CalculatorAction.Delete
                                "=" -> CalculatorAction.Calculate
                                else -> null
                            }
                            action?.let { onAction(it) }
                        }
                    )
                }
            }
            if (rowItems.size < 4 && rowItems.contains("=")) {
                Box(modifier = Modifier.weight(1f)) {}
            }
            if (rowItems.size < 5 && !rowItems.contains("=")) {
                Box(modifier = Modifier.weight(1f)) {}
            }
        }
    }
}

@Preview
@Composable
fun PreviewGrid(){
    CalculatorGrid(
        onAction = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(1000.dp)
    )
}