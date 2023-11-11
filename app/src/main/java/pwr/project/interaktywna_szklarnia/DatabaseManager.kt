package pwr.project.interaktywna_szklarnia

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Connection
import java.sql.DriverManager

class DatabaseManager {
    private var connection: Connection? = null
    val TAG_DB = "Debug_DB"

    private val databaseReference = FirebaseDatabase.getInstance().getReference("path_to_your_data")

    fun listenForDataChanges(): LiveData<YourDataType> {
        val liveData = MutableLiveData<YourDataType>()

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Tutaj przekształć snapshot na Twój typ danych i wyślij do LiveData
                liveData.value = snapshot.getValue(YourDataType::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log error, handle cancellation
            }
        }

        databaseReference.addValueEventListener(listener)
        return liveData
    }

    // Pamiętaj, aby usunąć listener, gdy nie jest już potrzebny
    fun removeListener() {
        databaseReference.removeEventListener(listener)
    }

}