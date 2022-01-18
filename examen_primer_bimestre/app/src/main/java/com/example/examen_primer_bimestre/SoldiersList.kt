package com.example.examen_primer_bimestre

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

class SoldiersList : AppCompatActivity() {
    private var soldiers: ArrayList<Soldier> = arrayListOf()
    private var idSelectedSoldier = -1

    private val soldierInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val soldierModified = it.data?.getParcelableExtra<Soldier>("soldierModified")
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

        val general = intent.getParcelableExtra<General>("general")
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

    private fun getAdapterSoldierListView(): ArrayAdapter<Soldier> {
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
        selectedSoldier: Soldier?
    ) {
        val soldierIntent = Intent(this, activityClass)

        if (!isNew) {
            soldierIntent.putExtra("soldier", selectedSoldier)
        }
        soldierIntent.putExtra("new", isNew)

        soldierInfoLauncher.launch(soldierIntent)
    }

    private fun addSoldier(
        adapter: ArrayAdapter<Soldier>,
        newSoldier: Soldier
    ) {
        soldiers.add(newSoldier)
        adapter.notifyDataSetChanged()
    }

    private fun modifySoldier(
        adapter: ArrayAdapter<Soldier>,
        soldierModified: Soldier
    ) {
        soldiers[idSelectedSoldier] = soldierModified
        adapter.notifyDataSetChanged()
    }

    private fun deleteSoldier(
        adapter: ArrayAdapter<Soldier>,
    ) {
        soldiers.removeAt(idSelectedSoldier)
        adapter.notifyDataSetChanged()
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

        val general = intent.getParcelableExtra<General>("general")!!
        general.soldiers = soldiers

        returnIntent.putExtra("generalModified", general)

        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }
}