package com.example.prueba.Inventoryhhgdfjgf

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba.Inventory.Data.DataProductsResponse
import com.example.prueba.Inventory.Data.DataReuqestProduct
import com.example.prueba.Inventory.Data.PedidoRequest
import com.example.prueba.Inventory.Recycler.Adapter
import com.example.prueba.Model.AppEfa
import com.example.prueba.Model.RetrofitClient
import com.example.prueba.R
import com.example.prueba.databinding.ActivityProductsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var adapter: Adapter
    // Lista que contiene los productos del carrito
    private val carrito = mutableListOf<DataReuqestProduct>()
    val token = AppEfa.preferences.getToken()
    val retrofit = RetrofitClient.web

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managerUI()
        Allproducts()
        // Vincular el botón en onCreate
//        binding.btnEnviarCarrito.setOnClickListener {
//            enviarCarrito() // Llama a la función que enviará el carrito completo
//        }

    }

    private fun managerUI() {
        adapter = Adapter { selectproducts, cantidad ->
            Log.d("MainActivity", "Producto seleccionado: ${selectproducts}")

            val idProduct = selectproducts
            val cantidadProduct = cantidad.toInt()
            sendPedidos(idProduct, cantidadProduct)


        }
        binding.recyclerproducts.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@ProductsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@ProductsActivity.adapter
        }
    }

    private fun agregarAlCarrito(idProduct: String, cantidadProduct: Int) {
        val productoEnCarrito = DataReuqestProduct(
            producto = idProduct,
            cantidad = cantidadProduct
        )

        carrito.add(productoEnCarrito)

        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
    }



    private fun enviarCarrito() {
        if (carrito.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Crear el cuerpo del request con la lista de productos en el carrito
                    val pedidoRequest = PedidoRequest(carrito)

                    Log.d("POST", "Cuerpo de la solicitud: $pedidoRequest")

                    // Hacer la llamada POST enviando el token y el cuerpo del carrito
                    val response = retrofit.postPedidos("Bearer $token", pedidoRequest)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ProductsActivity, "Pedido enviado", Toast.LENGTH_SHORT).show()
                            carrito.clear() // Limpiar el carrito después de enviar el pedido
                        } else {
                            Toast.makeText(this@ProductsActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ProductsActivity, "Error al enviar el carrito", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendPedidos(idProduct: String, cantidadProduct: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Crear el producto que se va a enviar en el pedido
                val productosArray = listOf(
                    DataReuqestProduct(
                        producto = idProduct,
                        cantidad = cantidadProduct
                    )
                )

                // Crear el cuerpo del request con la estructura PedidoRequest
                val pedidoRequest = PedidoRequest(productosArray)

                Log.d("POST", "Cuerpo de la solicitud: $pedidoRequest")

                // Hacer la llamada POST enviando el token y el cuerpo de la solicitud
                val response = retrofit.postPedidos("Bearer $token", pedidoRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProductsActivity, "Pedido enviado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ProductsActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductsActivity, "Error al enviar pedido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun Allproducts() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val myResponse: Response<DataProductsResponse> = retrofit.getAllProducts()
                Log.d("MainActivity", "Response Products: ${myResponse.body()}")
                if (myResponse.isSuccessful) {
                    val response: DataProductsResponse? = myResponse.body()
                    if (response != null) {
                        val listaproducts = response.products.reversed()
                        withContext(Dispatchers.Main) {
                            adapter.updatelist(listaproducts)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Tag", "Error")
            }
        }
    }
}