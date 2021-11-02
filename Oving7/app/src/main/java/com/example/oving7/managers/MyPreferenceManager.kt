package com.example.oving7.managers

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentContainer
import androidx.preference.PreferenceManager
import com.example.oving7.MainActivity
import com.example.oving7.MainActivity.Companion.mainLayout
import com.example.oving7.R
import com.example.oving7.databinding.ActivityMainBinding


class MyPreferenceManager(private val activity: AppCompatActivity) {

	private val resources = activity.resources
	private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
	private val editor: SharedPreferences.Editor = preferences.edit()


	fun example() {
		val preferences: SharedPreferences = activity.getSharedPreferences("prefs", 0)
		val editor: SharedPreferences.Editor = preferences.edit()
		editor.putString(resources.getString(R.string.background_color), resources.getString(R.string.default_background))
		//editor.putBoolean("darkMode", true)
		editor.apply()
	}

	fun getString(key: String, defaultValue: String): String {
		return preferences.getString(key, defaultValue) ?: defaultValue
	}



	fun updateBackgroundColor() {
		val colorValues = resources.getStringArray(R.array.color_values)
		val value = getString(
				resources.getString(R.string.background_color),
				resources.getString(R.string.default_background)
		)
		when (value) {
			colorValues[0] -> mainLayout.root.setBackgroundColor(Color.parseColor(colorValues[0].toString()))
			colorValues[1] -> mainLayout.root.setBackgroundColor(Color.parseColor(colorValues[1].toString()))
			colorValues[2] -> mainLayout.root.setBackgroundColor(Color.parseColor(colorValues[2].toString()))
			colorValues[3] -> mainLayout.root.setBackgroundColor(Color.parseColor(colorValues[3].toString()))
			colorValues[4] -> mainLayout.root.setBackgroundColor(Color.parseColor(colorValues[4].toString()))
		}
	}

	fun registerListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
		preferences.registerOnSharedPreferenceChangeListener(activity)
	}

	fun unregisterListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
		preferences.unregisterOnSharedPreferenceChangeListener(activity)
	}
}
