package com.example.prueba.Inventory.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba.Inventory.Data.DataProducts
import com.example.prueba.R

class Adapter(
    var listproducts: List<DataProducts> = emptyList(),
    private val onItemselected: (String,String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    fun updatelist(list: List<DataProducts>) {
        listproducts = list ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listproducts[position],onItemselected)
    }



    override fun getItemCount() = listproducts.size
}