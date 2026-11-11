package com.aibuilt.nowwiththeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

        // Row 1: C, ÷, ×, -
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("C", onClick = { onClearClick() }, color = MaterialTheme.colorScheme.error, flexWeight = 1f)
            CalculatorButton("÷", onClick = { onOperationClick('÷') }, color = MaterialTheme.colorScheme.primary, flexWeight = 1f)
            CalculatorButton("×", onClick = { onOperationClick('×') }, color = MaterialTheme.colorScheme.primary, flexWeight = 1f)
            CalculatorButton("-", onClick = { onOperationClick('-') }, color = MaterialTheme.colorScheme.primary, flexWeight = 1f)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: 7, 8, 9, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("7", onClick = { onNumberClick("7") }, flexWeight = 1f)
            CalculatorButton("8", onClick = { onNumberClick("8") }, flexWeight = 1f)
            CalculatorButton("9", onClick = { onNumberClick("9") }, flexWeight = 1f)
            CalculatorButton("+", onClick = { onOperationClick('+') }, color = MaterialTheme.colorScheme.primary, flexWeight = 1f)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 3: 4, 5, 6, =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("4", onClick = { onNumberClick("4") }, flexWeight = 1f)
            CalculatorButton("5", onClick = { onNumberClick("5") }, flexWeight = 1f)
            CalculatorButton("6", onClick = { onNumberClick("6") }, flexWeight = 1f)
            CalculatorButton("=", onClick = { onEqualsClick() }, color = MaterialTheme.colorScheme.tertiary, flexWeight = 1f)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 4: 1, 2, 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("1", onClick = { onNumberClick("1") }, flexWeight = 1f)
            CalculatorButton("2", onClick = { onNumberClick("2") }, flexWeight = 1f)
            CalculatorButton("3", onClick = { onNumberClick("3") }, flexWeight = 1f)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 5: 0, .
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("0", onClick = { onNumberClick("0") }, flexWeight = 2f)
            CalculatorButton(".", onClick = { onDecimalClick() }, flexWeight = 1f)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    flexWeight: Float = 1f
) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(flexWeight),
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
