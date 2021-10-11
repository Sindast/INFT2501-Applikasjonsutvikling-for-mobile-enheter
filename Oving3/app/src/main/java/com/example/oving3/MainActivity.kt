package com.example.oving3

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : Activity() {
    @RequiresApi(Build.VERSION_CODES.O)
    private var formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
    private var friendNames : Array<String> = arrayOf()
    private var friendDates : Array<String> = arrayOf()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        friendNames = resources.getStringArray(R.array.friendNames)
        friendDates = resources.getStringArray(R.array.friendDates)
        initSpinner()
        initList()
    }
    //function for adding new elements to string array
    fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }
    private fun initSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, friendNames)
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                valgt: View,
                posisjon: Int,
                id: Long
            ) {
                findViewById<TextView>(R.id.date).text = friendDates[posisjon]
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }
    private fun initList() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, friendNames)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, valgt: View, posisjon: Int, id: Long ->
                findViewById<TextView>(R.id.date).text = friendDates[posisjon]
                findViewById<Spinner>(R.id.spinner).setSelection(posisjon)
            }
    }
    fun onClickAddFriend(v:View?) {
        setContentView(R.layout.add_friend)
        val addName :EditText = findViewById(R.id.addName)
        val addDate :DatePicker = findViewById(R.id.addDate)
        val addButton:Button = findViewById(R.id.addFriendButton)

        addButton.setOnClickListener{
            friendNames = append(friendNames,addName.text.toString())
            friendDates =append(friendDates,(addDate.dayOfMonth.toString()+"-"+(addDate.month+1).toString()+"-"+addDate.year.toString()))
            setContentView(R.layout.activity_main)
            initSpinner()
            initList()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickEditFriend(v:View?){
        val spinnerIndex = findViewById<Spinner>(R.id.spinner).selectedItemPosition
        setContentView(R.layout.edit_friend)
        val selectedDateText = friendDates[spinnerIndex]
       // println(selectedDateText)
        val selectedDate :LocalDate = LocalDate.parse(selectedDateText,formatter);
        val datePicker :DatePicker = findViewById(R.id.editDate)
        val editName :EditText = findViewById(R.id.editName)
        val updateButton :Button = findViewById(R.id.updateFriendButton)
     //   println("Day of month: "+ selectedDate.dayOfMonth +"\n Month: "+selectedDate.monthValue+"\n Year: "+selectedDate.year)
        datePicker.updateDate(selectedDate.year,selectedDate.monthValue-1,selectedDate.dayOfMonth)
        editName.setText(friendNames[spinnerIndex])
        updateButton.setOnClickListener{
            friendNames[spinnerIndex]=editName.text.toString()
        //    println(editName.text.toString())
            friendDates[spinnerIndex]=(datePicker.dayOfMonth.toString()+"-"+(datePicker.month+1).toString()+"-"+datePicker.year.toString())
          //  println(datePicker.dayOfMonth.toString()+"-"+(datePicker.month+1).toString()+"-"+datePicker.year.toString())
            setContentView(R.layout.activity_main)
            initSpinner()
            initList()
        }
    }

    fun onCLickGoBack(v:View?){
        setContentView(R.layout.activity_main)
        initSpinner()
        initList()
    }
}

