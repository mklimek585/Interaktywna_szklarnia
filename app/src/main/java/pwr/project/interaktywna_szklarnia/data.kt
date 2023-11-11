package pwr.project.interaktywna_szklarnia

data class Sunlight(val lux: Int? = null)

data class Temperature(val temp: Int? = null)

data class WorkstationThreshold(val Humidity: String? = null, val LightIntensity: String? = null)

data class ThresholdSet(
    val Custom: Map<String, WorkstationThreshold>? = null,
    // Możesz dodać inne, np. "1", "2", "3" jeśli ich struktura jest stała
    // W innym przypadku użyj Map<String, WorkstationThreshold> dla dynamicznych kluczy
)

data class Humidity(val hum: Int? = null)

data class LightIntensity(val lux: Int? = null)

data class Workstation(val Humidity: Humidity? = null, val LightIntensity: LightIntensity? = null)

data class Szklarnia(
    val Sunlight: Sunlight? = null,
    val Temperature: Temperature? = null,
    val ThresholdSets: ThresholdSet? = null,
    val Workstations: List<Workstation?>? = null
)
