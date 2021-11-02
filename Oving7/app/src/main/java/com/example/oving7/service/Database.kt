package com.example.oving7.service

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Output
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.oving7.R
import com.example.oving7.managers.DataBaseManager
import com.example.oving7.managers.FileManager
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

class Database(context: Context): DataBaseManager(context) {
    private val file: File = File(context.filesDir, "filename2.txt")
    init {
        try{
            val content = StringBuffer("")
            this.clear()
            val inputStream: InputStream = context.resources.openRawResource(R.raw.test)
            var input : List<String>
            val reader = BufferedReader(InputStreamReader(inputStream)).use {
                    reader ->
                var line = reader.readLine()
                while (line != null) {
                    input = line.toString().split(",")
                    this.insert(input[0],input[1],input[2])
                 //   Log.e("TEST-READLINE",line)
                 //   Log.e("TEST-READLINE",line.toString())
                    content.append(line)
                    content.append("\n")
                    line = reader.readLine()
                }
                file.printWriter().use { out -> out.print(content)}
            }

           /* this.insert("Avengers: Age of Ultron(2015)","Joss Whedon", "Aaron Taylor-Johnson")
            this.insert("Avengers: Age of Ultron(2015)","Russo Brothers", "Robert Downey Jr")
            this.insert("Avengers: Age of Ultron(2015)","Joss Whedon", "Chris Evans")
            this.insert("Avengers: Age of Ultron(2015)","Joss Whedon", "Mark Ruffalo")

            */



        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTOR, arrayOf(ID, ACTOR_NAME))

    val allMovies: ArrayList<String>
        get() = performQuery(TABLE_MOVIE, arrayOf(ID, MOVIE_TITLE), null)

    val allDirectors: ArrayList<String>
        get() = performQuery(TABLE_DIRECTOR, arrayOf(ID, DIRECTOR_NAME))

    fun getMoviesWithActor(actor: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_TITLE")
        val from = arrayOf(TABLE_ACTOR, TABLE_MOVIE, TABLE_ACTOR_MOVIE)
        val join = JOIN_ACTOR_MOVIE
        val where = "$TABLE_ACTOR.$ACTOR_NAME='$actor'"

        return performRawQuery(select, from, join, where)
    }
    fun getActorsInMovie(movie: String): ArrayList<String> {
        val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
        val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_ACTOR_MOVIE)
        val join = JOIN_ACTOR_MOVIE
        val where = "$TABLE_MOVIE.$MOVIE_TITLE='$movie'"

        return performRawQuery(select, from, join, where)
    }
}