package inft2501.leksjon_06.tjener

import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket


 class Server(val textView: TextView, private val PORT: Int = 12345) {

	/**
	 * Egendefinert set() som gjør at vi enkelt kan endre teksten som vises i skjermen til
	 * emulatoren med
	 *
	 * ```
	 * ui = "noe"
	 * ```
	 */
	companion object {

		val clientHandlers : ArrayList<ClientHandler> = ArrayList()
		fun sendToClients(message: String){
			for(handler in clientHandlers){
				val writer = PrintWriter(handler.getSocket().getOutputStream(), true)
				writer.println(message)
			}
		}
		fun changeUI(message: String){
			//unable to
		}
	}

	var ui: String? = ""
		set(str) {
			MainScope().launch { textView.text = str }
			field = str
		}

	fun start() {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				//ui = "Starter Tjener ..."
				// "innapropriate blocking method call" advarsel betyr at tråden
				// stopper helt opp og ikke går til neste linje før denne fullfører, i dette
				// eksempelet er ikke dette så farlig så vi ignorerer advarselen.
				ServerSocket(PORT).use { serverSocket: ServerSocket ->
					while (true) {
                // Vent på ny forbindelse
						val clientSocket = serverSocket.accept()
						// Start ny tråd eller coroutine for klienten
						ClientHandler(clientSocket).start()
						clientHandlers.add(ClientHandler(clientSocket))
					}

				}

			} catch (e: IOException) {
				e.printStackTrace()
				ui = e.message
			}
		}
	}
}
