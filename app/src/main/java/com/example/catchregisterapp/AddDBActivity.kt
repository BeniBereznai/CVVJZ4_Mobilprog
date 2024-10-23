package com.example.catchregisterapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ArrayAdapter
import android.widget.Spinner

class AddDBActivity : AppCompatActivity() {

    private lateinit var lakeInput: EditText
    private lateinit var baitInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var rigInput: EditText     // Új mező a rig számára
    private lateinit var rodInput: Spinner     // Új mező a rod számára
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dbactivity)

        // Státusz sáv színének beállítása
        window.statusBarColor = resources.getColor(R.color.white)

        // EditText és Button elemek inicializálása
        lakeInput = findViewById(R.id.lake_input)
        baitInput = findViewById(R.id.bait_input)
        weightInput = findViewById(R.id.weight_input)
        rigInput = findViewById(R.id.rig_input)

        // Spinner inicializálása
        rodInput = findViewById(R.id.rod_input)

        // Opciók a Spinnerhez
        val rodOptions = arrayOf("Left rod", "Middle rod", "Right rod")

        // Adapter létrehozása
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rodOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rodInput.adapter = adapter

        saveButton = findViewById(R.id.save_button)

        // Mentés gomb eseménykezelő
        saveButton.setOnClickListener {
            val lake = lakeInput.text.toString().trim()
            val bait = baitInput.text.toString().trim()
            val weight = weightInput.text.toString().trim().toIntOrNull()
            val rig = rigInput.text.toString().trim()
            val rod = rodInput.selectedItem.toString() // Spinner érték lekérése

            if (weight != null) {
                val myDB = DataBaseController(this@AddDBActivity)
                myDB.addCatch(lake, weight, bait, rig, rod)  // Az új mezők átadása
            } else {
                // Hibakezelés: ha a súly nem szám, adj visszajelzést a felhasználónak
                weightInput.error = "Please enter a valid weight"
            }
        }

        // Toolbar beállítása
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Vissza gomb engedélyezése
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        // Vissza gomb kattintási esemény
        toolbar.setNavigationOnClickListener {
            finish() // Visszatérés az előző aktivitásra
        }
    }
}
