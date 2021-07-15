package fr.francoisgaucher.poc_sonar_analysis.modals

import android.os.Parcel
import android.os.Parcelable

data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val temp_c: Double,
    val temp_f: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Condition::class.java.classLoader)!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(condition, flags)
        parcel.writeDouble(feelslike_c)
        parcel.writeDouble(temp_c)
        parcel.writeDouble(temp_f)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Current(condition=$condition, feelslike_c=$feelslike_c, temp_c=$temp_c, temp_f=$temp_f)"
    }

    companion object CREATOR : Parcelable.Creator<Current> {
        override fun createFromParcel(parcel: Parcel): Current {
            return Current(parcel)
        }

        override fun newArray(size: Int): Array<Current?> {
            return arrayOfNulls(size)
        }
    }
}