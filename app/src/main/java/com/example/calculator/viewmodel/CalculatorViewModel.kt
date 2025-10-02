package com.example.calculator.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.*
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()
    private var operands = mutableListOf<Double>()
    private var operations = mutableListOf<String>()
    private var shouldResetDisplay: Boolean = false

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> setOperation(action.symbol)
            is CalculatorAction.Scientific -> setFunction(action.function)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Clear -> clear()
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Decimal -> enterDecimal()
        }
    }

    private fun enterNumber(number: Int) {
        val currentDisplay = _state.value.displayValue
        if (currentDisplay == "0" && number != 0) {
            _state.update { it.copy(displayValue = number.toString()) }
//            valuable = number.toString()
        } else if (currentDisplay != "0") {
            _state.update { it.copy(displayValue = currentDisplay + number) }
//            valuable += number.toString()
        }
    }

    private fun setOperation(op: String) {
        val currentValue = _state.value.displayValue
        if (currentValue == "0") {
            return
        }
        _state.update { it.copy(displayValue = it.displayValue + op) }
        Log.i("debug operasi masuk:", _state.value.displayValue)
    }

    private fun setFunction(function: String) {
        val currentValue = _state.value.displayValue
        val appender = when (function) {
            "sin" -> "sin("
            "cos" -> "cos("
            "tan" -> "tan("
            "x!" -> "x!("
            "√" -> "√("
            "sin⁻¹" -> "sin⁻¹("
            "cos⁻¹" -> "cos⁻¹("
            "tan⁻¹" -> "tan⁻¹("
            "log" -> "log("
            "ln" -> "ln("
            "1/x" -> "1/x("
            ")" -> ")"
            else -> ""
        }
        if (currentValue == "0") {
            _state.update { it.copy(displayValue = appender) }
            Log.i("debug fungsi masuk:", _state.value.displayValue)
        } else {
            _state.update { it.copy(displayValue = currentValue + appender) }
            Log.i("debug fungsi masuk:", _state.value.displayValue)
        }
    }

    private fun filterString(input: String) {
        val appendFuncs =
            "(1/x|x!|ln|log|sin|cos|tan|√|sin⁻¹|cos⁻¹|tan⁻¹)+\\((\\d+)\\)+|(d+)".toRegex()
        val prepFuncs = ""
        val numbers = "\\d+".toRegex()
        val operators = "[+\\-×÷]".toRegex()

        val temp = input.split(operators).toString()
        val tempdata =
            appendFuncs.findAll(temp).map { it -> Pair(it.groupValues[1], it.groupValues[2]) }
                .toList()

        for ((tipe, nilai) in tempdata) {
            if (tipe == "d") {
                operands.add(nilai.toDouble())
            } else {
                val dnilai = nilai.toDouble()
                val appender = when (tipe) {
                    "1/x" -> if (dnilai != 0.0) 1.0 / dnilai else Double.NaN
                    "x!" -> factorial(dnilai)
                    "√" -> if (dnilai >= 0) sqrt(dnilai) else Double.NaN
                    "sin" -> sin(Math.toRadians(dnilai))
                    "cos" -> cos(Math.toRadians(dnilai))
                    "tan" -> tan(Math.toRadians(dnilai))
                    "sin⁻¹" -> Math.toDegrees(asin(dnilai))
                    "cos⁻¹" -> Math.toDegrees(acos(dnilai))
                    "tan⁻¹" -> Math.toDegrees(atan(dnilai))
                    "log" -> if (dnilai > 0) log10(dnilai) else Double.NaN
                    "ln" -> if (dnilai > 0) ln(dnilai) else Double.NaN
                    else -> nilai
                }
                operands.add(appender.toString().toDouble())
            }
        }

        Log.i("debug operands:", operands.toString())
        operations = input.split(numbers).filter { it.isNotEmpty() } as MutableList<String>
        Log.i("debug operations:", operations.toString())
    }

    private fun performCalculation() {
        try {
            val currentValue = _state.value.displayValue
            filterString(currentValue)

            if (operands.size > 1 && operands.size == operations.size + 1) {
                var result = operands.first()

                for (i in operations.indices) {
                    val nextOperand = operands[i + 1]
                    val currentOperation = operations[i]

                    result = when (currentOperation) {
                        "+" -> result + nextOperand
                        "-" -> result - nextOperand
                        "×" -> result * nextOperand
                        "÷" -> if (nextOperand != 0.0) result / nextOperand else Double.NaN
                        else -> result
                    }
                }

                displayResult(result)
                operands.clear()
                operations.clear()
                shouldResetDisplay = true

            } else {
                if (operands.isNotEmpty()) {
                    displayResult(operands.first())
                    operands.clear()
                    operations.clear()
                    shouldResetDisplay = true
                }
            }
        } catch (e: Exception) {
            Log.e("debug", e.toString())
        }
    }

    private fun enterDecimal() {
        if (shouldResetDisplay) {
            _state.update { it.copy(displayValue = "0.") }
            shouldResetDisplay = false
            return
        }
        if (!_state.value.displayValue.contains(".")) {
            _state.update { it.copy(displayValue = _state.value.displayValue + ".") }
        }
    }

    private fun delete() {
        val currentDisplay = _state.value.displayValue
        if (currentDisplay.length > 1) {
            _state.update { it.copy(displayValue = currentDisplay.dropLast(1)) }
        } else {
            _state.update { it.copy(displayValue = "0") }
        }
    }

    private fun clear() {
        _state.value = CalculatorState()
        operands.clear()
        operations.clear()
        shouldResetDisplay = false
    }

    private fun displayResult(result: Double) {
        val resultString = if (result.isNaN() || result.isInfinite()) {
            "Error"
        } else if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        _state.update { it.copy(displayValue = resultString) }
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n)) return Double.NaN
        if (n == 0.0) return 1.0
        var result = 1.0
        for (i in 1..n.toInt()) {
            result *= i
        }
        return result
    }
}