package pwr.project.interaktywna_szklarnia
import com.google.gson.annotations.SerializedName

data class Measurement(
    val temp: Float,  // Zmiana z Int na Float
    val lux: Float    // Zmiana z Int na Float
)

data class Workstation(
    val Humidity: Float,  // Zmiana z Int na Float
    val Light: Float      // Zmiana z Int na Float
)




