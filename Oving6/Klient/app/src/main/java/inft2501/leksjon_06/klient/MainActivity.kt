package inft2501.leksjon_06.klient

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val textViewRecieved = findViewById<TextView>(R.id.textViewRecieved)
		val textViewSent = findViewById<TextView>(R.id.textViewSent)
		val sendButton = findViewById<Button>(R.id.sendButton)
		val editMessage : EditText = findViewById(R.id.editMessage)
		Client(textViewRecieved,textViewSent,sendButton,editMessage).start()
	}
	fun onClickSendMessage(V: View?){

		//val message :String = editMessage.text.toString()

	}
}
