package com.example.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class HouseAdapter(val context: Context, val houses: List<House>) : BaseAdapter() {

    // On dit à la liste combien d'éléments il y a
    override fun getCount(): Int = houses.size

    override fun getItem(position: Int): Any = houses[position]

    override fun getItemId(position: Int): Long = position.toLong()

    // C'est ici qu'on "dessine" chaque ligne (comme pour les pizzas)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val rowView = layoutInflater.inflate(R.layout.row_house, parent, false)

        val house = houses[position]

        // On fait le lien avec les TextView du fichier row_house.xml
        val txtId = rowView.findViewById<TextView>(R.id.txtHouseId)
        val txtRole = rowView.findViewById<TextView>(R.id.txtRole)

        // On remplit avec les vraies données
        txtId.text = "Maison n°${house.houseId}"
        txtRole.text = if (house.owner) "Propriétaire" else "Accès partagé"

        return rowView
    }
}