package com.aibuilt.nowwiththeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF6750A4),
                    secondary = Color(0xFF625B71),
                    tertiary = Color(0xFF7D5260),
                    background = Color(0xFFFFFBFE),
                    surface = Color(0xFFFFFBFE),
                    onPrimary = Color.White,
                    onSecondary = Color.White,
                    onTertiary = Color.White,
                    onBackground = Color(0xFF1C1B1F),
                    onSurface = Color(0xFF1C1B1F),
                )
            ) {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<Char?>(null) }
    var waitingForSecondOperand by remember { mutableStateOf(false) }

    fun calculate(first: Double, second: Double, op: Char): Double {
        return when (op) {
            '+' -> first + second
            '-' -> first - second
            '×' -> first * second
            '÷' -> if (second != 0.0) first / second else 0.0
            else -> second
        }
    }

    fun onNumberClick(number: String) {
        if (waitingForSecondOperand) {
            display = number
            waitingForSecondOperand = false
        } else {
            display = if (display == "0") number else display + number
        }
    }

    fun onDecimalClick() {
        if (waitingForSecondOperand) {
            display = "0."
            waitingForSecondOperand = false
            return
        }
        if (!display.contains(".")) {
            display += "."
        }
    }

    fun onOperationClick(op: Char) {
        val inputValue = display.toDoubleOrNull() ?: return
        
        if (firstOperand == null) {
            firstOperand = inputValue
        } else if (operation != null) {
            val result = calculate(firstOperand!!, inputValue, operation!!)
            display = result.toString()
            firstOperand = result
        }
        
        operation = op
        waitingForSecondOperand = true
    }

    fun onEqualsClick() {
        val inputValue = display.toDoubleOrNull() ?: return
        if (operation != null && firstOperand != null) {
            val result = calculate(firstOperand!!, inputValue, operation!!)
            display = result.toString()
            firstOperand = null
            operation = null
            waitingForSecondOperand = true
        }
    }

    fun onClearClick() {
        display = "0"
        firstOperand = null
        operation = null
        waitingForSecondOperand = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = display,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("C", onClick = { onClearClick() }, color = MaterialTheme.colorScheme.error)
            CalculatorButton("÷", onClick = { onOperationClick('÷') }, color = MaterialTheme.colorScheme.primary)
            CalculatorButton("×", onClick = { onOperationClick('×') }, color = MaterialTheme.colorScheme.primary)
            CalculatorButton("-", onClick = { onOperationClick('-') }, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("7", onClick = { onNumberClick("7") })
            CalculatorButton("8", onClick = { onNumberClick("8") })
            CalculatorButton("9", onClick = { onNumberClick("9") })
            CalculatorButton("+", onClick = { onOperationClick('+') }, color = MaterialTheme.colorScheme.primary, rowspan = 2)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("4", onClick = { onNumberClick("4") })
            CalculatorButton("5", onClick = { onNumberClick("5") })
            CalculatorButton("6", onClick = { onNumberClick("6") })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("1", onClick = { onNumberClick("1") })
            CalculatorButton("2", onClick = { onNumberClick("2") })
            CalculatorButton("3", onClick = { onNumberClick("3") })
            CalculatorButton("=", onClick = { onEqualsClick() }, color = MaterialTheme.colorScheme.tertiary, rowspan = 2)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("0", onClick = { onNumberClick("0") }, colspan = 2)
            CalculatorButton(".", onClick = { onDecimalClick() })
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    colspan: Int = 1,
    rowspan: Int = 1
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f * colspan)
            .height(70.dp * rowspan),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = if (color == MaterialTheme.colorScheme.error) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}
