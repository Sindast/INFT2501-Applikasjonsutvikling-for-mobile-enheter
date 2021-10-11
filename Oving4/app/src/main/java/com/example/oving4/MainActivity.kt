package com.example.oving4

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), NamesFragment.CallBackListener{
private var currentPosition = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOrientation(resources.configuration)
        showImage(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_next -> changeDescription(currentPosition+1)
            R.id.menu_previous -> changeDescription(currentPosition-1)
            else -> return false
        }
        return true
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setOrientation(newConfig)
        Log.e("Hei","hello")
        changeDescription(currentPosition)
    }


    private fun setOrientation(config: Configuration) {
        val transaction: FragmentTransaction =
            supportFragmentManager.beginTransaction()
       /* if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            transaction.replace(R.id.namesFragmentView, NamesFragment())
            transaction.replace(R.id.imageFragmentView,ImageFragment())
        } else {

        */
            transaction.replace(R.id.namesFragmentView, NamesFragment())
            transaction.replace(R.id.imageFragmentView,ImageFragment())
        //}
        transaction.commit()
    }


    private val imageFragment
        get() = supportFragmentManager.findFragmentById(R.id.imageFragmentView) as ImageFragment

    private fun showImage(show: Boolean) {
        val transaction: FragmentTransaction =
            supportFragmentManager.beginTransaction()
        if (show) transaction.show(imageFragment) else transaction.hide(imageFragment)
        transaction.commit()
    }
    /*private var names :Array<String> = arrayOf()
    private var description : Array<String> = arrayOf()
    private var images : TypedArray? = null
     */

    private fun changeDescription(int: Int){
        val title = findViewById<TextView>(R.id.fragment_title)
        val description = findViewById<TextView>(R.id.fragment_description)
        val image = findViewById<ImageView>(R.id.fragment_imageView)
        val names :Array<String> = resources.getStringArray(R.array.pokemonName)
        if(int<0 || int>names.size-1){
            return
        }
        val descriptions : Array<String> = resources.getStringArray(R.array.pokemonDescription)
        val images : TypedArray = resources.obtainTypedArray(R.array.images)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        title.setText(names[int].toString())
        description.setText(descriptions[int].toString())
        image.setImageResource(images.getResourceId(int,0))
        transaction.commit()
        currentPosition = int
    }

    private fun findImage(nr: Int){
        val imageArray = resources.obtainTypedArray(R.array.images)
        val image = imageArray.getDrawable(nr)
    }

   override fun onFragmentInteraction(position: Int) {
        changeDescription(position)
    }

}

