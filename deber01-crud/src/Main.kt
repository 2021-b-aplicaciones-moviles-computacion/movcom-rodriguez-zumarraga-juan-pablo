import java.time.LocalDate

fun createNewSoldier(): Soldier {
    print("Insert name of the new soldier: ")
    val name = readLine()
    print("Insert birth date of the new soldier (yyyy-mm-dd): ")
    val rawDate = readLine()?.split("-")
    val birthDate = LocalDate.of(rawDate!![0].toInt(), rawDate!![1].toInt(), rawDate!![2].toInt())
    print("Insert main weapon (S/G/F): ")
    val mainWeapon = readLine()?.get(0)
    print("Is active the new soldier? (true/false): ")
    val active = readLine()?.toBoolean()
    print("How many years of experience have the new soldier?: ")
    val yearsExperience = readLine()?.toInt()
    print("How much is the salary for the new soldier?: ")
    val salary = readLine()?.toDouble()

    return Soldier(name!!, birthDate, mainWeapon!!, active!!, yearsExperience!!, salary!!)
}

fun createNewGeneral(): General {
    print("Insert name of the new general: ")
    val name = readLine()
    print("Insert birth date of the new general (yyyy-mm-dd): ")
    val rawDate = readLine()?.split("-")
    val birthDate = LocalDate.of(rawDate!![0].toInt(), rawDate!![1].toInt(), rawDate!![2].toInt())
    print("Insert the number of medals that the general has: ")
    val medals = readLine()?.toInt()
    print("Is active the new general? (true/false): ")
    val active = readLine()?.toBoolean()
    print("How many years of experience have the new general?: ")
    val yearsExperience = readLine()?.toInt()
    print("How much is the salary for the new general?: ")
    val salary = readLine()?.toDouble()

    return General(name!!, birthDate, medals!!, active!!, yearsExperience!!, salary!!)
}

fun linkSoldierGeneral(generals: ArrayList<General>, soldiers: ArrayList<Soldier>): Pair<Int, Int> {
    println("Please first select the general")
    generals.forEachIndexed { index, general ->
        print("${index + 1}._\n")
        general.prettyPrint()
    }
    print("Please choose a number -> ")
    val generalIndex = readLine()?.toInt()?.minus(1)


    println("Please now select the soldier")
    soldiers.forEachIndexed { index, soldier ->
        print("${index + 1}._\n")
        soldier.prettyPrint()
    }
    print("Please choose a number -> ")
    val soldierIndex = readLine()?.toInt()?.minus(1)

    return Pair(generalIndex!!, soldierIndex!!)
}

fun modifyGeneral(generals: ArrayList<General>): Int {
    println("Please select the general to modify")
    generals.forEachIndexed { index, general ->
        print("${index + 1}._\n")
        general.prettyPrint()
    }
    print("Please choose a number -> ")
    return readLine()?.toInt()?.minus(1)!!
}

fun modifySoldier(soldiers: ArrayList<Soldier>): Int {
    println("Please select the soldier to modify")
    soldiers.forEachIndexed { index, soldier ->
        print("${index + 1}._\n")
        soldier.prettyPrint()
    }
    print("Please choose a number -> ")
    return readLine()?.toInt()?.minus(1)!!
}

fun deleteGeneral(generals: ArrayList<General>): Int {
    println("Please select the general to delete")
    generals.forEachIndexed { index, general ->
        print("${index + 1}._\n")
        general.prettyPrint()
    }
    print("Please choose a number -> ")
    return readLine()?.toInt()?.minus(1)!!
}

fun deleteSoldier(soldiers: ArrayList<Soldier>): Int {
    println("Please select the soldier to delete")
    soldiers.forEachIndexed { index, soldier ->
        print("${index + 1}._\n")
        soldier.prettyPrint()
    }
    print("Please choose a number -> ")
    return readLine()?.toInt()?.minus(1)!!
}

fun main() {
    val generals = General.readFromFile()
    val soldiers = Soldier.readFromFile()

    soldiers.forEach { soldier: Soldier ->
        val generalID = soldier.readProperty(7)
        generals.forEach { general: General ->
            if (general.readProperty(0) == generalID)
                general.addSoldier(soldier)
        }
    }

    var mainOption: Int = 0

    while (mainOption != 6) {
        print(
            "1. View Generals\n2. View Soldiers\n3. Add a new General\n4. Add a new Soldier\n" +
                    "5. Link a Soldier with a General\n6. Modify a general\n7. Modify a soldier\n" +
                    "8. Delete a general\n9. Delete a soldier\n10. Exit\n->"
        )
        mainOption = readLine()!!.toInt()

        when (mainOption) {
            1 -> generals.forEach { general: General -> general.prettyPrint() }
            2 -> soldiers.forEach { soldier: Soldier -> soldier.prettyPrint() }
            3 -> {
                var general = createNewGeneral()
                generals.add(general)
                general.writeToFile()
            }
            4 -> {
                var soldier = createNewSoldier()
                soldiers.add(soldier)
                soldier.writeToFile()
            }
            5 -> {
                val (generalIndex, soldierIndex) = linkSoldierGeneral(generals, soldiers)
                generals[generalIndex].addSoldier(soldiers[soldierIndex])
            }
            6 -> {
                val generalIndex = modifyGeneral(generals)

                println("Please select the attribute to modify")
                print("1.Name\n2.Medals\n3.Active\n4.Years of Experience\n5.Salary\n->")
                val attribute = readLine()?.toInt()

                print("Please insert the new value: ")
                val newValue = readLine()

                generals[generalIndex].updateProperty(newValue!!, attribute!!)
            }
            7 -> {
                val soldierIndex = modifySoldier(soldiers)

                println("Please select the attribute to modify")
                print("1.Name\n2.Main Weapon\n3.Active\n4.Years of Experience\n5.Salary\n->")
                val attribute = readLine()?.toInt()

                print("Please insert the new value: ")
                val newValue = readLine()

                soldiers[soldierIndex].updateProperty(newValue!!, attribute!!)
            }
            8 -> {
                val generalIndex = deleteGeneral(generals)

                generals[generalIndex].deleteFromFile()
                generals.removeAt(generalIndex)
            }
            9 -> {
                val soldierIndex = deleteSoldier(soldiers)

                soldiers[soldierIndex].deleteFromFile()
                soldiers.removeAt(soldierIndex)
            }
            10 -> println("Thanks for using the app!")
            else -> println("Not a valid option")
        }
    }

}