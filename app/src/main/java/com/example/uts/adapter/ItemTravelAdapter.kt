package com.example.uts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.databinding.ItemTravelBinding
import com.example.uts.model.TravelWithAllFields
import com.example.uts.utils.DateTimeUtil
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Time
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ItemTravelAdapter(private val action: (action: String, travelId: String) -> Unit) :
    RecyclerView.Adapter<ItemTravelAdapter.ItemTravelViewHolder>() {

    private var travelList: List<TravelWithAllFields> = listOf()
    private var userRole: String = ""
    private var userId: String = ""


    inner class ItemTravelViewHolder(private val binding: ItemTravelBinding):
        RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(travel: TravelWithAllFields) {
                binding.apply {
                    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")) //
                    val formattedPrice = currencyFormat.format(travel.travel.price)

                    txtTrainName.text = "${travel.travel.trainName} ${travel.travel.trainNumber}"
                    txtWagonClass.text = "${travel.travel.wagonClass} (${travel.travel.subClass})"
                    txtDepartureDate.text = DateTimeUtil.formatDateToString(travel.travel.departureDate)
                    txtDepartureTime.text = DateTimeUtil.formatTimeToString(travel.travel.departureTime)
                    txtArrivalDate.text = DateTimeUtil.formatDateToString(travel.travel.arrivalDate)
                    txtArrivalTime.text = DateTimeUtil.formatTimeToString(travel.travel.arrivalTime)
                    txtPrice.text = formattedPrice
                    txtOriginStation.text = "${travel.originStation.name} (${travel.originStation.code})"
                    txtArrivalStation.text = "${travel.arrivalStation.name} (${travel.arrivalStation.code})"

                    if (userRole == "user") {
                        btnOrder.setOnClickListener {
                            action("orderTravel", travel.travel.id)
                        }
                    }

                    if (userRole == "admin") {
                        btnOrder.visibility = android.view.View.GONE
                        btnDelete.visibility = android.view.View.VISIBLE
                        btnEdit.visibility = android.view.View.VISIBLE

                        btnDelete.setOnClickListener {
                            action("openConfirmationDialog", travel.travel.id)
                        }
                        btnEdit.setOnClickListener {
                            action("openEditTravelFragment", travel.travel.id)
                        }
                    }
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemTravelViewHolder {
        val binding = ItemTravelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ItemTravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTravelViewHolder, position: Int) {
        holder.bind(travelList[position])
    }

    override fun getItemCount(): Int {
        return travelList.size
    }

    fun setTravelList(travelList: List<TravelWithAllFields>) {
        this.travelList = travelList
            .sortedWith(compareBy({ it.travel.departureDate }, { it.travel.departureTime }))
        notifyDataSetChanged()
    }

    fun setUserRole(userRole: String) {
        this.userRole = userRole
        notifyDataSetChanged()
    }

    fun setUserId(userId: String) {
        this.userId = userId
        notifyDataSetChanged()
    }

    fun getTravelPrice(travelId: String): String? {
        return travelList.find { it.travel.id == travelId }?.travel?.price.toString()
    }
}