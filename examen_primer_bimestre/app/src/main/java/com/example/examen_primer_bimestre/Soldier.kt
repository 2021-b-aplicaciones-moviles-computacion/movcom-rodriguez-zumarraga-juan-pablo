package com.example.examen_primer_bimestre

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.LocalDate

class Soldier(
    var name: String?,
    var birthDate: LocalDate?,
    var mainWeapon: String?,
    var active: Boolean?,
    var yearExperience: Int?,
    var salary: Double?
) : Parcelable {
    override fun toString(): String {
        return name!!
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readSerializable() as LocalDate?,
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel?.writeString(name)
        parcel?.writeSerializable(birthDate)
        parcel?.writeString(mainWeapon)
        parcel?.writeBoolean(active!!)
        parcel?.writeInt(yearExperience!!)
        parcel?.writeDouble(salary!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Soldier> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Soldier {
            return Soldier(parcel)
        }

        override fun newArray(size: Int): Array<Soldier?> {
            return arrayOfNulls(size)
        }
    }
}