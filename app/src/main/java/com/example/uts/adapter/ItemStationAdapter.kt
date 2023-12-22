package com.example.uts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.databinding.ItemStationBinding
import com.example.uts.model.Station

class ItemStationAdapter(): RecyclerView.Adapter<ItemStationAdapter.ItemStationViewHolder>(){

    private var stationList: List<Station> = listOf()

    inner class ItemStationViewHolder(private val binding: ItemStationBinding):
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(station: Station) {
            binding.apply {
                txtStationName.text = "${station.name} (${station.code})"
                txtStationLocation.text = "${station.district}, ${station.regency}, ${station.province}"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemStationAdapter.ItemStationViewHolder {
        val binding = ItemStationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ItemStationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemStationAdapter.ItemStationViewHolder, position: Int) {
        holder.bind(stationList[position])
    }

    override fun getItemCount(): Int {
        return stationList.size
    }

    fun setStationList(stationList: MutableList<Station>) {
        this.stationList = stationList
        notifyDataSetChanged()
    }
}