package com.example.polyhome
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class LightsActivity : AppCompatActivity() {
    private var token: String? = null
    private var houseId: Int = -1
    private val lightsList = ArrayList<Device>()
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lights)

        token = intent.getStringExtra("token")
        houseId = intent.getIntExtra("houseId", -1)

        val listView = findViewById<ListView>(R.id.listLights)
        adapter = DeviceAdapter(this, lightsList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val light = lightsList[position]
            val newCommand = if (light.power == 1) "TURN OFF" else "TURN ON"
            sendCommand(light.id, newCommand)
        }
        loadLights()
    }

    private fun loadLights() {
        val api = Api()
        api.get<DevicesResponse>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices", { code, resp ->
            runOnUiThread {
                if (code == 200 && resp != null) {
                    lightsList.clear()
                    // FILTRE : On ne garde que les "light"
                    lightsList.addAll(resp.devices.filter { it.type == "light" })
                    adapter.notifyDataSetChanged()
                }
            }
        }, token)
    }
    private fun sendCommand(deviceId: String, command: String) {
        val api = Api()

        val body = mapOf("command" to command)
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"

        api.post<Map<String, String>, Any>(
            url,
            body,
            { responseCode, _ ->
                runOnUiThread {
                    if (responseCode == 200) {

                        Toast.makeText(this, "Action : $command", Toast.LENGTH_SHORT).show()
                        loadLights()
                    } else {
                        Toast.makeText(this, "Erreur lors de l'envoi ($responseCode)", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            token
        )
    }

}