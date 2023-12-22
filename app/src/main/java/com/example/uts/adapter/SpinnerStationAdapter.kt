package com.example.uts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.uts.model.Station

class SpinnerStationAdapter(
    private val context: Context,
    private val stations: List<Station>
): BaseAdapter() {
    override fun getCount(): Int {
        return stations.size
    }

    override fun getItem(position: Int): Any {
        return stations[position]
    }

    override fun getItemId(position: Int): Long {
        return stations[position].id.hashCode().toLong()
    }

    fun getPosition(stationId: String): Int {
        return stations.indexOfFirst { it.id == stationId }
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_item,
            parent,
            false
        )

        val station = stations[position] as Station

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "${station.name} (${station.code})"

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_dropdown_item,
            parent,
            false
        )

        val station = stations[position] as Station

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "${station.name} (${station.code})"

        return view
    }


}