package com.example.examensegundobimestre

import android.os.Parcel
import android.os.Parcelable
import java.util.Date
import kotlin.collections.ArrayList

class GeneralDTO(
    var id: String?,
    var name: String?,
    var birthDate: Date?,
    var medals: Long?,
    var active: Boolean?,
    var yearExperience: Long?,
    var salary: Double?,
    var soldiers: ArrayList<SoldierDTO>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readSerializable() as Date?,
        parcel.readValue(Int::class.java.classLoader) as? Long,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readArrayList(SoldierDTO::class.java.classLoader) as ArrayList<SoldierDTO>?
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeSerializable(birthDate)
        parcel.writeValue(medals)
        parcel.writeValue(active)
        parcel.writeValue(yearExperience)
        parcel.writeValue(salary)
        parcel.writeList(soldiers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeneralDTO> {
        override fun createFromParcel(parcel: Parcel): GeneralDTO {
            return GeneralDTO(parcel)
        }

        override fun newArray(size: Int): Array<GeneralDTO?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name!!
    }
}