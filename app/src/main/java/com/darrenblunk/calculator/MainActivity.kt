package com.darrenblunk.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import java.util.*


class MainActivity : AppCompatActivity() {

    var displaySum = ""

    lateinit var textInput:AppCompatTextView
    lateinit var outputText:AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val zero = findViewById<ImageView>(R.id.BtZero)
        val one = findViewById<ImageView>(R.id.BtOne)
        val two = findViewById<ImageView>(R.id.BtTwo)
        val three = findViewById<ImageView>(R.id.BtThree)
        val four = findViewById<ImageView>(R.id.BtFour)
        val five = findViewById<ImageView>(R.id.BtFive)
        val six = findViewById<ImageView>(R.id.BtSix)
        val seven = findViewById<ImageView>(R.id.BtSeven)
        val eight = findViewById<ImageView>(R.id.BtEight)
        val nine = findViewById<ImageView>(R.id.BtNine)
        val btEquals = findViewById<ImageView>(R.id.BtEquals)
        val btPlus = findViewById<ImageView>(R.id.BtPlus)
        val btMinus = findViewById<ImageView>(R.id.BtMinus)
        val btMultiple = findViewById<ImageView>(R.id.BtMultiple)
        val btPoint = findViewById<ImageView>(R.id.BtPoint)
        val btDivide = findViewById<ImageView>(R.id.BtDivide)
        val btModulus = findViewById<ImageView>(R.id.BtPercent)
        val btback = findViewById<ImageView>(R.id.BtDelete)
        val btClearScreen = findViewById<ImageView>(R.id.BtClrSrn)
        textInput = findViewById<AppCompatTextView>(R.id.TextInput)
        outputText = findViewById<AppCompatTextView>(R.id.TextOutput)

        zero.setOnTouchListener(listener)
        one.setOnTouchListener(listener)
        two.setOnTouchListener(listener)
        three.setOnTouchListener(listener)
        four.setOnTouchListener(listener)
        five.setOnTouchListener(listener)
        six.setOnTouchListener(listener)
        seven.setOnTouchListener(listener)
        eight.setOnTouchListener(listener)
        nine.setOnTouchListener(listener)
        btEquals.setOnTouchListener(listener)
        btPlus.setOnTouchListener(listener)
        btMinus.setOnTouchListener(listener)
        btMultiple.setOnTouchListener(listener)
        btPoint.setOnTouchListener(listener)
        btDivide.setOnTouchListener(listener)
        btModulus.setOnTouchListener(listener)
        btback.setOnTouchListener(listener)
        btClearScreen.setOnTouchListener(listener)

        //var result = Regex("([\\/\\+\\-\\*\\%])?(\\d*\\.?\\d+)").findAll("5+5",0)



    }

    var listener : View.OnTouchListener = View.OnTouchListener { v, me ->
        when(me.action) {
            MotionEvent.ACTION_DOWN -> {
                v.setBackgroundColor(ContextCompat.getColor(this,R.color.Semi_Transparent))
                textInput.text = onClick(v.tag)
            }
            MotionEvent.ACTION_UP -> v.setBackgroundColor(0)
        }

        true
    }
    fun searchPoint() : Boolean {
        for(index in displaySum.reversed()) {
        when(index) {
            '+','-','/','x','%' ->return true
            '.' -> return false
            }
        }
        return true
    }
        fun onClick(tag : Any) : String {

            when(tag.toString()[0]){
                '.' ->{  //Adds point to the number
                    if(displaySum.isNotEmpty()){
                        if(searchPoint()){

                            if(displaySum.last().isDigit())
                                displaySum+= tag.toString()[0]
                            else
                                displaySum+= "0."
                        }
                    }
                    else
                        displaySum+= "0."
                }
                '=' -> {
                    //Code goes here
                    displaySum = outputText.text.toString()
                }
                'd' -> { //Deletes the last character in the string
                    displaySum = displaySum.dropLast(1)
                }
                'c' -> { //Clear Screen - returns empty string
                    displaySum = ""
                }

                in '0'..'9' ->{ //Returns digits
                    displaySum+= tag.toString()[0]

                }
                'x','+','/','%' ->{  //Returns Operators
                    if(displaySum.isNotEmpty()) {
                        if (displaySum[displaySum.lastIndex] == '-') {
                            displaySum = displaySum.dropLast(1)
                        }
                    }
                    if(displaySum.isNotEmpty())
                    {
                        if(!displaySum[displaySum.lastIndex].isDigit())
                            displaySum = displaySum.dropLast(1)

                        displaySum+= tag.toString()[0]
                    }

                }
                '-' ->{
                    if(displaySum.isNotEmpty())
                    {
                        when(displaySum[displaySum.lastIndex]){
                            '+','-','.' ->{
                                displaySum = displaySum.dropLast(1)
                            }
                        }
                    }
                    displaySum+= tag.toString()[0]
                }

            }
        //The Input is done

            if(displaySum != "")
            {
                if(displaySum.last().isDigit())
                {
                    val total = InputParser(displaySum)

                    //if(total.dec().toString().substringAfterLast('.').toInt() > 0)
                    if(total != total.toInt().toFloat())// It's used to check if there's a decimal point or not
                        outputText.text = String.format("%.2f",total)
                    else
                        outputText.text = String.format("%.0f",total)
                }
            }
            else{
                outputText.text = ""
                return "0"
            }


            return displaySum
        }



    fun InputParser(expression : String) : Float{

        //Parsing String to an Array
        var tempNumber : String = ""
        var sumParse : MutableList<String> = java.util.ArrayList()

        for(i in expression){
            if(i.isDigit())
            {
                tempNumber += i.toString()

                if(sumParse.count() == 0)
                    sumParse.add(tempNumber)
                else{
                    if(sumParse.last()[0].isDigit() || sumParse.last()[0] == '.') {
                        sumParse[sumParse.lastIndex] = tempNumber
                    }
                    else {
                        sumParse.add(tempNumber)
                    }
                }
            }
            else{
                if(i == '-'){
                    if(sumParse.count() == 0 || !sumParse[sumParse.lastIndex][0].isDigit())
                        tempNumber += i.toString()
                    else
                    {
                        tempNumber = ""
                        sumParse.add(i.toString())
                    }
                }
                else
                {
                    if(i != '.'){
                        tempNumber = ""
                        sumParse.add(i.toString())
                    }
                    else {
                        tempNumber += i.toString()
                    }
                }


            }
        }
        if(sumParse.count() < 2){
            return 0f
        }
        //Parsing array end

        //Calculating using operator precedence
        var numStack = Stack<Float>()
        var operatorStack = Stack<Char>()

        var tempNum : Float = 0f
        var tempOper : Char = ','

        val high = "/x%"
        val low = "+-"

        for (i in sumParse){
            if(tempOper != ','){
                numStack.push(sumof(tempNum,i.toFloat(),tempOper))
                tempNum = 0f
                tempOper = ','
                continue
            }
            if(i[i.lastIndex].isDigit()){ //i is a number
                numStack.push(i.toFloat())
            }
            else{   //i is an operator
                if(operatorStack.isNotEmpty()){
                    when( operatorStack.peek()){ //checking if the previous operator has high value using when
                        in low.toCharArray() -> {  //if previous operator is '+ - ' as they have low Precedence
                            when(i[0]){
                                in high.toCharArray() ->{ //if current operator is '/ x % ' as they have high Precedence
                                    tempNum = numStack.pop()
                                    tempOper = i[0]
                                }
                                else -> {
                                    operatorStack.push(i[0])
                                }
                            }
                        }
                        else -> {
                            operatorStack.push(i[0])
                        }
                    }
                }
                else {
                    operatorStack.push(i[0])
                }

            }
        }

        var result : Float
        result = numStack[0]

        for(index in 1 until  numStack.count()){
            result = sumof(result,numStack[index],operatorStack[index -1])
        }


        return result

    }

    fun sumof(a:Float,b:Float,operator : Char) :Float{
        var temp = 0f

        when(operator){
            '/' -> {
                temp = a / b
            }
            'x' -> {
                temp = a * b
            }
            '%' -> {
                temp = a % b
            }
            '+' -> {
                temp = a + b
            }
            '-' -> {
                temp = a - b
            }
        }
        return temp
    }
}