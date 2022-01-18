package com.example.examen_primer_bimestre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.time.LocalDate

class SoldierInfo : AppCompatActivity() {
    private var selectedMainWeapon = "Sniper"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soldier_info)
        Log.i("chip-group", selectedMainWeapon)

        val isNew = intent.getBooleanExtra("new", true)

        if (!isNew) editSoldier()

        val saveButton = findViewById<Button>(R.id.button_save_soldier)
        saveButton.setOnClickListener {
            returnData(isNew)
        }

        val cancelButton = findViewById<Button>(R.id.button_cancel_soldier)
        cancelButton.setOnClickListener {
            returnCancel()
        }

        val mainWeaponGroup = findViewById<ChipGroup>(R.id.chip_group_main_weapon)
        mainWeaponGroup.setOnCheckedChangeListener { group, checkedId ->
            (findViewById<Chip>(checkedId))?.let {
                selectedMainWeapon = "${it.text}"
            }
        }
    }

    private fun editSoldier() {
        val soldier = intent.getParcelableExtra<Soldier>("soldier")

        val soldierTitle = findViewById<TextView>(R.id.txt_soldier_title)
        soldierTitle.text = soldier?.name + "'s Info"

        val editTextName = findViewById<EditText>(R.id.edit_text_name_soldier)
        editTextName.setText(soldier?.name)

        val editTextBirthDate = findViewById<EditText>(R.id.edit_text_birth_date_soldier)
        editTextBirthDate.setText(soldier?.birthDate.toString())
        editTextBirthDate.isEnabled = false

        val mainWeaponGroup = findViewById<ChipGroup>(R.id.chip_group_main_weapon)
        when (soldier?.mainWeapon) {
            "Sniper" -> {
                mainWeaponGroup.check(R.id.sniper_opt)
            }
            "Rifle" -> {
                mainWeaponGroup.check(R.id.rifle_opt)
            }
            "Machine-Gun" -> {
                mainWeaponGroup.check(R.id.machine_gun_opt)
            }
        }
        selectedMainWeapon = soldier?.mainWeapon!!

        val switchActive = findViewById<Switch>(R.id.switch_active_soldier)
        switchActive.isChecked = soldier?.active!!

        val editTextYearExperience = findViewById<EditText>(R.id.edit_text_year_experience_soldier)
        editTextYearExperience.setText(soldier?.yearExperience.toString())

        val editTextSalary = findViewById<EditText>(R.id.edit_text_salary_soldier)
        editTextSalary.setText(soldier?.salary.toString())
    }

    private fun returnData(isNew: Boolean) {
        val editTextName = findViewById<EditText>(R.id.edit_text_name_soldier)
        val editTextBirthDate = findViewById<EditText>(R.id.edit_text_birth_date_soldier)
        val switchActive = findViewById<Switch>(R.id.switch_active_soldier)
        val editTextYearExperience = findViewById<EditText>(R.id.edit_text_year_experience_soldier)
        val editTextSalary = findViewById<EditText>(R.id.edit_text_salary_soldier)

        var soldier: Soldier

        if (isNew) {
            soldier = Soldier(
                editTextName.text.toString(),
                LocalDate.parse(editTextBirthDate.text.toString()),
                selectedMainWeapon,
                switchActive.isChecked,
                editTextYearExperience.text.toString().toInt(),
                editTextSalary.text.toString().toDouble()
            )
        } else {
            soldier = intent.getParcelableExtra<Soldier>("soldier")!!
            soldier?.name = editTextName.text.toString()
            soldier?.birthDate = LocalDate.parse(editTextBirthDate.text.toString())
            soldier?.mainWeapon = selectedMainWeapon
            soldier?.active = switchActive.isChecked
            soldier?.yearExperience = editTextYearExperience.text.toString().toInt()
            soldier?.salary = editTextSalary.text.toString().toDouble()
        }

        val returnIntent = Intent()
        returnIntent.putExtra("soldierModified", soldier)
        returnIntent.putExtra("isNewModified", isNew)

        setResult(
            RESULT_OK, returnIntent
        )

        finish()
    }

    private fun returnCancel() {
        val returnIntent = Intent()

        setResult(
            RESULT_CANCELED, returnIntent
        )

        finish()
    }
}