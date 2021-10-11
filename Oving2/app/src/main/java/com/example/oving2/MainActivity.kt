package com.example.oving2

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private var maxValue: Int = 5
    private var value: Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     *  Start ny aktivitet, og lytt etter resultatet
     */
    fun onClickStartRandomNumActivity(v: View?) {
        val intent = Intent("com.example.RandomNumActivity")
        val numberView= findViewById<EditText>(R.id.textNumberMain)
       // Toast.makeText(applicationContext, numberView.text.toString(), Toast.LENGTH_SHORT).show()
        val int: Int? = numberView.text.toString().toIntOrNull()
        if(int!=null){
            intent.putExtra("maxValueIntent",int)
            startActivityForResult(intent, 1)
        }else{
            Toast.makeText(applicationContext, "Skriv et tall", Toast.LENGTH_SHORT).show()
        }
    }
    fun onClickStartCalculator(v:View?){
        setContentView(R.layout.activity_equation)
    }

    fun onClickMultiply(v:View?){
        val firstNum= findViewById<TextView>(R.id.first_number).text.toString().toIntOrNull()
        val secondNum= findViewById<TextView>(R.id.second_number).text.toString().toIntOrNull()
        val answer = findViewById<TextView>(R.id.answer).text.toString().toIntOrNull()
            if(firstNum!!* secondNum!! ==answer){
                Toast.makeText(applicationContext, getString(R.string.Riktig), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, getString(R.string.Feil_Riktig_svar_er) + firstNum*secondNum, Toast.LENGTH_SHORT).show()
            }
        val maxInt = findViewById<TextView>(R.id.upper_limit).text.toString().toIntOrNull()
        findViewById<TextView>(R.id.first_number).setText((0..maxInt!!).random().toString())
        findViewById<TextView>(R.id.second_number).setText((0..maxInt!!).random().toString())
    }
    fun onClickAdd(v:View?){
        val firstNum= findViewById<TextView>(R.id.first_number).text.toString().toIntOrNull()
        val secondNum= findViewById<TextView>(R.id.second_number).text.toString().toIntOrNull()
        val answer = findViewById<TextView>(R.id.answer).text.toString().toIntOrNull()
            if (firstNum!! + secondNum!! == answer) {
                Toast.makeText(applicationContext, getString(R.string.Riktig), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.Feil_Riktig_svar_er) + firstNum + secondNum,
                    Toast.LENGTH_SHORT
                ).show()
            }
        val maxInt = findViewById<TextView>(R.id.upper_limit).text.toString().toIntOrNull()
        findViewById<TextView>(R.id.first_number).setText((0..maxInt!!).random().toString())
        findViewById<TextView>(R.id.second_number).setText((0..maxInt!!).random().toString())
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            Log.e("onActivityResult()", "Noe gikk galt")
            return
        }else{
            value = data.getIntExtra("number",0)
            findViewById<TextView>(R.id.textViewResult).setText(value.toString())
            Toast.makeText(applicationContext, "result: "+value, Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Viser en AlertDialog med knapper, når knappene blir trykket på blir den valgte
     * knappen skrevet ut i en Toast
     */
    fun onClickVisAlertDialog(v: View?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("AlertDialog")
        builder.setMessage("Forklarende tekst")
        builder.setPositiveButton("positiv") { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext, "positiv", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("negativ") { _, _ ->
            Toast.makeText(applicationContext, "negativ", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("nøytral") { _, _ ->
            Toast.makeText(applicationContext, "nøytral", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
}