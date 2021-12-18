import java.io.FileReader
import java.io.FileWriter
import java.time.LocalDate
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class Soldier(
    private var name: String,
    private val birthDate: LocalDate,
    private var mainWeapon: Char,
    private var active: Boolean,
    private var yearExperience: Int,
    private var salary: Double,
    private var id: UUID? = null,
    private var generalId: UUID? = null,
) {
    init {
        if (id == null)
            this.id = UUID.randomUUID()
    }

    fun readProperty(selectedProperty: Int): String? {
        return when (selectedProperty) {
            0 -> id.toString()
            1 -> name
            2 -> birthDate.toString()
            3 -> mainWeapon.toString()
            4 -> active.toString()
            5 -> yearExperience.toString()
            6 -> salary.toString()
            7 -> generalId.toString()
            else -> null
        }
    }

    fun updateProperty(newValue: String, selectedProperty: Int) {
        when (selectedProperty) {
            (0) -> this.name = newValue
            (1) -> this.mainWeapon = newValue[0]
            (2) -> this.active = newValue.toBoolean()
            (3) -> this.yearExperience = newValue.toInt()
            (4) -> this.salary = newValue.toDouble()
            (5) -> {
                if (newValue == "")
                    this.generalId = null
            }
            else -> println("Property doesn't exist")
        }
        this.updateFile()
    }

    private fun updateFile() {
        try {
            var fileReader = FileReader("soldiers.txt")
            var lines = fileReader.readLines().toMutableList()

            lines.forEach {
                val tokens = it.split("||")
                val fileID = UUID.fromString(tokens[0])

                if (fileID == this.id) {
                    val index = lines.indexOf(it)
                    lines[index] = this.toString()
                    return@forEach
                }
            }
            fileReader.close()

            var fileWriter = FileWriter("soldiers.txt")
            lines.forEach { fileWriter.write(it) }
            fileWriter.close()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun writeToFile() {
        try {
            var fileWriter = FileWriter("soldiers.txt", true)
            fileWriter.write(this.toString())
            fileWriter.close()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun deleteFromFile() {
        try {
            var fileReader = FileReader("soldiers.txt")
            var lines = fileReader.readLines()

            lines.forEach {
                val tokens = it.split("||")
                val fileID = UUID.fromString(tokens[0])

                if (fileID == this.id) {
                    val index = lines.indexOf(it)
                    lines.drop(index)
                    return@forEach
                }
            }
            fileReader.close()

            var fileWriter = FileWriter("soldiers.txt")
            lines.forEach { fileWriter.write(it) }
            fileWriter.close()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun prettyPrint() {
        println("Name: $name\nBirth Date: $birthDate\nMain Weapon: $mainWeapon\nYears Experience: $yearExperience\n")
    }

    companion object {
        fun readFromFile(): ArrayList<Soldier> {
            val soldiers: ArrayList<Soldier> = arrayListOf()
            try {
                var fileReader = FileReader("soldiers.txt")
                var lines = fileReader.readLines()

                lines.forEach {
                    soldiers.add(processFileLine(it))
                }

                fileReader.close()
            } catch (ex: Exception) {
                println(ex.message)
            }

            return soldiers
        }

        fun processFileLine(rawSoldier: String): Soldier {
            val rawData = rawSoldier.split("||")

            val id = UUID.fromString(rawData[0])
            val name = rawData[1]

            val rawDate = rawData[2].split("-")
            val birthDate = LocalDate.of(rawDate[0].toInt(), rawDate[1].toInt(), rawDate[2].toInt())

            val mainWeapon = rawData[3][0]
            val active = rawData[4].toBoolean()
            val yearExperience = rawData[5].toInt()
            val salary = rawData[6].toDouble()
            val generalId: UUID

            return if (rawData.size > 7) {
                generalId = UUID.fromString(rawData[7])
                Soldier(name, birthDate, mainWeapon, active, yearExperience, salary, id, generalId)
            } else
                Soldier(name, birthDate, mainWeapon, active, yearExperience, salary, id)
        }
    }

    override fun toString(): String {
        return if (generalId == null)
            "$id||$name||$birthDate||$mainWeapon||$active||$yearExperience||$salary\n"
        else
            "$id||$name||$birthDate||$mainWeapon||$active||$yearExperience||$salary||$generalId\n"
    }
}