package com.example.examensegundobimestre

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class SoldierDTO(
    var id: String?,
    var name: String?,
    var birthDate: Date?,
    var mainWeapon: String?,
    var active: Boolean?,
    var yearExperience: Long?,
    var salary: Double?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readSerializable() as Date?,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeSerializable(birthDate)
        parcel.writeString(mainWeapon)
        parcel.writeValue(active)
        parcel.writeValue(yearExperience)
        parcel.writeValue(salary)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SoldierDTO> {
        override fun createFromParcel(parcel: Parcel): SoldierDTO {
            return SoldierDTO(parcel)
        }

        override fun newArray(size: Int): Array<SoldierDTO?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name!!
    }
}