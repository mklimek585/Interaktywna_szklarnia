package pwr.project.interaktywna_szklarnia.ui.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class StatsViewModel : ViewModel() {

    private val databaseRef = Firebase.database.reference

    data class ChartsDataModel(val title: String, val label1: String, val data1: Map<Int, Double>, val color1: Int,
                         val label2: String, val data2: Map<Int, Double>, val color2: Int)

    data class DataModel(val humWk1_avg: Double? = null, val humWk2_avg: Double? = null, val lightWk1_avg: Double? = null,
        val lightWk2_avg: Double? = null, val lux_avg: Double? = null, val temp_avg: Double? = null
    )


    interface DataCallback { fun onDataLoaded(data: Array<DataModel>) }

    val dataContainer = mutableListOf<Float>()


    fun loadDataForTimePeriod(callback: DataCallback, days: Int) {
        val dataRef = databaseRef.child("Szklarnia/Statistics")
        val endDate = LocalDateTime.now(ZoneOffset.UTC)
        val startDate = endDate.minusDays(days.toLong())

        dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = mutableListOf<DataModel>()

                dataSnapshot.children.forEach { snapshot ->
                    try {
                        val timestamp = snapshot.key ?: return@forEach
                        val dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)

                        if (dateTime in startDate..endDate) {
                            val dataModel = snapshot.getValue(DataModel::class.java)
                            if (dataModel != null) {
                                result.add(dataModel)
                            }
                        }
                    } catch (e: DateTimeParseException) {
                        Log.e("DataLog", "Error parsing date: ${e.message}")
                    }
                }
                callback.onDataLoaded(result.toTypedArray())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DataLog", "Firebase error: ${databaseError.message}")
            }
        })
    }



}