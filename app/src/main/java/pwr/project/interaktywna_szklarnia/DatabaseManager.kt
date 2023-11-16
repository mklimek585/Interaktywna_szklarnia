package pwr.project.interaktywna_szklarnia

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pwr.project.interaktywna_szklarnia.Sunlight
import java.sql.Connection
import java.sql.DriverManager
import kotlin.math.log

class DatabaseManager {
    private lateinit var database: DatabaseReference
    val TAG_DB = "Debug_DB"
    init {
        setupDatabase()
    }

    private fun setupDatabase() {
        database = Firebase.database.reference
//        setupLatestObjectMeasurmentsListener()
//        setupAllObjectMeasurmentsListener()
//        setupCurrentThresholdListener()
    }

    private fun setupLatestObjectMeasurmentsListener() {
        val measurementsRef = database.child("Szklarnia/Measurements")
        measurementsRef.orderByKey().limitToLast(1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val lux = snapshot.child("lux").getValue(String::class.java)
                    val temp = snapshot.child("temp").getValue(String::class.java)
                    Log.i(TAG_DB, "Latest Measurement - Lux: $lux, Temp: $temp")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG_DB, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun setupAllObjectMeasurmentsListener() {
        val measurementsRef = database.child("Szklarnia/Measurements")
        measurementsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val lux = snapshot.child("lux").getValue(String::class.java)
                    val temp = snapshot.child("temp").getValue(String::class.java)
                    Log.i(TAG_DB, "Measurement - Lux: $lux, Temp: $temp")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG_DB, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

//    private fun setupCurrentThresholdListener() {
//        val currentSetRef = database.child("Szklarnia/Threshold sets/In use")
//        currentSetRef.orderByKey().addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val set = dataSnapshot.child("Set").getValue(Long::class.java)
//                Log.i(TAG_DB, "Set currently in use: $set")
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w(TAG_DB, "loadPost:onCancelled", databaseError.toException())
//            }
//        })
//    }
}








    //database = Firebase.database.reference ---- tak zapis
//    fun listenForDataChanges(): LiveData<YourDataType> {
//        val liveData = MutableLiveData<YourDataType>()
//
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // Tutaj przekształć snapshot na Twój typ danych i wyślij do LiveData
//                liveData.value = snapshot.getValue(YourDataType::class.java)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Log error, handle cancellation
//            }
//        }
//
//        database.addValueEventListener(listener)
//        return liveData
//    }

    // Pamiętaj, aby usunąć listener, gdy nie jest już potrzebny
//    fun removeListener() {
//        database.removeEventListener(listener)
//    }



//    // Write a message to the database
//    val database = Firebase.database
//    val myRef = database.getReference("message")
//
//    myRef.setValue("Hello, World!")
//    // Read from the database
//    myRef.addValueEventListener(object: ValueEventListener() {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            // This method is called once with the initial value and again
//            // whenever data at this location is updated.
//            val value = snapshot.getValue<String>()
//            Log.d(TAG, "Value is: " + value)
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.w(TAG, "Failed to read value.", error.toException())
//        }
//
//    })
