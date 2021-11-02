package com.example.oving7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.oving7.databinding.ActivityMainBinding
import com.example.oving7.managers.FileManager
import com.example.oving7.managers.MyPreferenceManager
import com.example.oving7.service.Database
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var db: Database


    companion object{
        public lateinit var mainLayout: ActivityMainBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainLayout.root)

        db = Database(this)
        //MyPreferenceManager(this)
        FileManager(this)
        MyPreferenceManager(this).updateBackgroundColor()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "Alle filmer")
        menu.add(0, 2, 0, "Alle skuespillere")
        menu.add(0, 3, 0, "Alle regissører")
        menu.add(0, 4, 0, "Filmer med Mark Ruffalo")
        menu.add(0, 5, 0, "Sluespillere i \"Avengers: Age of Ultron(2015)\"")
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val array: IntArray = resources.getIntArray(R.array.color_values)
        when (item.itemId) {
           R.id.settings -> startActivity(Intent("com.example.oving7.SettingsActivity"))
           // R.id.settings -> mainLayout.textViewMain.setBackgroundColor(array[2])
            1             -> showResults(db.allMovies)
            2             -> showResults(db.allActors)
            3             -> showResults(db.allDirectors)
            4             -> showResults(db.getMoviesWithActor("Mark Ruffalo"))
            5             -> showResults(db.getActorsInMovie("Avengers: Age of Ultron(2015)"))
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResults(list: ArrayList<String>) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        mainLayout.textViewMain.text = res
    }

}