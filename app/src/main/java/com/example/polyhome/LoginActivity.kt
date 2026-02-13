package com.example.polyhome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        }


    public fun registerNewAccount(view: View)
    {
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }

    fun login(view: View) {
        val loginText = findViewById<EditText>(R.id.txtLogin).text.toString()
        val passwordText = findViewById<EditText>(R.id.txtPassword).text.toString()

        val data = LoginData(loginText, passwordText)
        val api = Api()

        api.post<LoginData, LoginResponse>(
            "https://polyhome.lesmoulinsdudev.com/api/users/auth",
        data,
        ::loginSuccess
        )
    }

    private fun loginSuccess(responseCode: Int, response: LoginResponse?) {
        runOnUiThread {
            if (responseCode == 200 && response != null) {
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()

                // Ici, nous devrons sauvegarder le token dans les SharedPreferences
                val intent = Intent(this, HousesActivity::class.java)
                intent.putExtra("token", response.token)
                startActivity(intent)
                finish()
            } else {
                val message = when(responseCode) {
                    404 -> "Utilisateur non trouvé"
                        400 -> "Données incorrectes"
                    else -> "Erreur serveur ($responseCode)"
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
    // Fonction pour sauvegarder le token dans les SharedPreferences et  "se souvenir" de l'utilisateur
    private fun saveToken(token: String) {
        val sharedPref = getSharedPreferences("PolyHomePrefs", MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("AUTH_TOKEN", token)
            apply()
        }
    }
}

