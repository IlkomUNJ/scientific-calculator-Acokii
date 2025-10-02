package com.example.calculator.viewmodel

sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    data class Operation(val symbol: String) : CalculatorAction()
    data class Scientific(val function: String) : CalculatorAction()
    object Calculate : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Decimal : CalculatorAction()
}