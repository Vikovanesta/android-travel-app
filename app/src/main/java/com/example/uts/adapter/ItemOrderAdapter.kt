package com.example.uts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.databinding.ItemOrderBinding
import com.example.uts.model.TravelOrderWithAllFields
import com.example.uts.utils.DateTimeUtil

class ItemOrderAdapter(): RecyclerView.Adapter<ItemOrderAdapter.ItemOrderViewHolder>() {
    private var orderList: List<TravelOrderWithAllFields> = listOf()

    inner class ItemOrderViewHolder(private val binding: ItemOrderBinding):
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: TravelOrderWithAllFields) {
            binding.apply {
                txtTrainName.text = "${order.travel.travel.trainName} ${order.travel.travel.trainNumber}"
                txtWagonClass.text = "${order.travel.travel.wagonClass} (${order.travel.travel.subClass})"
                txtDepartureDate.text = DateTimeUtil.formatDateToString(order.travel.travel.departureDate)
                txtDepartureTime.text = DateTimeUtil.formatTimeToString(order.travel.travel.departureTime)
                txtArrivalDate.text = DateTimeUtil.formatDateToString(order.travel.travel.arrivalDate)
                txtArrivalTime.text = DateTimeUtil.formatTimeToString(order.travel.travel.arrivalTime)
                txtOriginStation.text = "${order.travel.originStation.name} (${order.travel.originStation.code})"
                txtArrivalStation.text = "${order.travel.arrivalStation.name} (${order.travel.arrivalStation.code})"
                txtOrderDatetime.text = "${DateTimeUtil.formatDateToString(order.travelOrder.date)} " +
                        DateTimeUtil.formatTimeToString(order.travelOrder.time)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemOrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ItemOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemOrderAdapter.ItemOrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun setOrderList(orderList: MutableList<TravelOrderWithAllFields>) {
        this.orderList = orderList
            .sortedWith(compareBy( { it.travelOrder.date }, {it.travelOrder.time}))
            .reversed()
        notifyDataSetChanged()
    }
}