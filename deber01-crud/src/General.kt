import java.io.FileReader
import java.io.FileWriter
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class General(
    private var name: String,
    private val birthDate: LocalDate,
    private var medals: Int,
    private var active: Boolean,
    private var yearExperience: Int,
    private var salary: Double,
    private var id: UUID? = null,
    private var soldiers: ArrayList<Soldier>? = null
) {
    init {
        if (id == null)
            id = UUID.randomUUID()
    }

    fun readProperty(selectedProperty: Int): String? {
        return when (selectedProperty) {
            0 -> this.id.toString()
            1 -> this.name
            2 -> birthDate.toString()
            3 -> medals.toString()
            4 -> active.toString()
            5 -> yearExperience.toString()
            6 -> salary.toString()
            else -> null
        }
    }

    fun updateProperty(newValue: String, selectedProperty: Int) {
        when (selectedProperty) {
            (0) -> this.name = newValue
            (1) -> this.medals = newValue.toInt()
            (2) -> this.active = newValue.toBoolean()
            (3) -> this.yearExperience = newValue.toInt()
            (4) -> this.salary = newValue.toDouble()
            else -> println("Property don't exist")
        }
        this.updateFile()
    }

    private fun updateFile() {
        try {
            var fileReader = FileReader("generals.txt")
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

            var fileWriter = FileWriter("generals.txt")
            lines.forEach { fileWriter.write(it) }
            fileWriter.close()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun writeToFile() {
        try {
            var fileWriter = FileWriter("generals.txt", true)
            fileWriter.write(this.toString())
            fileWriter.close()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }


    fun deleteFromFile() {
        try {
            var fileReader = FileReader("generals.txt")
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

            var fileWriter = FileWriter("generals.txt")
            lines.forEach { fileWriter.write(it) }
            fileWriter.close()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun addSoldier(newSoldier: Soldier) {
        newSoldier.updateProperty(this.id.toString(), 5)

        if (this.soldiers == null)
            this.soldiers = arrayListOf(newSoldier)
        else
            this.soldiers!!.add(newSoldier)
    }

    fun removeSoldier(soldier: Soldier) {
        soldier.updateProperty("", 5)
        this.soldiers?.remove(soldier)
    }

    fun prettyPrint() {
        println("Name: $name\nBirth Date: $birthDate\nMedals: $medals\nYears Experience: $yearExperience\n")
    }

    companion object {
        fun readFromFile(): ArrayList<General> {
            val generals: ArrayList<General> = arrayListOf()

            try {
                var fileReader = FileReader("generals.txt")
                var lines = fileReader.readLines()

                lines.forEach { generals.add(processFileLine(it)) }

                fileReader.close()
            } catch (ex: Exception) {
                println(ex.message)
            }

            return generals
        }

        fun processFileLine(rawGeneral: String): General {
            val rawData = rawGeneral.split("||")

            val id = UUID.fromString(rawData[0])
            val name = rawData[1]

            val rawDate = rawData[2].split("-")
            val birthDate = LocalDate.of(rawDate[0].toInt(), rawDate[1].toInt(), rawDate[2].toInt())

            val medals = rawData[3].toInt()
            val active = rawData[4].toBoolean()
            val yearExperience = rawData[5].toInt()
            val salary = rawData[6].toDouble()

            return General(name, birthDate, medals, active, yearExperience, salary, id)
        }
    }

    override fun toString(): String {
        return "$id||$name||$birthDate||$medals||$active||$yearExperience||$salary\n"
    }
}