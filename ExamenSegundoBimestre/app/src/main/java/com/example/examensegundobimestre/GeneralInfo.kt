package com.example.examensegundobimestre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import java.text.SimpleDateFormat

class GeneralInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_info)

        val isNew = intent.getBooleanExtra("new", true)

        if (!isNew) editGeneral()

        val saveButton = findViewById<Button>(R.id.button_save_general)
        saveButton.setOnClickListener {
            returnData(isNew)
        }

        val cancelButton = findViewById<Button>(R.id.button_cancel_general)
        cancelButton.setOnClickListener {
            returnCancel()
        }
    }

    private fun editGeneral() {
        val general = intent.getParcelableExtra<GeneralDTO>("general")

        val generalTitle = findViewById<TextView>(R.id.txt_general_title)
        generalTitle.text = general?.name + "'s Info"

        val editTextName = findViewById<EditText>(R.id.edit_text_name_general)
        editTextName.setText(general?.name)

        val editTextBirthDate = findViewById<EditText>(R.id.edit_text_birth_date_general)
        editTextBirthDate.setText(general?.birthDate?.toLocaleString())
        editTextBirthDate.isEnabled = false

        val editTextMedals = findViewById<EditText>(R.id.edit_text_medals_general)
        editTextMedals.setText(general?.medals.toString())

        val switchActive = findViewById<Switch>(R.id.switch_active_general)
        switchActive.isChecked = general?.active!!

        val editTextYearExperience = findViewById<EditText>(R.id.edit_text_year_experience_general)
        editTextYearExperience.setText(general?.yearExperience.toString())

        val editTextSalary = findViewById<EditText>(R.id.edit_text_salary_general)
        editTextSalary.setText(general?.salary.toString())
    }

    private fun returnData(isNew: Boolean) {
        val editTextName = findViewById<EditText>(R.id.edit_text_name_general)
        val editTextBirthDate = findViewById<EditText>(R.id.edit_text_birth_date_general)
        val editTextMedals = findViewById<EditText>(R.id.edit_text_medals_general)
        val switchActive = findViewById<Switch>(R.id.switch_active_general)
        val editTextYearExperience = findViewById<EditText>(R.id.edit_text_year_experience_general)
        val editTextSalary = findViewById<EditText>(R.id.edit_text_salary_general)

        val general: GeneralDTO


        if (isNew) {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val rawDate = editTextBirthDate.text.toString()
            general = GeneralDTO(
                null,
                editTextName.text.toString(),
                formatter.parse(rawDate),
                editTextMedals.text.toString().toLong(),
                switchActive.isChecked,
                editTextYearExperience.text.toString().toLong(),
                editTextSalary.text.toString().toDouble(),
                arrayListOf()
            )
        } else {
            general = intent.getParcelableExtra<GeneralDTO>("general")!!
            general.name = editTextName.text.toString()
            general.medals = editTextMedals.text.toString().toLong()
            general.active = switchActive.isChecked
            general.yearExperience = editTextYearExperience.text.toString().toLong()
            general.salary = editTextSalary.text.toString().toDouble()
        }

        val returnIntent = Intent()
        returnIntent.putExtra("generalModified", general)
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