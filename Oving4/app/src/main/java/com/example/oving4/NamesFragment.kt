package com.example.oving4


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.telecom.Call
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.ListFragment

class NamesFragment: ListFragment(){
    private var names: Array<String> = arrayOf()
    private var clicked = -1
    var mListener: CallBackListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        names = resources.getStringArray(R.array.pokemonName)
        listAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_list_item_1,android.R.id.text1, names)
        }

    }
     override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
         clicked = position
         mListener?.onFragmentInteraction(position)
    }

    interface CallBackListener {
        fun onFragmentInteraction(position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            activity as CallBackListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement OnFragmentInteractionListener"
            )
        }

    }
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}