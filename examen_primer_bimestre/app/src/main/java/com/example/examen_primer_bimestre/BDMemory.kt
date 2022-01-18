package com.example.examen_primer_bimestre

import java.time.LocalDate

class BDMemory {
    companion object {
        val generals = arrayListOf<General>()

        init {
            generals.add(
                General(
                    "Juan Rodriguez", LocalDate.of(1988, 12, 11),
                    5, true, 5, 1250.12,
                    arrayListOf(
                        Soldier(
                            "Pablo", LocalDate.of(1999, 12, 11),
                            "Rifle", true, 2, 850.32
                        ),

                        Soldier(
                            "Daniel", LocalDate.of(1999, 8, 19),
                            "Machine-Gun", true, 3, 820.32
                        ),
                    )
                )
            )
            generals.add(
                General(
                    "Alejandro Naula", LocalDate.of(1988, 4, 9),
                    10, false, 7, 2430.21,
                    arrayListOf(
                        Soldier(
                            "Christian", LocalDate.of(1999, 12, 11),
                            "Sniper", true, 2, 850.32
                        ),

                        Soldier(
                            "Alexander", LocalDate.of(1999, 8, 19),
                            "Rifle", true, 3, 820.32
                        ),
                    )
                )
            )
            generals.add(
                General(
                    "Leonardo Jaramillo", LocalDate.of(1987, 2, 4),
                    1, true, 2, 1000.45,
                    arrayListOf(
                        Soldier(
                            "Christopher", LocalDate.of(1999, 12, 11),
                            "Sniper", true, 2, 850.32
                        ),

                        Soldier(
                            "Carlos", LocalDate.of(1999, 8, 19),
                            "Machine-Gun", true, 3, 820.32
                        ),
                    )
                )
            )
        }
    }
}