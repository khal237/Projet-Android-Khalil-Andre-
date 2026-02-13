package com.example.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HousesActivity : AppCompatActivity() {
    private var token: String? = null
    private val houses = ArrayList<House>()

    // Notre adaptateur (le serveur qui apporte les données à la liste)
    private lateinit var adapter: HouseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houses)

        // 1. On récupère le token du Login
        token = intent.getStringExtra("token")

        // 2. On lie la ListView du XML
        val listView = findViewById<ListView>(R.id.listHouses)

        // 3. On initialise l'adaptateur avec la liste vide au départ
        adapter = HouseAdapter(this, houses)
        listView.adapter = adapter


        // À ajouter dans le onCreate, juste après listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedHouse = houses[position]

            // On prépare le passage à l'écran suivant
            val intent = Intent(this, DevicesActivity::class.java)

            // On emporte les deux informations vitales :
            intent.putExtra("token", token)           // Pour rester connecté
            intent.putExtra("houseId", selectedHouse.houseId) // Pour savoir quelle maison ouvrir

            startActivity(intent)
        }

        // 4. On lance l'appel API pour remplir la liste
        loadHouses()
    }

    private fun loadHouses() {
        val api = Api()
        api.get<List<House>>(
            "https://polyhome.lesmoulinsdudev.com/api/houses",
            ::loadHousesSuccess,
            token // Le jeton de sécurité indispensable
        )
    }

    private fun loadHousesSuccess(responseCode: Int, loadedHouses: List<House>?) {
        runOnUiThread {
            if (responseCode == 200 && loadedHouses != null) {
                // On met à jour nos données
                houses.clear()
                houses.addAll(loadedHouses)

                // On force la ListView à se rafraîchir
                adapter.notifyDataSetChanged()
            } else {
                // Si ça ne marche pas, on affiche le code d'erreur (ex: 403)
                Toast.makeText(this, "Erreur $responseCode : Connexion impossible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}