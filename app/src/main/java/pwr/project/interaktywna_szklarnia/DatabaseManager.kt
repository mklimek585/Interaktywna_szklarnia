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
    private var connection: Connection? = null
    val TAG_DB = "Debug_DB"

    //    private val databaseReference = FirebaseDatabase.getInstance().getReference("path_to_your_data")
    private lateinit var database: DatabaseReference

    init {
            // Kod inicjalizacyjny
            setupSunlightListener()
        }

        fun setupSunlightListener() {
            val sunlightRef = FirebaseDatabase.getInstance().getReference("Szklarnia/Sunlight")

            sunlightRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val sunlight = dataSnapshot.getValue(Sunlight::class.java)
                    Log.i(TAG_DB, sunlight.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i(TAG_DB, "Coś poszlo nie tak")
                }
            })
        }
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
