package com.example.calculatorapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var expression: Expression

    var lastNumeric = false
    var stateError = false
    var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString()
        binding.resultTv.text = ""
        binding.resultTv.visibility = View.GONE
        lastDot = false
    }


    fun onDigitClick(view: View) {
        if(stateError){
                binding.dataTv.text = (view as Button).text
                stateError = false
        }
        else{
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }


    public fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric){

            binding.dataTv.append((view as Button).text)
            lastNumeric = false
            lastDot = false
            onEqual()
        }
    }


    fun onBackClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        try{
            val lastchar = binding.dataTv.text.toString().last()
            if(lastchar.isDigit()){
                onEqual()
            }
        }
        catch (e : Exception){
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char Error",e.toString())
        }
    }

    fun onDotClick(view: View) {
        if (!lastDot && lastNumeric && !stateError) {
            binding.dataTv.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
    }


    fun onAllClearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        lastNumeric = false
        lastDot = false
        stateError = false
        binding.resultTv.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun onEqual(){
        if(lastNumeric && !stateError){

            val txt = binding.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()

            try{
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE

                binding.resultTv.text = result.toString()
            }
            catch (ex : ArithmeticException){
                Log.e("evaluate Error", ex.toString())
                binding.resultTv.text = "Error"

                stateError = true
                lastNumeric = false
            }
        }
    }


}