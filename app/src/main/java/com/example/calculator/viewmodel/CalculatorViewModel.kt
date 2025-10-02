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
//        _state.update { it.copy(operatorValue = op) }
//        _state.update { it.copy(displayValue = "0")}
//        if (operands.isEmpty()) {
//            operands.add()
//        } else if (operation != null) {
//            performCalculation()
//        }
//        operation = op
//        shouldResetDisplay = true
        if (currentValue == "0") {
            return
        }
        _state.update { it.copy(displayValue = it.displayValue + op) }
        Log.i("debug operasi masuk:", _state.value.displayValue)
//        operations.add(op)
//        operands.add(valuable.toDouble())
//        valuable = ""
    }

    private fun setFunction(function: String) {
        val currentValue = _state.value.displayValue
        val appender = when (function) {
            "sin" -> "sin("
            "cos" -> "cos("
            "tan" -> "tan("
            "x!" -> "!"
            "√" -> "√("
            "sin⁻¹" -> "sin⁻¹("
            "cos⁻¹" -> "cos⁻¹("
            "tan⁻¹" -> "tan⁻¹("
            "log" -> "log("
            "ln" -> "ln("
            "1/x" -> "1/"
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

//    private fun filterOpt(input: String){
//        for (i in input){
//            when (i) {
//                '+' -> operations.add("+")
//                '-' -> operations.add("-")
//                '×' -> operations.add("×")
//                '÷' -> operations.add("÷")
//                else -> continue
//            }
//        }
//    }

//    private fun filterNum(input: String){
//        var num = ""
//        for (i in input){
//            if (i.isDigit() || i == '.'){
//                num += i
//                Log.i("debug i on filterNum:", i.toString())
//            } else {
//                operands.add(num.toDouble())
//                Log.i("debug num on filterNum:", num)
//                num = ""
//            }
//        }
//        operands.add(num.toDouble())
//    }

    private fun filterString(input: String) {
//        "1/x" -> if (currentValue != 0.0) 1.0 / currentValue else Double.NaN
//                "x!" -> factorial(currentValue)
//                "√" -> if (currentValue >= 0) sqrt(currentValue) else Double.NaN
//                "sin" -> sin(Math.toRadians(currentValue))
//                "cos" -> cos(Math.toRadians(currentValue))
//                "tan" -> tan(Math.toRadians(currentValue))
//                "sin⁻¹" -> Math.toDegrees(asin(currentValue))
//                "cos⁻¹" -> Math.toDegrees(acos(currentValue))
//                "tan⁻¹" -> Math.toDegrees(atan(currentValue))
//                "log" -> if (currentValue > 0) log10(currentValue) else Double.NaN
//                "ln" -> if (currentValue > 0) ln(currentValue) else Double.NaN
//                else -> currentValue
        val appendFuncs = "(|x!|ln|log|sin|cos|tan|√|sin⁻¹|cos⁻¹|tan⁻¹)\\((\\d+)\\)".toRegex()
        val numbers = "\\d+".toRegex()
        val operators = "[+\\-×÷]".toRegex()
        var temp = input.replace(appendFuncs, "$1($2)")
        Log.i("debug operands:", operands.toString())
        operations = input.split(numbers).filter { it.isNotEmpty() } as MutableList<String>
        Log.i("debug operands:", operations.toString())
    }

    private fun performCalculation() {
        //        try {
//            val currentValue = _state.value.displayValue
//            filterString(currentValue)
//
//            if (operands.size > 1 && operands.size == operations.size + 1) {
//                var result = operands.first()
//
//                for (i in operations.indices) {
//                    val nextOperand = operands[i + 1]
//                    val currentOperation = operations[i]
//
//                    result = when (currentOperation) {
//                        "+" -> result + nextOperand
//                        "-" -> result - nextOperand
//                        "×" -> result * nextOperand
//                        "÷" -> if (nextOperand != 0.0) result / nextOperand else Double.NaN
//                        else -> result
//                    }
//                }
//
//                displayResult(result)
//                operands.clear()
//                operations.clear()
//                shouldResetDisplay = true
//
//            } else {
//                if (operands.isNotEmpty()) {
//                    displayResult(operands.first())
//                    operands.clear()
//                    operations.clear()
//                    shouldResetDisplay = true
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("debug", e.toString())
//        }

        var expressionString = _state.value.displayValue

        expressionString = expressionString.replace('×', '*')
        expressionString = expressionString.replace('÷', '/')
        expressionString = expressionString.replace("sin⁻¹", "asin")
        expressionString = expressionString.replace("cos⁻¹", "acos")
        expressionString = expressionString.replace("tan⁻¹", "atan")

        License.iConfirmNonCommercialUse("zaffaz1106@gmail.com")
        val expression = Expression(expressionString)

        val result = expression.calculate()

        displayResult(result)
        shouldResetDisplay = true

        operands.clear()
        operations.clear()
    }

//    private fun performCalculation() {
//        if (!operation.isEmpty() && !operands.isEmpty()) {
//            val result =
//                for (Int i = 0; i < operation.size){
//
//                when (operation) {
//                "+" -> firstOperand!! + secondOperand
//                "-" -> firstOperand!! - secondOperand
//                "×" -> firstOperand!! * secondOperand
//                "÷" -> if (secondOperand != 0.0) firstOperand!! / secondOperand else Double.NaN
//                else -> return
//            }
//            displayResult(result)
//            firstOperand = result
////            operation = null
//            shouldResetDisplay = true
//        }
//    }

//    private fun performScientific(function: String) {
//        val currentValue = _state.value.displayValue.toDoubleOrNull() ?: return
//        _state.update { it.copy(operatorValue = function) }
//        val result = try {
//            when (function) {
//                "1/x" -> if (currentValue != 0.0) 1.0 / currentValue else Double.NaN
//                "x!" -> factorial(currentValue)
//                "√" -> if (currentValue >= 0) sqrt(currentValue) else Double.NaN
//                "sin" -> sin(Math.toRadians(currentValue))
//                "cos" -> cos(Math.toRadians(currentValue))
//                "tan" -> tan(Math.toRadians(currentValue))
//                "sin⁻¹" -> Math.toDegrees(asin(currentValue))
//                "cos⁻¹" -> Math.toDegrees(acos(currentValue))
//                "tan⁻¹" -> Math.toDegrees(atan(currentValue))
//                "log" -> if (currentValue > 0) log10(currentValue) else Double.NaN
//                "ln" -> if (currentValue > 0) ln(currentValue) else Double.NaN
//                else -> currentValue
//            }
//        } catch (_: Exception) {
//            Double.NaN
//        }
//        displayResult(result, true)
//        shouldResetDisplay = true
//    }

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
//        if (!isScientific){
//            _state.update { it.copy(operatorValue = "")}
//        }
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