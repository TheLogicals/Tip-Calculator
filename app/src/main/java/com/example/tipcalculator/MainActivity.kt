package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculatorApp()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorApp(tipCalculatorViewModel: TipCalculatorViewModel = viewModel()) {
    val animatedTotal by animateFloatAsState(targetValue = tipCalculatorViewModel.total.toFloat(), label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = tipCalculatorViewModel.billAmount,
            onValueChange = { tipCalculatorViewModel.billAmount = it },
            label = { Text("Bill Amount") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tip Percentage: ${tipCalculatorViewModel.tipPercentage.toInt()}%", fontSize = 20.sp)
        Slider(
            value = tipCalculatorViewModel.tipPercentage,
            onValueChange = { tipCalculatorViewModel.tipPercentage = it },
            valueRange = 0f..30f,
            steps = 30,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Text(text = "Service Quality: ",
                modifier = Modifier.wrapContentHeight().align(Alignment.CenterVertically))
            ServiceQualitySelector(onQualitySelected = { tipCalculatorViewModel.tipPercentage = it })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Text(text = "Rounding: ",
                modifier = Modifier.wrapContentHeight().align(Alignment.CenterVertically))
            RoundingOptionSelector(
                roundingOption = tipCalculatorViewModel.roundingOption,
                onOptionSelected = { tipCalculatorViewModel.roundingOption = it }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Text(text = "Split Among: ", modifier = Modifier.wrapContentHeight().align(Alignment.CenterVertically))
            SplitBillSelector(
                split = tipCalculatorViewModel.split ,
                onOptionSelected =  {tipCalculatorViewModel.split = it}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tip Amount: $${String.format("%.2f", tipCalculatorViewModel.tip)}", fontSize = 20.sp)
        Text(text = "Total Amount: $${String.format("%.2f", animatedTotal)}", fontSize = 20.sp)
        Text(text = "Total Per Person: $${String.format("%.2f", tipCalculatorViewModel.totalPerPerson)}", fontSize = 20.sp)
    }
}

@Composable
fun ServiceQualitySelector(onQualitySelected: (Float) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val serviceOptions = listOf("Poor (10%)", "Good (15%)", "Excellent (20%)")
    val percentages = listOf(10f, 15f, 20f)
    var selectedIndex by remember { mutableIntStateOf(1) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(serviceOptions[selectedIndex])
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            serviceOptions.forEachIndexed { index, option ->
                DropdownMenuItem(text = {Text(option)}, onClick = {
                    selectedIndex = index
                    onQualitySelected(percentages[index])
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun RoundingOptionSelector(
    roundingOption: RoundingOption,
    onOptionSelected: (RoundingOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("No Rounding", "Round Up", "Round Down")
    val optionEnums = listOf(RoundingOption.NONE, RoundingOption.UP, RoundingOption.DOWN)
    var selectedIndex by remember { mutableIntStateOf(optionEnums.indexOf(roundingOption)) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(options[selectedIndex])
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(text = {Text(option)}, onClick = {
                    selectedIndex = index
                    onOptionSelected(optionEnums[index])
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun SplitBillSelector(
    split: Int,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val splitOptions = (1..10).toList()
    var selectedIndex by remember { mutableStateOf(splitOptions.indexOf(split)) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = !expanded }) {
            Text("${splitOptions[selectedIndex]}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            splitOptions.forEachIndexed { index, option ->
                DropdownMenuItem(text = {Text(option.toString())},
                    onClick = {
                    selectedIndex = index
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorApp()
}
