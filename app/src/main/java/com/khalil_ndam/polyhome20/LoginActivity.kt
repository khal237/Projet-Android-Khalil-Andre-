package com.khalil_ndam.polyhome20

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.khalil_ndam.polyhome20.Models.AuthRequest
import com.khalil_ndam.polyhome20.Models.AuthResponse

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val etLogin = findViewById<TextInputEditText>(R.id.etLogin)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)

        val api = Api()

        btnLogin.setOnClickListener {
            val user = etLogin.text.toString()
            val pass = etPassword.text.toString()
            val authData = AuthRequest(user, pass)

            // ATTENTION : On définit "codeHttp" et "donneesRecues" ici
            api.post<AuthRequest, AuthResponse>(
                "https://polyhome.lesmoulinsdudev.com/api/users/auth",
                authData,
                { codeHttp, donneesRecues ->
                    // On utilise runOnUiThread car Api.kt utilise Dispatchers.IO
                    runOnUiThread {
                        if (codeHttp == 200 && donneesRecues != null) {
                            // On extrait le token de l'objet donneesRecues
                            val monToken = donneesRecues.token

                            // Sauvegarde pour le barème
                            val sharedPref = getSharedPreferences("PolyhomePrefs", Context.MODE_PRIVATE)
                            sharedPref.edit().putString("AUTH_TOKEN", monToken).apply()

                            // Passage à l'écran suivant
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Erreur : $codeHttp", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}