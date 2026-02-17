package com.example.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class HouseAdapter(val context: Context, val houses: List<House>) : BaseAdapter() {

    override fun getCount(): Int = houses.size

    override fun getItem(position: Int): Any = houses[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val rowView = layoutInflater.inflate(R.layout.row_house, parent, false)

        val house = houses[position]

        val txtId = rowView.findViewById<TextView>(R.id.txtHouseId)
        val txtRole = rowView.findViewById<TextView>(R.id.txtRole)

        txtId.text = "Maison n°${house.houseId}"
        txtRole.text = if (house.owner) "Propriétaire" else "Accès partagé"

        return rowView
    }
}