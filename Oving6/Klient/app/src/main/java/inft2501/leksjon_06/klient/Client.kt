package inft2501.leksjon_06.klient

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import javax.security.auth.callback.Callback

class Client(
	private val textRecieved: TextView,
	private val textSent: TextView,
	private val button: Button,
	private val editMessage: EditText,
	private val SERVER_IP: String = "10.0.2.2",
	private val SERVER_PORT: Int = 12345,
) {

	/**
	 * Egendefinert set() som gjør at vi enkelt kan endre teksten som vises i skjermen til
	 * emulatoren med
	 *
	 * ```
	 * ui = "noe"
	 * ```
	 */
	private var uiRecieved: String? = ""
		set(str) {
			MainScope().launch { textRecieved.text = str }
			field = str
		}
	private var uiSent: String? = ""
		set(str) {
			MainScope().launch { textSent.text = str }
			field = str
		}


	fun start() {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val socket:Socket = Socket(SERVER_IP, SERVER_PORT)
					uiRecieved = "Messages recieved:\n"
				uiSent = "Sent messages:\n"
					//delay(5000)
					//readFromServer(socket)
					CoroutineScope(IO).launch {
						while(true){
							delay(1000)
							readFromServer(socket)
						}
					}

					//delay(5000)
					//sendToServer(socket, "Heisann Tjener! Hyggelig å hilse på deg")
						while(true){
							delay(1000)
							button.setOnClickListener{
								val message :String = editMessage.text.toString()

								CoroutineScope(IO).launch {sendToServer(socket,message)}
							}
					}

			} catch (e: IOException) {
				e.printStackTrace()
				uiRecieved = e.message
			}
		}
	}
	/*private fun readFromServer(socket: Socket, callback: (socket:Socket) -> String) {
		CoroutineScope(IO).launch {
			val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
			val message = reader.readLine()
			while(true){
				val returned: String = callback(socket)
			}
		}


		uiRecieved = "Melding fra tjeneren:\n$message"
	}

	 */

	private fun readFromServer(socket: Socket) {
		val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
		val message = reader.readLine()
		if(message!=null){
		uiRecieved += "$message\n"
			Log.i("READ FROM SERVER","read message from server")
		}
	}



	private fun sendToServer(socket: Socket, message: String) {
		val writer = PrintWriter(socket.getOutputStream(), true)

		if(message!="") {
			writer.println(message)
			uiSent += "$message\n"
			Log.i("SENT TO SERVER", "sent: '" + message + "' to the server")
		}
	}
}
