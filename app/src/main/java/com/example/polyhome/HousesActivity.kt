package com.example.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HousesActivity : AppCompatActivity() {
    private var token: String? = null
    private val houses = ArrayList<House>()


    private lateinit var adapter: HouseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houses)

        token = intent.getStringExtra("token")

        val listView = findViewById<ListView>(R.id.listHouses)


        adapter = HouseAdapter(this, houses)
        listView.adapter = adapter



        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedHouse = houses[position]


            val intent = Intent(this, DevicesActivity::class.java)


            intent.putExtra("token", token)
            intent.putExtra("houseId", selectedHouse.houseId)

            startActivity(intent)
        }


        loadHouses()
    }

    private fun loadHouses() {
        val api = Api()
        api.get<List<House>>(
            "https://polyhome.lesmoulinsdudev.com/api/houses",
            ::loadHousesSuccess,
            token
        )
    }

    private fun loadHousesSuccess(responseCode: Int, loadedHouses: List<House>?) {
        runOnUiThread {
            if (responseCode == 200 && loadedHouses != null) {
                houses.clear()
                houses.addAll(loadedHouses)

                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Erreur $responseCode : Connexion impossible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}