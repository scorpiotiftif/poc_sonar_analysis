package fr.francoisgaucher.poc_sonar_analysis.modals

import android.os.Parcel
import android.os.Parcelable

data class CurrentWeather(
    val current: Current,
    val location: Location
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Current::class.java.classLoader)!!,
        parcel.readParcelable(Location::class.java.classLoader)!!
    )

    override fun toString(): String {
        return "CurrentWeather(current=$current, location=$location)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(current, flags)
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrentWeather> {
        override fun createFromParcel(parcel: Parcel): CurrentWeather {
            return CurrentWeather(parcel)
        }

        override fun newArray(size: Int): Array<CurrentWeather?> {
            return arrayOfNulls(size)
        }
    }
}