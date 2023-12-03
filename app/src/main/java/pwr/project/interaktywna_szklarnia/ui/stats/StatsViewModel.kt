package pwr.project.interaktywna_szklarnia.ui.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.ArrayList

class StatsViewModel : ViewModel() {

    private val databaseRef = Firebase.database.reference

    data class ChartsDataModel(
        val title: String, val label1: String, val data1: Map<Int, Double>, val color1: Int,
        val label2: String, val data2: Map<Int, Double>, val color2: Int)

    data class DataModel(val humWk1_avg: Double? = null, val humWk2_avg: Double? = null, val lightWk1_avg: Double? = null,
        val lightWk2_avg: Double? = null, val lux_avg: Double? = null, val temp_avg: Double? = null
    )

    fun loadDataForTimePeriod(callback: (ArrayList<DataModel>, Boolean) -> Unit, timeRange: TimeRange) {
        val dataRef = databaseRef.child("Szklarnia/Statistics")
        val now = LocalDateTime.now(ZoneId.systemDefault()) // Użyj czasu lokalnego

        when (timeRange) {
            TimeRange.DAY -> {
                dataRef.orderByKey().limitToLast(24)
                    .addListenerForSingleValueEvent(createValueEventListener(callback, now, 1))
            }
            TimeRange.WEEK -> {
                dataRef.orderByKey().limitToLast(7 * 24)
                    .addListenerForSingleValueEvent(createValueEventListener(callback, now, 7))
            }
            TimeRange.MONTH -> {
                dataRef.addListenerForSingleValueEvent(createValueEventListener(callback, now, 30))
            }
        }
    }

    private fun createValueEventListener(
        callback: (ArrayList<DataModel>, Boolean) -> Unit,
        now: LocalDateTime,
        daysBack: Long
    ): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = ArrayList<DataModel>()
                var lastMeasurementDateTime: LocalDateTime? = null

                dataSnapshot.children.forEach { snapshot ->
                    try {
                        val measurementDate = LocalDateTime.parse(snapshot.key, DateTimeFormatter.ISO_DATE_TIME)
                        if (measurementDate.isAfter(now.minusDays(daysBack))) {
                            snapshot.getValue(DataModel::class.java)?.let {
                                result.add(it)
                                if (lastMeasurementDateTime == null || measurementDate.isAfter(lastMeasurementDateTime)) {
                                    lastMeasurementDateTime = measurementDate
                                }
                            }
                        }
                    } catch (e: DateTimeParseException) {
                        Log.e("DataLog", "Error parsing date: ${e.message}")
                    }
                }

                // Sprawdzenie, czy ostatni pomiar jest wystarczająco aktualny
                val isDataUpToDate = lastMeasurementDateTime?.isAfter(now.minusDays(1)) ?: false
                callback(result, isDataUpToDate)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DataLog", "Firebase error: ${databaseError.message}")
            }
        }
    }
}