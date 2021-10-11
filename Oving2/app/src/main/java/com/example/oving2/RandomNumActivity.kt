package com.example.oving2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class RandomNumActivity : Activity() {
    private var maxInt = 2
    private var minInt = 0
//    private val textView: TextView = findViewById(R.id.randomNumTextView);
        @SuppressLint("SetTextI18n")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            maxInt=intent.getIntExtra("maxValueIntent",maxInt)
        Log.i("Intent",maxInt.toString())
           // textView.setText("Generate a random number between 0 and ")
            setContentView(R.layout.activity_random_number)
        }
    fun onClickExitActivity(v: View?) {
        val value = (minInt..maxInt).random()
        setResult(RESULT_OK, Intent().putExtra("number",value))
       // Toast.makeText(applicationContext,value.toString(), Toast.LENGTH_SHORT).show()
        Log.i("RANDOM_NUM",value.toString())
        finish()
    }

    fun onClickRandomNumber(v:View?){
        val value = (minInt..maxInt).random()
        Toast.makeText(applicationContext,value.toString(), Toast.LENGTH_SHORT).show()
    }
        /**
         *  Avslutt aktivitet og gå tilbake til MainActivity, legg ved intent med riktig flagg
         */
}