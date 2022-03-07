package com.example.examensegundobimestre

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SoldiersList : AppCompatActivity() {
    private var soldiers: ArrayList<SoldierDTO> = arrayListOf()
    private var idSelectedSoldier = -1

    private val soldierInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val soldierModified = it.data?.getParcelableExtra<SoldierDTO>("soldierModified")
                val isNew = it.data?.getBooleanExtra("isNewModified", true)

                val adapter = getAdapterSoldierListView()

                if (isNew!!) addSoldier(adapter, soldierModified!!)
                else modifySoldier(adapter, soldierModified!!)
            }
        }
        if (it.resultCode == Activity.RESULT_CANCELED) {
            Log.i("soldier-info", "Canceled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soldiers_list)

        val general = intent.getParcelableExtra<GeneralDTO>("general")
        soldiers = general?.soldiers!!

        val txtGeneral = findViewById<TextView>(R.id.txt_son)
        txtGeneral.text = general?.name + "'s Soldiers"

        val soldierListView = findViewById<ListView>(R.id.lv_soldiers)
        val adapter = ArrayAdapter(
            this,
            R.layout.text_list_view,
            R.id.txt_list_view,
            soldiers
        )

        soldierListView.adapter = adapter
        adapter.notifyDataSetChanged()
        registerForContextMenu(soldierListView)

        val buttonNewSoldier = findViewById<Button>(R.id.button_new_soldier)
        buttonNewSoldier.setOnClickListener {
            openSoldierInfoActivity(SoldierInfo::class.java, true, null)
        }

        val returnButton = findViewById<Button>(R.id.button_return)
        returnButton.setOnClickListener {
            returnAction()
        }
    }

    private fun getAdapterSoldierListView(): ArrayAdapter<SoldierDTO> {
        val soldierListView = findViewById<ListView>(R.id.lv_soldiers)
        val adapter = ArrayAdapter(
            this,
            R.layout.text_list_view,
            R.id.txt_list_view,
            soldiers
        )
        soldierListView.adapter = adapter
        return adapter
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.soldier_menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position

        idSelectedSoldier = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_soldier -> {
                openSoldierInfoActivity(
                    SoldierInfo::class.java,
                    false,
                    soldiers[idSelectedSoldier]
                )
                return true
            }
            R.id.delete_soldier -> {
                confirmDelete()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun openSoldierInfoActivity(
        activityClass: Class<*>,
        isNew: Boolean,
        selectedSoldier: SoldierDTO?
    ) {
        val soldierIntent = Intent(this, activityClass)

        if (!isNew) {
            soldierIntent.putExtra("soldier", selectedSoldier)
        }
        soldierIntent.putExtra("new", isNew)

        soldierInfoLauncher.launch(soldierIntent)
    }

    private fun addSoldier(
        adapter: ArrayAdapter<SoldierDTO>,
        newSoldier: SoldierDTO
    ) {
        val db = Firebase.firestore
        val soldiersRef = db.collection("soldiers")

        val soldier = hashMapOf(
            "name" to newSoldier.name,
            "birthDate" to com.google.firebase.Timestamp(newSoldier.birthDate!!),
            "mainWeapon" to newSoldier.mainWeapon,
            "active" to newSoldier.active,
            "yearExperience" to newSoldier.yearExperience,
            "salary" to newSoldier.salary,
        )

        soldiersRef.add(soldier)
            .addOnSuccessListener { document ->
                newSoldier.id = document.id
                soldiers.add(newSoldier)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.i("soldiers-firebase", "Failure: ${it.toString()}")
            }
    }

    private fun modifySoldier(
        adapter: ArrayAdapter<SoldierDTO>,
        soldierModified: SoldierDTO
    ) {
        val db = Firebase.firestore
        val soldiersRef = db.collection("soldiers")

        val soldierToUpdate = hashMapOf(
            "name" to soldierModified.name,
            "birthDate" to com.google.firebase.Timestamp(soldierModified.birthDate!!),
            "mainWeapon" to soldierModified.mainWeapon,
            "active" to soldierModified.active,
            "yearExperience" to soldierModified.yearExperience,
            "salary" to soldierModified.salary,
        )

        soldiersRef.document(soldierModified.id!!)
            .set(soldierToUpdate)
            .addOnSuccessListener {
                soldiers[idSelectedSoldier] = soldierModified
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.i("soldiers-firebase", "Failure: ${it.toString()}")
            }

    }

    private fun deleteSoldier(
        adapter: ArrayAdapter<SoldierDTO>,
    ) {
        val db = Firebase.firestore
        val soldiersRef = db.collection("soldiers")

        soldiersRef.document(soldiers[idSelectedSoldier].id!!)
            .delete()
            .addOnSuccessListener {
                soldiers.removeAt(idSelectedSoldier)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.i("soldiers-firebase", "Failure: ${it.toString()}")
            }
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Soldier")
        builder.setMessage(R.string.confirm_delete)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            deleteSoldier(getAdapterSoldierListView())
        }

        builder.setNegativeButton("No", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun returnAction() {
        val returnIntent = Intent()

        val general = intent.getParcelableExtra<GeneralDTO>("general")!!
        general.soldiers = soldiers

        returnIntent.putExtra("generalModified", general)

        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }
}