package com.sgaikar.finboxassignment.ui.locations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.databinding.ItemLocationBinding

class LocationsAdapter(private val listener: LocationItemListener) : RecyclerView.Adapter<LocationViewHolder>() {

    interface LocationItemListener {
        fun onClickedLocation(location: LocationObj)
    }

    private val items = ArrayList<LocationObj>()

    fun setItems(items: ArrayList<LocationObj>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding: ItemLocationBinding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) = holder.bind(items[position])
}

class LocationViewHolder(private val itemBinding: ItemLocationBinding, private val listener: LocationsAdapter.LocationItemListener) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var location: LocationObj

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: LocationObj) {
        this.location = item
        itemBinding.name.text = item.timeStamp.toString()
        itemBinding.speciesAndStatus.text = "${item.latitude} - ${item.longitude}"
    }

    override fun onClick(v: View?) {
        listener.onClickedLocation(location)
    }
}

