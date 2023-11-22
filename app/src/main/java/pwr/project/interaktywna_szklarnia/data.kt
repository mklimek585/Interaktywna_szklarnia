package pwr.project.interaktywna_szklarnia
import com.google.gson.annotations.SerializedName

data class Sunlight(val lux: Int? = null)

data class Temperature(val temp: Int? = null)
data class Measurement(
    val temp: Int,
    val lux: Int
    )

data class Workstation( //TODO doprecyzuj w backendzie
    val Humidity: Int,
    val Light: Int
)


data class Humidity(val hum: Int? = null)

data class LightIntensity(val lux: Int? = null)



