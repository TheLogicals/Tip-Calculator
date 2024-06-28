package com.example.tipcalculator
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.ceil
import kotlin.math.floor

class TipCalculatorViewModel : ViewModel() {
    var billAmount by mutableStateOf("")
    var tipPercentage by mutableFloatStateOf(15f)
    var roundingOption by mutableStateOf(RoundingOption.NONE)
    var split by mutableStateOf<Int>(1)
    private val bill: Double get() = billAmount.toDoubleOrNull() ?: 0.0
    val tip: Double get() = bill * (tipPercentage / 100)

    val total: Double get() = when (roundingOption) {
            RoundingOption.UP -> ceil(bill + tip)
            RoundingOption.DOWN -> floor(bill + tip)
            else -> bill + tip
        }

    val totalPerPerson: Double get() =  when (roundingOption) {
        RoundingOption.UP -> ceil(total/split)
        RoundingOption.DOWN -> floor(total/split)
        else -> total/split
    }
}

enum class RoundingOption {
    NONE, UP, DOWN
}