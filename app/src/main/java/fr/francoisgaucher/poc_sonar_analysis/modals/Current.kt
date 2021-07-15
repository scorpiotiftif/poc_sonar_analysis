package fr.francoisgaucher.poc_sonar_analysis.modals

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class Current(
    val condition: Condition,
    @SerializedName("feelslike_c") val feelslikeC: Double,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Condition::class.java.classLoader)!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(condition, flags)
        parcel.writeDouble(feelslikeC)
        parcel.writeDouble(tempC)
        parcel.writeDouble(tempF)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Current(condition=$condition, feelslikeC=$feelslikeC, tempC=$tempC, tempF=$tempF)"
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