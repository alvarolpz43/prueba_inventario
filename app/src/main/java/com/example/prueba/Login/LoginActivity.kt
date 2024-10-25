package com.example.prueba.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba.MainActivity
import com.example.prueba.Model.RetrofitClient
import com.example.prueba.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var useToken: String = "" // Inicializar token vacío
    private val BASE_URL = "https://backend-efagram.vercel.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si el usuario ya tiene una sesión activa
        if (checkSession()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            ManagerIU()
        }
    }

    private fun ManagerIU() {
        EventListeners()
    }


    private fun EventListeners() {

        val retrofit = RetrofitClient.web

        binding.loginButton.setOnClickListener {
            val username = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese sus credenciales", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = LoginRequest(username, password)

            val call = retrofit.login(user)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        useToken = response.body()?.token ?: ""

                        if (isvalidatetoken(useToken)) {
                            saveTokenToSharedPreferences(useToken)
                            Log.i("LoginActivity", "Login exitoso, token guardado.")

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error de autenticación: Token inválido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        when (response.code()) {
                            401 -> Toast.makeText(
                                applicationContext,
                                "El usuario se encuentra inactivo",
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                applicationContext,
                                "Credenciales incorrectas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Error en la red", t)
                }
            })
        }
    }

    private fun isvalidatetoken(token: String?): Boolean {
        return token != null && token.isNotEmpty()
    }

    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun checkSession(): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        return isvalidatetoken(token)
    }
}
