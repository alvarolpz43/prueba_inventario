package com.example.prueba.Inventory.Recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba.Inventory.Data.DataProducts
import com.example.prueba.databinding.ItemProductsBinding

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemProductsBinding.bind(view)
    fun bind(products: DataProducts, onItemselected: (String,String) -> Unit) {
        binding.nombreProduc.text = "${products.nombre}"
        binding.cantidadProduc.text = "Cantidad: ${products.cantidad}"
        binding.categoriaProduc.text = "Categoría: ${products.categoria}"

        binding.btnAgregar.setOnClickListener {
            onItemselected(products._id,binding.cantSoli.text.toString())
        }
    }
}