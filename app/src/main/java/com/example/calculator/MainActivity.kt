package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.calculator.composables.CalculatorDisplay
import com.example.calculator.composables.CalculatorGrid
import com.example.calculator.theme.CalculatorTheme
import com.example.calculator.theme.DeepSpace
import com.example.calculator.viewmodel.CalculatorViewModel

// ... (imports)

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<CalculatorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val state by viewModel.state.collectAsState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DeepSpace)
                ) {
                    CalculatorDisplay(
                        displayText = state.displayValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    CalculatorGrid(
                        onAction = viewModel::onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                    )
                }
            }
        }
    }
}
