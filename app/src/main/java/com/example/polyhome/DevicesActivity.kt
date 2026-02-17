package com.example.polyhome
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class DevicesActivity : AppCompatActivity() {
    private var token: String? = null
    private var houseId: Int = -1
    private val devicesList = ArrayList<Device>()
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        token = intent.getStringExtra("token")
        houseId = intent.getIntExtra("houseId", -1)

        findViewById<LinearLayout>(R.id.navLumieres).setOnClickListener {
            val intent = Intent(this, LightsActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.navVolets).setOnClickListener {
            val intent = Intent(this, ShuttersActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOffAllLights).setOnClickListener {
            var count = 0
            for (device in devicesList) {
                if (device.type.equals("light", ignoreCase = true)) {
                    sendCommand(device.id, "TURN OFF")
                    count++
                }
            }
            if (count > 0) {
                Toast.makeText(this, "Extinction de $count lumière(s)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Aucune lumière trouvée", Toast.LENGTH_SHORT).show()
            }
        }

        // Ouvrir tous les volets
        findViewById<Button>(R.id.btnOpenAllShutters).setOnClickListener {
            for (device in devicesList) {
                if (device.type.contains("shutter", ignoreCase = true)) {
                    // Selon ton API, on envoie "OPEN" ou un opening à 100
                    sendCommand(device.id, "OPEN")
                }
            }
        }

    // Fermer tous les volets
        findViewById<Button>(R.id.btnCloseAllShutters).setOnClickListener {
            for (device in devicesList) {
                if (device.type.contains("shutter", ignoreCase = true)) {
                    sendCommand(device.id, "CLOSE")
                }
            }
        }

        // Ouvrir la porte de garage
        findViewById<Button>(R.id.btnOpenGarage).setOnClickListener {
            for (device in devicesList) {
                // On vérifie si le type contient "garage" ou "door" (à adapter selon ton API)
                if (device.type.contains("garage", ignoreCase = true) || device.type.contains("door", ignoreCase = true)) {
                    sendCommand(device.id, "OPEN")
                }
            }
            Toast.makeText(this, "Ouverture du garage...", Toast.LENGTH_SHORT).show()
        }

// Fermer la porte de garage
        findViewById<Button>(R.id.btnCloseGarage).setOnClickListener {
            for (device in devicesList) {
                if (device.type.contains("garage", ignoreCase = true) || device.type.contains("door", ignoreCase = true)) {
                    sendCommand(device.id, "CLOSE")
                }
            }
            Toast.makeText(this, "Fermeture du garage...", Toast.LENGTH_SHORT).show()
        }
        //val listView = findViewById<ListView>(R.id.listDevices)
        adapter = DeviceAdapter(this, devicesList)
        //listView.adapter = adapter

        //listView.setOnItemClickListener { _, _, position, _ ->
          //  val device = devicesList[position]

            // Logique intelligente pour les volets (rolling_shutter)
            //val commandToSend = if (device.type == "rolling_shutter") {
                // Si l'ouverture est à 0 (fermé), on envoie OPEN, sinon on envoie CLOSE
              //  if (device.opening == 0) "OPEN" else "CLOSE"
           // } else if (device.type == "light") {
                // Pareil pour la lumière : si power est 0 (éteint), on envoie TURN ON
                //if (device.power == 0) "TURN ON" else "TURN OFF"
            //} else {
                "OPEN" // Valeur par défaut pour le garage ou autre
            //}

            //sendCommand(device.id, commandToSend)
        //}
        loadDevices()
    }

    private fun loadDevices() {
        val api = Api()
        api.get<DevicesResponse>(
            "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices",
            ::loadDevicesSuccess,
            token
        )
    }

    private fun loadDevicesSuccess(responseCode: Int, response: DevicesResponse?) {
        runOnUiThread {
            if (responseCode == 200 && response != null) {
                devicesList.clear()
                devicesList.addAll(response.devices)
                adapter.notifyDataSetChanged()
            }
        }
    }
    private fun sendCommand(deviceId: String, command: String) {
        val api = Api()

        // L'API attend un JSON simple : {"command": "OPEN"}
        val body = mapOf("command" to command)

        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"

        api.post<Map<String, String>, Any>(
            url,
            body,
            { responseCode, _ ->
                runOnUiThread {
                    if (responseCode == 200) {
                        Toast.makeText(this, "Commande envoyée : $command", Toast.LENGTH_SHORT).show()

                        loadDevices()
                    } else {
                        Toast.makeText(this, "Erreur lors de l'envoi", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            token
        )
    }
}