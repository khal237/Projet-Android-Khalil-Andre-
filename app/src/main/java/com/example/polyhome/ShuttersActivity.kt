package com.example.polyhome
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShuttersActivity : AppCompatActivity() {
    private var token: String? = null
    private var houseId: Int = -1
    private val shuttersList = ArrayList<Device>()
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shutters)

        token = intent.getStringExtra("token")
        houseId = intent.getIntExtra("houseId", -1)

        val listView = findViewById<ListView>(R.id.listShutters)
        adapter = DeviceAdapter(this, shuttersList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val shutter = shuttersList[position]
            val newOpening = if (shutter.opening == 100) 0 else 100
            sendShutterCommand(shutter.id, newOpening)
        }
        loadShutters()
    }

    private fun loadShutters() {
        val api = Api()
        api.get<DevicesResponse>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices", { code, resp ->
            runOnUiThread {
                if (code == 200 && resp != null) {
                    shuttersList.clear()
                    shuttersList.addAll(resp.devices.filter { it.type == "rolling shutter" })
                    adapter.notifyDataSetChanged()
                }
            }
        }, token)
    }
    private fun sendShutterCommand(deviceId: String, opening: Int) {
        val api = Api()
        val body = mapOf("opening" to opening)
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"

        api.post<Map<String, Int>, Any>(url, body, { code, _ ->
            runOnUiThread {
                if (code == 200) {
                    Toast.makeText(this, "Volet réglé à $opening%", Toast.LENGTH_SHORT).show()
                    loadShutters()
                }
            }
        }, token)
    }
}