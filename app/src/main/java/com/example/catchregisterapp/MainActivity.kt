package com.example.catchregisterapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var myDB: DataBaseController
    private lateinit var catch_id: ArrayList<String>
    private lateinit var catch_lake: ArrayList<String>
    private lateinit var catch_bait: ArrayList<String>
    private lateinit var catch_weight: ArrayList<String>
    private lateinit var catch_rig: ArrayList<String>  // Új rig lista
    private lateinit var catch_rod: ArrayList<String>  // Új rod lista
    private lateinit var customAdapter: CustomAdapter
    private lateinit var noDataTextView: TextView  // TextView hozzáadása

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Toolbar beállítása
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // RecyclerView, FloatingActionButton és TextView inicializálása
        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)
        noDataTextView = findViewById(R.id.noDataTextView)  // TextView inicializálása

        // FloatingActionButton kattintás esemény
        addButton.setOnClickListener {
            val intent = Intent(this, AddDBActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // Adatbázis és listák inicializálása
        myDB = DataBaseController(this)
        catch_id = ArrayList()
        catch_lake = ArrayList()
        catch_bait = ArrayList()
        catch_weight = ArrayList()
        catch_rig = ArrayList()  // Rig lista inicializálása
        catch_rod = ArrayList()  // Rod lista inicializálása

        // Adatok betöltése az ArrayList-ekbe
        storeDataInArrays()

        // Adapter beállítása a RecyclerView-hoz
        customAdapter = CustomAdapter(this, this, catch_id, catch_lake, catch_bait, catch_weight, catch_rig, catch_rod)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ha nincs adat, jelenjen meg a "No data" üzenet
        updateUI() // UI frissítése az adatok állapotának megfelelően

        // Ablakszegélyek beállítása
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // UI frissítése a fogások jelenlétének megfelelően
    private fun updateUI() {
        if (catch_id.isEmpty()) {
            noDataTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            findViewById<View>(R.id.backgroundView).setBackgroundColor(getResources().getColor(android.R.color.white)) // Háttér szín változtatása
        } else {
            noDataTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            findViewById<View>(R.id.backgroundView).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)) // Alap háttérszín
        }
    }

    // Adatok betöltése az ArrayList-ekbe
    private fun storeDataInArrays() {
        val cursor = myDB.readAllData()
        if (cursor.count == 0) {
            // Nincs adat
        } else {
            while (cursor.moveToNext()) {
                catch_id.add(cursor.getString(0))
                catch_lake.add(cursor.getString(1))
                catch_bait.add(cursor.getString(2))
                catch_weight.add(cursor.getString(3))
                catch_rig.add(cursor.getString(4))  // Rig adat hozzáadása
                catch_rod.add(cursor.getString(5))  // Rod adat hozzáadása
            }
        }
    }

    // onActivityResult hogy frissítse az adatokat visszatéréskor
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // Frissíteni kell a listát és az adaptert
            catch_id.clear()
            catch_lake.clear()
            catch_bait.clear()
            catch_weight.clear()
            catch_rig.clear()  // Rig lista törlése
            catch_rod.clear()  // Rod lista törlése
            storeDataInArrays()  // Adatok újratöltése
            customAdapter.notifyDataSetChanged()  // Adapter frissítése

            // UI frissítése az adatok állapotának megfelelően
            updateUI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.catch_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            confirmDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun confirmDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to delete all Data?")
        builder.setPositiveButton("Yes") { _, _ ->
            val myDB: DataBaseController = DataBaseController(this@MainActivity)
            myDB.deleteAllData()
            // Refresh Activity
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No", null)
        builder.create().show()
    }
}
