package com.example.polyhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    // Lié au bouton "Créer mon compte" (onClick: register)
    public fun register(view: View) {
        val loginText = findViewById<EditText>(R.id.txtRegisterLogin).text.toString()
        val passwordText = findViewById<EditText>(R.id.txtRegisterPassword).text.toString()

        // Vérification simple pour l'ergonomie
        if (loginText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val data = LoginData(loginText, passwordText)
        val api = Api()

        api.post<LoginData>(
            "https://polyhome.lesmoulinsdudev.com/api/users/register",
            data,
            ::registerSuccess
        )
    }

    // Callback après l'appel API
    private fun registerSuccess(responseCode: Int) {
        runOnUiThread {
            when (responseCode) {
                200 -> {
                    Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show()
                    finish() // Retourne à l'écran de connexion
                }
                409 -> {
                    Toast.makeText(this, "Cet identifiant est déjà utilisé", Toast.LENGTH_LONG).show()
                }
                400 -> {
                    Toast.makeText(this, "Données invalides", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Erreur serveur ($responseCode)", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    public fun goToLogin(view: View) {
        finish()
    }
}