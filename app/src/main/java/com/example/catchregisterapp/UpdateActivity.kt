package com.example.catchregisterapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class UpdateActivity : AppCompatActivity() {

    private lateinit var lakeInput: EditText
    private lateinit var baitInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var rigInput: EditText
    private lateinit var rodInput: Spinner
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    private lateinit var id: String
    private var lake: String? = null
    private var bait: String? = null
    private var weight: String? = null
    private var rig: String? = null
    private var rod: String? = null

    // Osztályszintű rodOptions változó*
    private val rodOptions = arrayOf("Left rod", "Middle rod", "Right rod")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Státusz sáv színének beállítása
        window.statusBarColor = resources.getColor(R.color.white)

        // Toolbar beállítása
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        lakeInput = findViewById(R.id.lake_input2)
        baitInput = findViewById(R.id.bait_input2)
        weightInput = findViewById(R.id.weight_input2)
        rigInput = findViewById(R.id.rig_input2)
        rodInput = findViewById(R.id.rod_input2)
        updateButton = findViewById(R.id.update_button)
        deleteButton = findViewById(R.id.delete_button)

        // Spinner beállítása
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rodOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rodInput.adapter = adapter

        // Adatok beállítása az Intent-ből
        getAndSetIntentData()

        val ab: ActionBar? = supportActionBar
        ab?.title = lake

        // Update gomb eseménykezelő
        updateButton.setOnClickListener {
            val myDB = DataBaseController(this)
            lake = lakeInput.text.toString().trim()
            bait = baitInput.text.toString().trim()
            weight = weightInput.text.toString().trim()
            rig = rigInput.text.toString().trim()
            rod = rodInput.selectedItem.toString()
            myDB.updateData(id, lake, weight, bait, rig, rod)
            finish()
        }

        // Delete gomb eseménykezelő
        deleteButton.setOnClickListener {
            confirmDialog()
        }
    }

    private fun getAndSetIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("lake") && intent.hasExtra("bait") && intent.hasExtra("weight")
            && intent.hasExtra("rig") && intent.hasExtra("rod")) {

            id = intent.getStringExtra("id") ?: ""
            lake = intent.getStringExtra("lake")
            bait = intent.getStringExtra("bait")
            weight = intent.getStringExtra("weight")
            rig = intent.getStringExtra("rig")
            rod = intent.getStringExtra("rod")

            lakeInput.setText(lake)
            baitInput.setText(bait)
            weightInput.setText(weight)
            rigInput.setText(rig)

            val rodIndex = rodOptions.indexOf(rod)
            if (rodIndex >= 0) {
                rodInput.setSelection(rodIndex)
            }

            Log.d("UpdateActivity", "$lake, $bait, $weight, $rig, $rod")
        } else {
            Toast.makeText(this, "Nincs adat.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Törlés: $lake?")
        builder.setMessage("Biztosan törölni akarod a $lake fogást?")
        builder.setPositiveButton("Igen") { dialogInterface: DialogInterface, i: Int ->
            val myDB = DataBaseController(this)
            myDB.deleteOneRow(id)
            finish()
        }
        builder.setNegativeButton("Nem") { dialogInterface: DialogInterface, i: Int -> }
        builder.create().show()
    }
}
