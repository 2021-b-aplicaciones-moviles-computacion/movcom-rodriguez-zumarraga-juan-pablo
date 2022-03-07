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

class MainActivity : AppCompatActivity() {
    private var idSelectedGeneral = -1
    private var generals = arrayListOf<GeneralDTO>()

    private val generalInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val generalModified = it.data?.getParcelableExtra<GeneralDTO>("generalModified")
                val isNew = it.data?.getBooleanExtra("isNewModified", true)

                val adapter = getAdapterGeneralListView()

                if (isNew!!) addGeneral(generalModified!!)
                else modifyGeneral(generalModified!!)
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
                val generalModified = it.data?.getParcelableExtra<GeneralDTO>("generalModified")
                val adapter = getAdapterGeneralListView()
                modifyGeneral(generalModified!!)
            }
        }
    }

    private fun readGenerals() {
        val db = Firebase.firestore
        val generalRef = db.collection("generals")
            .orderBy("name")
            .limit(10)
        generals = arrayListOf<GeneralDTO>()
        generalRef.get()
            .addOnSuccessListener {
                for (rawGeneral in it) {
                    val generalData = rawGeneral.data
                    Log.i("generals-firebase", "$generalData")

                    var timestamp = generalData["birthDate"] as com.google.firebase.Timestamp
                    val generalBirthDate = timestamp.toDate()
                    val rawSoldiers =
                        generalData["soldiers"] as List<com.google.firebase.firestore.DocumentReference>
                    val soldiers = arrayListOf<SoldierDTO>()
                    for (reference in rawSoldiers) {
                        reference.get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val soldierData = task.result?.data
                                    Log.i("generals-firebase", "$soldierData")

                                    timestamp =
                                        soldierData?.get("birthDate") as com.google.firebase.Timestamp
                                    val soldierBirthDate = timestamp.toDate()

                                    soldiers.add(
                                        SoldierDTO(
                                            task.result?.id,
                                            soldierData["name"] as String?,
                                            soldierBirthDate,
                                            soldierData["mainWeapon"] as String?,
                                            soldierData["active"] as Boolean?,
                                            soldierData["yearExperience"] as Long?,
                                            soldierData["salary"] as Double?
                                        )
                                    )
                                } else {
                                    Log.i("generals-firebase", "${task.exception}")
                                }
                            }
                    }

                    generals.add(
                        GeneralDTO(
                            rawGeneral.id,
                            generalData["name"] as String?,
                            generalBirthDate,
                            generalData["medals"] as Long?,
                            generalData["active"] as Boolean?,
                            generalData["yearExperience"] as Long?,
                            generalData["salary"] as Double?,
                            soldiers
                        )
                    )
                }

                val adapter = this.getAdapterGeneralListView()
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.i("generals-firebase", it.toString())
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

        readGenerals()

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
                    generals[idSelectedGeneral]
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
                    generals[idSelectedGeneral]
                )
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun getAdapterGeneralListView(): ArrayAdapter<GeneralDTO> {
        val generalListView = findViewById<ListView>(R.id.lv_general)
        val adapter = ArrayAdapter(
            this,
            R.layout.text_list_view,
            R.id.txt_list_view,
            generals
        )
        generalListView.adapter = adapter
        registerForContextMenu(generalListView)
        return adapter
    }

    private fun openGeneralInfoActivity(
        activityClass: Class<*>,
        isNew: Boolean,
        selectedGeneral: GeneralDTO?
    ) {
        val generalIntent = Intent(this, activityClass)

        if (!isNew) {
            generalIntent.putExtra("general", selectedGeneral)
        }
        generalIntent.putExtra("new", isNew)

        generalInfoLauncher.launch(generalIntent)
    }

    private fun addGeneral(
        newGeneral: GeneralDTO
    ) {
        val db = Firebase.firestore
        val generalsRef = db.collection("generals")

        val general = hashMapOf(
            "name" to newGeneral.name,
            "birthDate" to com.google.firebase.Timestamp(newGeneral.birthDate!!),
            "medals" to newGeneral.medals,
            "active" to newGeneral.active,
            "yearExperience" to newGeneral.yearExperience,
            "salary" to newGeneral.salary,
            "soldiers" to arrayListOf<DocumentReference>()
        )

        generalsRef.add(general)
            .addOnSuccessListener {
                Log.i("generals-firebase", "New general: $general")
                readGenerals()
            }
            .addOnFailureListener {
                Log.i("generals-firebase", "Failure: ${it.toString()}")
            }
    }

    private fun modifyGeneral(
        generalModified: GeneralDTO
    ) {
        val db = Firebase.firestore
        val generalsRef = db.collection("generals")
        val soldiersRef = db.collection("soldiers")
        val soldiersIDs = mutableListOf<String>()

        for (soldier in generalModified.soldiers!!) {
            soldiersIDs.add(soldier.id!!)
        }

        if (soldiersIDs.size > 0) {
            val soldiersDocuments = arrayListOf<DocumentReference>()
            soldiersRef.whereIn(FieldPath.documentId(), soldiersIDs)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isComplete) {
                        for (soldier in task.result!!) {
                            soldiersDocuments.add(soldier.reference)
                        }

                        val generalToUpdate = hashMapOf(
                            "name" to generalModified.name,
                            "birthDate" to com.google.firebase.Timestamp(generalModified.birthDate!!),
                            "medals" to generalModified.medals,
                            "active" to generalModified.active,
                            "yearExperience" to generalModified.yearExperience,
                            "salary" to generalModified.salary,
                            "soldiers" to soldiersDocuments
                        )

                        generalsRef.document(generalModified.id!!)
                            .set(generalToUpdate)
                            .addOnSuccessListener {
                                readGenerals()
                            }
                            .addOnFailureListener {
                                Log.i("generals-firebase", "Failure: ${it.toString()}")
                            }
                    }
                }
        } else {
            val generalToUpdate = hashMapOf(
                "name" to generalModified.name,
                "birthDate" to com.google.firebase.Timestamp(generalModified.birthDate!!),
                "medals" to generalModified.medals,
                "active" to generalModified.active,
                "yearExperience" to generalModified.yearExperience,
                "salary" to generalModified.salary,
                "soldiers" to arrayListOf<DocumentReference>()
            )

            generalsRef.document(generalModified.id!!)
                .set(generalToUpdate)
                .addOnSuccessListener {
                    readGenerals()
                }
                .addOnFailureListener {
                    Log.i("generals-firebase", "Failure: ${it.toString()}")
                }
        }
    }

    private fun deleteGeneral() {
        val db = Firebase.firestore
        val generalsRef = db.collection("generals")

        generalsRef.document(generals[idSelectedGeneral].id!!)
            .delete()
            .addOnSuccessListener {
                readGenerals()
            }
            .addOnFailureListener {
                Log.i("generals-firebase", "Failure: ${it.toString()}")
            }
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete General")
        builder.setMessage(R.string.confirm_delete)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            deleteGeneral()
        }

        builder.setNegativeButton("No", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun openSoldierListActivity(
        activityClass: Class<*>,
        selectedGeneral: GeneralDTO?
    ) {
        val soldiersIntent = Intent(this, activityClass)
        soldiersIntent.putExtra("general", selectedGeneral)
        soldiersListLauncher.launch(soldiersIntent)
    }
}