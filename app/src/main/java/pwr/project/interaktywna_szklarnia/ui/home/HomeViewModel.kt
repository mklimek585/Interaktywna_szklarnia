package pwr.project.interaktywna_szklarnia.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    val TAG = "HomeVM"
    private val databaseRef = Firebase.database.reference
    private val _currentSet = MutableLiveData<String>()
    val currentSet: LiveData<String> get() = _currentSet

    private var currentSetListener: ValueEventListener? = null

    init {
        setupCurrentThresholdListener()
    }

    override fun onCleared() {
        super.onCleared()
        currentSetListener?.let {
            databaseRef.child("Szklarnia/Threshold sets/In use").removeEventListener(it)
        }
    }

    private fun setupCurrentThresholdListener() {
        val currentSetRef = databaseRef.child("Szklarnia/Threshold sets/In use")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val set = dataSnapshot.child("Set").getValue(Long::class.java)
                _currentSet.value = set?.toString()
                Log.i(TAG, "Set currently in use: $set")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        currentSetRef.addValueEventListener(listener)
        currentSetListener = listener
    }

    interface DataCallback {
        fun onDataLoaded(data: Array<Int>)
    }

    fun loadSet(setNumber: String, callback: DataCallback) {
        val key = if (setNumber == "4") "Custom" else setNumber
        val setRef = databaseRef.child("Szklarnia/Threshold sets").child(key)

        setRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val setValues = dataSnapshot.children.mapNotNull { paramSnapshot ->
                    val value = paramSnapshot.value
                    when (value) {
                        is Long -> value.toInt()
                        is String -> value.toIntOrNull() ?: 0
                        else -> 0
                    }
                }.toTypedArray()

                callback.onDataLoaded(setValues)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

//    private fun setupCurrentMeasurmentsListener() {
//        val currentMrmRef = databaseRef.child("Szklarnia/Measurements")
//        val currentWk1MrmRef = databaseRef.child("Szklarnia/Wrokstations/Workstation 1/Measurements")
//        val currentWk2MrmRef = databaseRef.child("Szklarnia/Wrokstations/Workstation 2/Measurements")
//        val listener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val set = dataSnapshot.child("Set").getValue(Long::class.java)
//                _currentSet.value = set?.toString()
//                Log.i(TAG, "Set currently in use: $set")
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        currentSetRef.addValueEventListener(listener)
//        currentSetListener = listener
//    }



}