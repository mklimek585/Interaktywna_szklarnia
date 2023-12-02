package pwr.project.interaktywna_szklarnia.ui.stats

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

class StatsViewModel : ViewModel() {

    private val databaseRef = Firebase.database.reference

    data class DataModel(val title: String, val label1: String, val data1: Map<Int, Double>, val color1: Int,
                         val label2: String, val data2: Map<Int, Double>, val color2: Int)

    interface DataCallback { fun onDataLoaded(data: Array<DataModel>) }


    fun loadDataForTimePeriod(callback: DataCallback, days: Int) {
        val dataRef = databaseRef.child("Szklarnia/Data")
        val endDate = LocalDateTime.now(ZoneOffset.UTC)
        val startDate = endDate.minusDays(days.toLong())

        dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = mutableListOf<DataModel>()

                dataSnapshot.children.forEach { snapshot ->
                    val timestamp = snapshot.key ?: return@forEach
                    val dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)

                    if (dateTime in startDate..endDate) {
                        val dataModel = snapshot.getValue(DataModel::class.java)
                        if (dataModel != null) {
                            result.add(dataModel)
                        }
                    }
                }

                callback.onDataLoaded(result.toTypedArray())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }


}