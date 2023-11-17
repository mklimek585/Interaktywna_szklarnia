package pwr.project.interaktywna_szklarnia.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardViewModel : ViewModel() {
    val TAG = "DashVMdb"
    private val databaseRef = Firebase.database.reference
    private val _currentSet = MutableLiveData<String>()
    val currentSet: LiveData<String> get() = _currentSet

    private val _dataSet1 = MutableLiveData<Array<Int>>()
    val dataSet1: LiveData<Array<Int>> = _dataSet1
    private val _dataSet2 = MutableLiveData<Array<Int>>()
    val dataSet2: LiveData<Array<Int>> = _dataSet2
    private val _dataSet3 = MutableLiveData<Array<Int>>()
    val dataSet3: LiveData<Array<Int>> = _dataSet3
    private val _dataSet4 = MutableLiveData<Array<Int>>()
    val dataSet4: LiveData<Array<Int>> = _dataSet4

    interface DataCallback {
        fun onDataLoaded(data: Array<Array<Int>>)
    }

    fun loadSets(callback: DataCallback) {
        val setsRef = databaseRef.child("Szklarnia/Threshold sets")
        val result = Array<Array<Int>?>(4) { null }

        setsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEachIndexed { index, setSnapshot ->
                    if (index < result.size) {
                        val setValues = setSnapshot.children.mapNotNull { paramSnapshot ->
                            val value = paramSnapshot.value
                            when (value) {
                                is Long -> value.toInt()
                                is String -> value.toIntOrNull() ?: 0
                                else -> 0
                            }
                        }.toTypedArray()

                        result[index] = setValues
                    }
                }
                callback.onDataLoaded(result.filterNotNull().toTypedArray())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }


//    private fun logArrayValues(array: Array<Int>, setName: String) {
//        array.forEachIndexed { index, value ->
//            Log.i("ArrayLog", "$setName - Index $index: Value $value")
//        }
//    }

    private var currentSetListener: ValueEventListener? = null

    init {
        setupCurrentThresholdListener()
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


    override fun onCleared() {
        super.onCleared()
        currentSetListener?.let {
            databaseRef.child("Szklarnia/Threshold sets/In use").removeEventListener(it)
        }
    }
}

