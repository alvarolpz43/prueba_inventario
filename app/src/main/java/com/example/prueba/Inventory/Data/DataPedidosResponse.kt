package com.example.prueba.Inventory.Data

data class DataPedidosResponse(
    val pedidos: List<DataPedidos>
)

data class DataPedidos(
    val _id: String,
    val usuario: String,
    val productosArray: List<DataReuqestProduct>,
    val createdAt: String,
    val estado: String,
    val precioTotal: Int
)

data class DataReuqestProduct(
    val producto: String,
    val cantidad: Int
)
data class PedidoRequest(
    val productosArray: List<DataReuqestProduct>
)