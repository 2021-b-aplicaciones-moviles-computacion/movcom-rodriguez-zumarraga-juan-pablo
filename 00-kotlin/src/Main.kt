import java.util.*
import kotlin.collections.ArrayList

fun main() {
    println("Hola mundo")

    // * Tipos de variables

    // Inmutables

    val inmutable: String = "Juan"
    // inmutable = "Pablo" // X

    // Mutables

    var mutable: String = "Pablo"
    mutable = "Juan"

    // De preferencia usar variables inmutables

    // * Sintaxis y Duck Typing
    // Inferir el tipo de dato de las variables

    val ejemploVariable = "Nombre"

    var edadEjemplo = 12
    edadEjemplo = 14

    // * Tipos de variables JAVA

    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'S'
    val fechaNacimiento: Date = Date()

    // * Condicionales
    if (true) {

    } else {

    }

    val estadoCivilWhen: String = "S"

    // * Switch case (When)
    when (estadoCivilWhen) {
        ("S") -> {
            println("Acercarse")
        }
        ("C") -> {
            println("Alejarse")
        }
        "UN" -> println("Hablar")
        else -> println("No reconocido")
    }

    // * Operador terneario
    val coqueteo = if (estadoCivilWhen == "S") "Si" else "No"

    imprimirNombre("Juan")

    calcularSueldo(100.00)
    calcularSueldo(100.00, 14.00)
    calcularSueldo(100.00, 14.00, 25.00)

    // * Parametros nombrados
    calcularSueldo(
        bonoEspecial = 15.00,
        // tasa = 12.00,
        sueldo = 150.00
    )

    // * Tipos de arreglos
    val arregloEstatico: Array<Int> = arrayOf(1, 2, 3)

    val arregloDinamico: ArrayList<Int> = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    // * For Each

    val respuestaForEach: Unit =
        arregloDinamico.forEach { valorActual: Int -> println("Valor actual : ${valorActual}") }

    arregloDinamico.forEachIndexed { indice: Int, valorActual: Int -> println("Valor ${valorActual}, indice: ${indice}") }

    println(respuestaForEach)

    // * Map -> Mapea el arreglo
    val respuestaMap: List<Double> =
        arregloDinamico.map { valorActual: Int -> return@map valorActual.toDouble() + 100.00 }

    println(respuestaMap)

    val respuestaMapDos = arregloDinamico.map { it + 15 }
    println(respuestaMapDos)

}

// * Funciones
fun imprimirNombre(nombre: String): Unit { // Unit -> Void (Opcional)
    println("Nombre: ${nombre}")
}

fun calcularSueldo(
    sueldo: Double, // Requerido
    tasa: Double = 12.00, // Opcional (Valor por defecto)
    bonoEspecial: Double? = null // Opcional (Null)
): Double {
    if (bonoEspecial == null) {
        return sueldo * (100 / tasa)
    } else {
        return sueldo * (100 / tasa) + bonoEspecial
    }
}