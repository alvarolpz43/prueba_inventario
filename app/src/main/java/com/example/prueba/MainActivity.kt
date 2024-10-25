package com.example.prueba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prueba.Inventoryhhgdfjgf.ProductsActivity
import com.example.prueba.Model.AppEfa
import com.example.prueba.Model.RetrofitClient
import com.example.prueba.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val retrofit = RetrofitClient.web
    val token = AppEfa.preferences.getToken()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "Token: $token")
        ManagerIU()
        binding.CarEpp.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun ManagerIU() {
        lifecycleScope.launch(Dispatchers.IO){
            DatosUser()
        }
    }

    private suspend fun DatosUser() {
        try {
            Log.d("MainActivity", "Token con Bearer: Bearer $token")

            val response = retrofit.getprofileUser("Bearer $token")

            Log.d("MainActivity", "Response token: ${response.body()}")

            if (response.isSuccessful) {
                val myResponse = response.body()

                if (myResponse != null && myResponse.name != null) {
                    withContext(Dispatchers.Main) {
                        binding.textViewLog1.text = "Bienvenido ${myResponse.name}"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.textViewLog1.text = "Datos no encontrados"
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.textViewLog1.text = "Error: ${response.code()}"
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                binding.textViewLog1.text = "Error: ${e.message}"
            }
        }
    }

}
