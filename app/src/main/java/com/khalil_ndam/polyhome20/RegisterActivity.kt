package com.khalil_ndam.polyhome20

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.khalil_ndam.polyhome20.Models.AuthRequest

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register) // Ton interface Register avec confirmation

        val btnFinalRegister = findViewById<MaterialButton>(R.id.btnFinalRegister)
        val api = Api()

        btnFinalRegister.setOnClickListener {
            val user = findViewById<TextInputEditText>(R.id.etRegisterLogin).text.toString()
            val pass = findViewById<TextInputEditText>(R.id.etRegisterPassword).text.toString()
            val confirm = findViewById<TextInputEditText>(R.id.etConfirmPassword).text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Champs vides", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirm) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registerData = AuthRequest(user, pass)

            api.post<AuthRequest>(
                "https://polyhome.lesmoulinsdudev.com/api/users/register",
                registerData,
                { code ->
                    runOnUiThread {
                        if (code == 200) {
                            Toast.makeText(this, "Compte créé ! Connectez-vous.", Toast.LENGTH_LONG).show()
                            finish() // Retour au Login
                        } else if (code == 409) {
                            Toast.makeText(this, "Cet utilisateur existe déjà", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Erreur serveur : $code", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}