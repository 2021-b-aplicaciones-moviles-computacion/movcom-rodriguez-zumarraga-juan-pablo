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

class MainActivity : AppCompatActivity() {
    private var idSelectedGeneral = -1

    private val generalInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val generalModified = it.data?.getParcelableExtra<General>("generalModified")
                val isNew = it.data?.getBooleanExtra("isNewModified", true)

                val adapter = getAdapterGeneralListView()

                if (isNew!!) addGeneral(adapter, BDMemory.generals, generalModified!!)
                else modifyGeneral(adapter, BDMemory.generals, generalModified!!)
            }
        }
        if (it.resultCode == Activity.RESULT_CANCELED) {
            Log.i("general-info", "Canceled")
        }
    }

    private val soldiersListLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_CANCELED) {
            if (it.data != null) {
                val generalModified = it.data?.getParcelableExtra<General>("generalModified")
                val adapter = getAdapterGeneralListView()
                modifyGeneral(adapter, BDMemory.generals, generalModified!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtWelcome = findViewById<TextView>(R.id.txt_welcome)
        val stringWelcomeResource = resources.getStringArray(R.array.app_info)

        val stringWelcome =
            stringWelcomeResource[0] + '\n' + stringWelcomeResource[1] + '\n' + stringWelcomeResource[2]
        txtWelcome.text = stringWelcome

        val generalListView = findViewById<ListView>(R.id.lv_general)
        val adapter = ArrayAdapter(
            this,
            R.layout.text_list_view,
            R.id.txt_list_view,
            BDMemory.generals
        )

        generalListView.adapter = adapter
        adapter.notifyDataSetChanged()

        registerForContextMenu(generalListView)

        val buttonNewGeneral = findViewById<Button>(R.id.button_new_general)
        buttonNewGeneral.setOnClickListener {
            openGeneralInfoActivity(GeneralInfo::class.java, true, null)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.general_menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position

        idSelectedGeneral = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_general -> {
                openGeneralInfoActivity(
                    GeneralInfo::class.java,
                    false,
                    BDMemory.generals[idSelectedGeneral]
                )
                return true
            }
            R.id.delete_general -> {
                confirmDelete()
                return true
            }
            R.id.view_soldiers -> {
                openSoldierListActivity(
                    SoldiersList::class.java,
                    BDMemory.generals[idSelectedGeneral]
                )
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun getAdapterGeneralListView(): ArrayAdapter<General> {
        val generalListView = findViewById<ListView>(R.id.lv_general)
        val adapter = ArrayAdapter(
            this,
            R.layout.text_list_view,
            R.id.txt_list_view,
            BDMemory.generals
        )
        generalListView.adapter = adapter
        return adapter
    }

    private fun openGeneralInfoActivity(
        activityClass: Class<*>,
        isNew: Boolean,
        selectedGeneral: General?
    ) {
        val generalIntent = Intent(this, activityClass)

        if (!isNew) {
            generalIntent.putExtra("general", selectedGeneral)
        }
        generalIntent.putExtra("new", isNew)

        generalInfoLauncher.launch(generalIntent)
    }

    private fun addGeneral(
        adapter: ArrayAdapter<General>,
        generals: ArrayList<General>,
        newGeneral: General
    ) {
        generals.add(newGeneral)
        adapter.notifyDataSetChanged()
    }

    private fun modifyGeneral(
        adapter: ArrayAdapter<General>,
        generals: ArrayList<General>,
        generalModified: General
    ) {
        generals[idSelectedGeneral] = generalModified
        adapter.notifyDataSetChanged()
    }

    private fun deleteGeneral(
        adapter: ArrayAdapter<General>,
        generals: ArrayList<General>,
    ) {
        generals.removeAt(idSelectedGeneral)
        adapter.notifyDataSetChanged()
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete General")
        builder.setMessage(R.string.confirm_delete)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            deleteGeneral(getAdapterGeneralListView(), BDMemory.generals)
        }

        builder.setNegativeButton("No", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun openSoldierListActivity(
        activityClass: Class<*>,
        selectedGeneral: General?
    ) {
        val soldiersIntent = Intent(this, activityClass)
        soldiersIntent.putExtra("general", selectedGeneral)
        soldiersListLauncher.launch(soldiersIntent)
    }
}