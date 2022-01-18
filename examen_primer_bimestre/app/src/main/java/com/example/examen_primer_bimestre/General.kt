package com.example.examen_primer_bimestre

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.LocalDate

class General(
    var name: String?,
    var birthDate: LocalDate?,
    var medals: Int?,
    var active: Boolean?,
    var yearExperience: Int?,
    var salary: Double?,
    var soldiers: ArrayList<Soldier>?
) : Parcelable {
    override fun toString(): String {
        return name!!
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readSerializable() as LocalDate?,
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readArrayList(Soldier::class.java.classLoader) as ArrayList<Soldier>?
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel?.writeString(name)
        parcel?.writeSerializable(birthDate)
        parcel?.writeInt(medals!!)
        parcel?.writeBoolean(active!!)
        parcel?.writeInt(yearExperience!!)
        parcel?.writeDouble(salary!!)
        parcel?.writeList(soldiers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<General> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): General {
            return General(parcel)
        }

        override fun newArray(size: Int): Array<General?> {
            return arrayOfNulls(size)
        }
    }
}
