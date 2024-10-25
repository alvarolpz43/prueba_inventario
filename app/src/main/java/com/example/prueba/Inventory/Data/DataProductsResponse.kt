package com.example.prueba.Inventory.Data

data class DataProductsResponse(
    val products: List<DataProducts>
)

data class DataProducts(
    val _id: String,
    val nombre: String,
    val cantidad: String,
    val precio: String,
    val categoria: String,
    val familia: String
)