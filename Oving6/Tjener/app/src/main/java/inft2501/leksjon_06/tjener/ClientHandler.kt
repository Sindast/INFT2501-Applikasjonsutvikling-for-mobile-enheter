package inft2501.leksjon_06.tjener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientHandler(clientSocket: Socket) {
private val socket :Socket = clientSocket

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                delay(5000)
                CoroutineScope(Dispatchers.IO).launch {
                    while (true) {
                        delay(1000)
                        val test = readFromClient(socket)
                        if(test!=null){
                            Server.sendToClients(test)
                        }
                    }
                }

                //delay(5000)
                //sendToServer(socket, "Heisann Tjener! Hyggelig å hilse på deg")
                while (true) {
                    delay(1000)

                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getSocket():Socket{
        return socket
    }
    private fun readFromClient(socket: Socket): String? {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val message = reader.readLine()
        return message
    }


}