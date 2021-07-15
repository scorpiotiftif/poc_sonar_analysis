package fr.francoisgaucher.poc_sonar_analysis.modals

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    @SerializedName("localtime_epoch") val localtimeEpoch: Int,
    val lon: Double,
    val name: String,
    val region: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun toString(): String {
        return "Location(country='$country', lat=$lat, localtime='$localtime', localtimeEpoch=$localtimeEpoch, lon=$lon, name='$name', region='$region')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeDouble(lat)
        parcel.writeString(localtime)
        parcel.writeInt(localtimeEpoch)
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