package com.example.catchregisterapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val activity: Activity,
    private val context: Context,
    private val catch_id: ArrayList<String>,
    private val catch_lake: ArrayList<String>,
    private val catch_bait: ArrayList<String>,
    private val catch_weight: ArrayList<String>,
    private val catch_rig: ArrayList<String>,  // Új rig lista
    private val catch_rod: ArrayList<String>   // Új rod lista
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.idText.text = catch_id[position]
        holder.lakeText.text = catch_lake[position]
        holder.baitText.text = catch_bait[position]
        holder.weightText.text = catch_weight[position]
        holder.rigText.text = catch_rig[position]  // Rig megjelenítése
        holder.rodText.text = catch_rod[position]  // Rod megjelenítése

        // Ha sorra kattintasz, elindíthat egy új activity-t vagy műveletet
        holder.mainLayout.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", catch_id[position])
            intent.putExtra("lake", catch_lake[position])
            intent.putExtra("bait", catch_bait[position])
            intent.putExtra("weight", catch_weight[position])
            intent.putExtra("rig", catch_rig[position])  // Rig átadása
            intent.putExtra("rod", catch_rod[position])  // Rod átadása
            activity.startActivityForResult(intent, 1)
        }
    }

    override fun getItemCount(): Int {
        return catch_id.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idText: TextView = itemView.findViewById(R.id.id_text)
        val lakeText: TextView = itemView.findViewById(R.id.lake_text)
        val baitText: TextView = itemView.findViewById(R.id.bait_text)
        val weightText: TextView = itemView.findViewById(R.id.weight_text)
        val rigText: TextView = itemView.findViewById(R.id.rig_text)   // Új TextView a rig számára
        val rodText: TextView = itemView.findViewById(R.id.rod_text)   // Új TextView a rod számára
        val mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }
}
