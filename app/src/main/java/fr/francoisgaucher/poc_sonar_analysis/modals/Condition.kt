package fr.francoisgaucher.poc_sonar_analysis.modals

import android.os.Parcel
import android.os.Parcelable

data class Condition(
    val icon: String,
    val text: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Condition(icon='$icon', text='$text')"
    }

    companion object CREATOR : Parcelable.Creator<Condition> {
        override fun createFromParcel(parcel: Parcel): Condition {
            return Condition(parcel)
        }

        override fun newArray(size: Int): Array<Condition?> {
            return arrayOfNulls(size)
        }
    }
}