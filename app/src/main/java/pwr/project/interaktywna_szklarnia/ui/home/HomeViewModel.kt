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
    private val _currentMes = MutableLiveData<String>()
    val currentMes: LiveData<String> get() = _currentMes
    private val _currentWk1Mes = MutableLiveData<String>()
    val currentWk1Mes: LiveData<String> get() = _currentWk1Mes
    private val _currentWk2Mes = MutableLiveData<String>()
    val currentWk2Mes: LiveData<String> get() = _currentWk2Mes

    private var currentSetListener: ValueEventListener? = null
    private var currentMeasurmentsListener: ValueEventListener? = null
    private var currentWk1MeasurmentsListener: ValueEventListener? = null
    private var currentWk2MeasurmentsListener: ValueEventListener? = null

    data class Measurement(
        val temp: Float,
        val lux: Float
    )

    data class Workstation(
        val Humidity: Float,
        val Light: Float
    )

    init {
        setupCurrentThresholdListener()
        setupCurrentMeasurementsListener()
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
        val setRef = databaseRef.child("Szklarnia/Threshold sets").child(setNumber)

        setRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val setValues = dataSnapshot.children.mapNotNull { paramSnapshot ->
                    when (val value = paramSnapshot.value) {
                        is Long -> value.toInt()
                        else -> 0
                    }
                }.toTypedArray()
                callback.onDataLoaded(setValues)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "loadSet error: $databaseError")
            }
        })
    }

    private fun setupCurrentMeasurementsListener() {
        val currentMrmRef = databaseRef.child("Szklarnia/Measurements").limitToLast(1)
        val currentWk1MrmRef = databaseRef.child("Szklarnia/Workstations/Workstation 1/Measurements").limitToLast(1)
        val currentWk2MrmRef = databaseRef.child("Szklarnia/Workstations/Workstation 2/Measurements").limitToLast(1)

        val generalListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    // Przetwarzanie danych dla ogólnych pomiarów
                    _currentMes.postValue(snapshot.value?.toString())
                    Log.i(TAG, "General Measurement: ${snapshot.value}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "General Measurement error: $databaseError")
            }
        }

        val wk1Listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    // Przetwarzanie danych dla Workstation 1
                    _currentWk1Mes.postValue(snapshot.value?.toString())
                    Log.i(TAG, "Workstation 1 Measurement: ${snapshot.value}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Workstation 1 Measurement error: $databaseError")
            }
        }

        val wk2Listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    // Przetwarzanie danych dla Workstation 2
                    _currentWk2Mes.postValue(snapshot.value?.toString())
                    Log.i(TAG, "Workstation 2 Measurement: ${snapshot.value}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Workstation 2 Measurement error: $databaseError")
            }
        }

        currentMrmRef.addValueEventListener(generalListener)
        currentMeasurmentsListener = generalListener
        currentWk1MrmRef.addValueEventListener(wk1Listener)
        currentWk1MeasurmentsListener = wk1Listener
        currentWk2MrmRef.addValueEventListener(wk2Listener)
        currentWk2MeasurmentsListener = wk2Listener
    }




}