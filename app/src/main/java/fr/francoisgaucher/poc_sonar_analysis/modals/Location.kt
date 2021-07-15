package fr.francoisgaucher.poc_sonar_analysis.modals

import android.os.Parcel
import android.os.Parcelable

data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val localtime_epoch: Int,
    val lon: Double,
    val name: String,
    val region: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "NOT_INITIALIZED",
        parcel.readDouble(),
        parcel.readString() ?: "NOT_INITIALIZED",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString() ?: "NOT_INITIALIZED",
        parcel.readString() ?: "NOT_INITIALIZED",
    ) {
    }

    override fun toString(): String {
        return "Location(country='$country', lat=$lat, localtime='$localtime', localtime_epoch=$localtime_epoch, lon=$lon, name='$name', region='$region')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeDouble(lat)
        parcel.writeString(localtime)
        parcel.writeInt(localtime_epoch)
        parcel.writeDouble(lon)
        parcel.writeString(name)
        parcel.writeString(region)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}