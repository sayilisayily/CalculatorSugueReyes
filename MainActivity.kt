package com.example.SugueCalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // TextView to display the current input or result
    lateinit var display: TextView

    // Variables to keep track of the current input, selected operator, and first operand
    var currentNumber: String = ""
    var operator: String? = null
    var firstOperand: Double? = null
    var isNewCalculation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the display TextView
        display = findViewById(R.id.textView)

        // List of button IDs for number and dot buttons
        val buttons = listOf(
            R.id.zero, R.id.one, R.id.two, R.id.three,
            R.id.four, R.id.five, R.id.six, R.id.seven,
            R.id.eight, R.id.nine, R.id.dot
        )


        // Set up click listeners for number and dot buttons
        for (buttonId in buttons) {
            findViewById<Button>(buttonId).setOnClickListener {
                onNumberPressed((it as Button).text.toString())
            }
        }

        // Set up click listeners for operator buttons
        findViewById<Button>(R.id.plus).setOnClickListener { onOperatorPressed("+") }
        findViewById<Button>(R.id.minus).setOnClickListener { onOperatorPressed("-") }
        findViewById<Button>(R.id.multiply).setOnClickListener { onOperatorPressed("*") }
        findViewById<Button>(R.id.divide).setOnClickListener { onOperatorPressed("/") }
        findViewById<Button>(R.id.percent).setOnClickListener { onPercentPressed() }
        findViewById<Button>(R.id.negative).setOnClickListener { onNegativePressed() }

        // Set up click listeners for equals and clear buttons
        findViewById<Button>(R.id.equal).setOnClickListener { onEqualsPressed() }
        findViewById<Button>(R.id.delete).setOnClickListener { onClearPressed() }


    }

    // Handles number and dot button presses
    fun onNumberPressed(number: String) {
        if (isNewCalculation) {
            // If a new calculation is starting, reset currentNumber
            currentNumber = ""
            isNewCalculation = false
        }

        // Check if the display is currently "0" and the user pressed "0" again
        if (number == "0") {
            if (currentNumber.isEmpty() || currentNumber == "0") {
                // Do nothing if the display is already "0"
                return
            }
        }

        // Handle the case where currentNumber is empty and the user presses "."
        if (number == "." && currentNumber.isEmpty()) {
            currentNumber = "0."
        } else {
            currentNumber += number
        }

        // Update the display
        display.text = currentNumber
    }


    // Handles operator button presses
    fun onOperatorPressed(op: String) {
        firstOperand = currentNumber.toDoubleOrNull() // Store the current number as the first operand
        currentNumber = "" // Clear the current number for the next input
        operator = op // Store the selected operator
    }

    // Handles the equals button press
    fun onEqualsPressed() {
        val secondOperand = currentNumber.toDoubleOrNull()
        if (firstOperand != null && secondOperand != null && operator != null) {
            if (operator == "/" && secondOperand == 0.0) {
                display.text = "Error"
                resetCalculator()
                return
            }

            val result = when (operator) {
                "+" -> firstOperand!! + secondOperand
                "-" -> firstOperand!! - secondOperand
                "*" -> firstOperand!! * secondOperand
                "/" -> firstOperand!! / secondOperand
                else -> 0.0
            }

            val resultText = if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }

            display.text = resultText
            currentNumber = resultText
            operator = null
            isNewCalculation = true // Set the flag to true after calculation
        } else {

            resetCalculator()
        }
    }



    // Handles the percent button press
    fun onPercentPressed() {
        val value = currentNumber.toDoubleOrNull()
        if (value != null) {
            // Calculate the percentage (divide by 100)
            currentNumber = (value / 100).toString()
            display.text = currentNumber
        }
    }

    // Handles the negative button press
    fun onNegativePressed() {
        if (currentNumber.isEmpty()) {
            // If no number has been entered, assume the user wants to start with a negative number
            currentNumber = "-"
        } else {
            // If the current number is negative, remove the '-' sign
            if (currentNumber.startsWith("-")) {
                currentNumber = currentNumber.substring(1)
            } else {
                // Otherwise, add the '-' sign to make it negative
                currentNumber = "-$currentNumber"
            }
        }
        display.text = currentNumber
    }


    // Handles the clear button press
    fun onClearPressed() {
        currentNumber = "" // Clear the current input
        operator = null // Reset the operator
        firstOperand = null // Reset the first operand
        display.text = "0" // Reset the display to 0
    }

    fun resetCalculator() {
        currentNumber = ""
        operator = null
        firstOperand = null
    }
}
