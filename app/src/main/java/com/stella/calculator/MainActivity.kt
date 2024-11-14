package com.stella.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var historyTextView: TextView
    private var currentInput = ""
    private var operator = ""
    private var operand1 = 0.0 // Double로 유지
    private var isNewInput = true
    private var history = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
        historyTextView = findViewById(R.id.historyTextView)

        // historyTextView에 스크롤 기능 추가
        historyTextView.movementMethod = ScrollingMovementMethod()
        setButtonListeners()
    }

    private fun setButtonListeners() {
        val buttonIds = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide,
            R.id.buttonEquals, R.id.buttonClear, R.id.buttonBackspace, R.id.buttonDot, R.id.buttonPercent, R.id.button00
        )

        for (id in buttonIds) {
            val button = findViewById<Button>(id)
            button.setOnClickListener { view ->
                onButtonClicked(view as Button)
            }
        }
    }

    private fun onButtonClicked(button: Button) {
        val buttonText = button.text.toString()

        when (buttonText) {
            "C" -> clear()
            "%" -> calculatePercentage()
            "⌫" -> backspace()
            "+", "-", "×", "/" -> setOperator(buttonText)
            "=" -> calculateResult()
            "." -> addDot()
            else -> appendNumber(buttonText)
        }
    }

    private fun clear() {
        currentInput = ""
        operator = ""
        operand1 = 0.0 // 초기값을 0.0으로 설정
        isNewInput = true
        history = ""
        historyTextView.text = ""
        resultTextView.text = "0"
    }

    private fun calculatePercentage() {
        if (currentInput.isNotEmpty()) {
            val percentageValue = currentInput.toDouble() / 100
            currentInput = percentageValue.toString()
            resultTextView.text = formatResult(currentInput.toDouble())
        }
    }

    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            resultTextView.text = if (currentInput.isEmpty()) "0" else currentInput
        }
    }

    private fun setOperator(selectedOperator: String) {
        if (currentInput.isNotEmpty()) {
            operand1 = currentInput.toDouble()
            operator = selectedOperator
            history += "${formatResult(operand1)} $operator "
            historyTextView.text = history
            isNewInput = true
        }
    }

    // 결과 확인(=)
    private fun calculateResult() {
        if (currentInput.isNotEmpty() && operator.isNotEmpty()) {
            val operand2 = currentInput.toDouble()
            val result = when (operator) {
                "+" -> operand1 + operand2
                "-" -> operand1 - operand2
                "×" -> operand1 * operand2
                "/" -> operand1 / operand2
                else -> 0.0
            }

            history += "${formatResult(operand2)} = ${formatResult(result)}\n"
            historyTextView.text = history

            // 스크롤을 최하단으로 이동
            historyTextView.post {
                historyTextView.scrollTo(0, historyTextView.layout.height)
            }

            resultTextView.text = formatResult(result)
            currentInput = result.toString()
            operator = ""
            isNewInput = true
        }
    }
    //수숫점
    private fun addDot() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            resultTextView.text = currentInput
        }
    }
    //숫자 클릭
    private fun appendNumber(number: String) {
        currentInput = if (isNewInput) {
            number
        } else {
            currentInput + number
        }
        isNewInput = false
        resultTextView.text = currentInput
    }

    // 결과값을 포맷하는 함수
    private fun formatResult(value: Double): String {
        return if (value == value.toInt().toDouble()) {
            value.toInt().toString() // 소수점이 없는 경우 정수로 표시
        } else {
            value.toString() // 소수점이 있는 경우 그대로 표시
        }
    }
}
