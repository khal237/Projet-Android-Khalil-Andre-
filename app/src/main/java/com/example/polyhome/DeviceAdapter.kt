package com.example.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView // Ajoute cet import
import android.widget.TextView

class DeviceAdapter(val context: Context, val devices: List<Device>) : BaseAdapter() {
    override fun getCount() = devices.size
    override fun getItem(position: Int) = devices[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_device, parent, false)
        val device = devices[position]

        val name = view.findViewById<TextView>(R.id.txtDeviceName)
        val status = view.findViewById<TextView>(R.id.txtStatus)
        val icon = view.findViewById<ImageView>(R.id.imgDeviceIcon)

        when (device.type) {
            "light" -> {
                icon.setImageResource(R.drawable.ic_light)
                name.text = "Lumière"
            }
            "rolling shutter" -> {
                icon.setImageResource(R.drawable.ic_shutter)
                name.text = "Volet"
            }
            else -> {
                name.text = device.type
            }
        }

        status.text = when {
            device.opening != null -> "${device.opening}%"
            device.power != null -> if(device.power == 1) "ON" else "OFF"
            else -> ""
        }

        return view
    }
}