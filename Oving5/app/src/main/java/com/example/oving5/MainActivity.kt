package com.example.oving5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


const val URL = "http://10.0.2.2:8080/oving5/tallspill.jsp"
class MainActivity : AppCompatActivity() {
    private val network: HttpWrapper = HttpWrapper(URL)
    private var n :Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

   fun onClickLogIn(v: View?){
       val name:String = findViewById<EditText>(R.id.editName).text.toString()
       val cardNumber:String = findViewById<EditText>(R.id.editKortNummer).text.toString()
       performRequest(mapOf(
           "navn" to name,
           "kortnummer" to cardNumber,
       )){
           if(it.contains("Oppgi et tall", ignoreCase = true)){
               setResult(it)
               findViewById<Button>(R.id.guessButton).visibility=View.VISIBLE
           }
       }
       setContentView(R.layout.guessing_game)
    }
    fun onClickLogOut(v:View?){
        setContentView(R.layout.activity_main)
        Log.i("LOGOUT","Loggin out")
    }
    fun onClickGuess(v:View?) {
        val editGuessNumber :EditText = findViewById(R.id.editGuessNumber)
        val guessNumber: String = editGuessNumber.text.toString()
        val guessButton: Button = findViewById(R.id.guessButton)
        performRequest(mapOf(
            "tall" to guessNumber
        )) {
            val victoryMessage = "du har vunnet"
            val noMoreChances = "ingen flere sjanser"

            when {
                it.contains(victoryMessage) -> {
                    setResult(it)
                    guessButton.visibility=View.INVISIBLE
                    Log.i("Game is won! ", it)
                }

                it.contains(noMoreChances)->{
                  setResult(it)
                    guessButton.visibility=View.INVISIBLE
                    Log.i("All guesses spent! ",it)
                }
                else ->{
                    setResult(it)
                    Log.i("Number guessed: ",it)
                }
            }

            }
        }

    fun onClickTextUpdate(v:View?){
        findViewById<TextView>(R.id.result).text = ("test$n").toString()
        n+=1
    }
    private fun performRequest(parameterList: Map<String, String>, onResult: (String) -> Unit) {
        CoroutineScope(IO).launch {
            val response: String = try {
                network.get(parameterList)
            } catch (e: Exception) {
                Log.e("performRequest()", e.message!!)
                e.toString()
            }

            // Endre UI på hovedtråden
            MainScope().launch {
                onResult(response)
                Log.e("RESPONSE FROM SERVER", response)
            }
        }
    }

    private fun setResult(response: String) {
        findViewById<TextView>(R.id.result).text = response
    }
}