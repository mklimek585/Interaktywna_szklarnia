package pwr.project.interaktywna_szklarnia.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pwr.project.interaktywna_szklarnia.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    val TAG = "DashFG"
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel

    // Dane do wczytania z bazy danych
    var set1 = arrayOf(55,55,55,55,55,55) // wk1lum, wk1hum, wk2lum, wk2hum, mslum, mstemp
    var set2 = arrayOf(55,55,55,55,55,55)
    var set3 = arrayOf(55,55,55,55,55,55)
    var set4 = arrayOf(55,55,55,55,55,55)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Obserwowanie LiveData
        viewModel.loadSets(object : DashboardViewModel.DataCallback {
            override fun onDataLoaded(data: Array<Array<Int>>) {
                data.forEachIndexed { setIndex, set ->
                    set.forEachIndexed { valueIndex, value ->
                        Log.i("ArrayLog", "Set ${setIndex + 1} - Index $valueIndex: Value $value")
                        when(setIndex) {
                            0 -> set1[valueIndex] = value
                            1 -> set2[valueIndex] = value
                            2 -> set3[valueIndex] = value
                            3 -> set4[valueIndex] = value
                        }
                    }
                }
                updateUIBasedOnCurrentSet()
            }
        })

        viewModel.currentSet.observe(viewLifecycleOwner, Observer { set ->
            RadioButtonInit(set.toLong()) })

        //val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnConfirm.setOnClickListener { view ->
            updateDbFromUI(view)
        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = group.findViewById(checkedId)
            onRadioButtonClicked(radioButton)
        }

        binding.btnPlusPar1.setOnClickListener { view -> IncreasePar1(view) }
        binding.btnPlusPar2.setOnClickListener { view -> IncreasePar2(view) }
        binding.btnPlusPar3.setOnClickListener { view -> IncreasePar3(view) }
        binding.btnPlusPar4.setOnClickListener { view -> IncreasePar4(view) }
        binding.btnPlusPar5.setOnClickListener { view -> IncreasePar5(view) }
        binding.btnPlusPar6.setOnClickListener { view -> IncreasePar6(view) }

        binding.btnMinusPar1.setOnClickListener { view -> DecreasePar1(view) }
        binding.btnMinusPar2.setOnClickListener { view -> DecreasePar2(view) }
        binding.btnMinusPar3.setOnClickListener { view -> DecreasePar3(view) }
        binding.btnMinusPar4.setOnClickListener { view -> DecreasePar4(view) }
        binding.btnMinusPar5.setOnClickListener { view -> DecreasePar5(view) }
        binding.btnMinusPar6.setOnClickListener { view -> DecreasePar6(view) }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateUIBasedOnCurrentSet() {
        viewModel.currentSet.value?.let { currentSet ->
            RadioButtonInit(currentSet.toLong())
        }
    }
        // set -> sun 0 / temp 1 / wk1hum 2 / wk1light 3 / wk2hum 4 / wk2light 5
    fun fillData(set: Array<Int>) {
        binding.ETpar1.setText(set[3].toString()) // wk1lum
        binding.ETpar2.setText(set[2].toString()) // wk1hum
        binding.ETpar3.setText(set[5].toString()) // wk2lum
        binding.ETpar4.setText(set[4].toString()) // wk2hum
        binding.ETpar5.setText(set[0].toString()) // sun
        binding.ETpar6.setText(set[1].toString()) // temp
    }

    fun updateDbFromUI(view: View) { // setNumber
        val setNumber = when {
            binding.RD1.isChecked -> 1
            binding.RD2.isChecked -> 2
            binding.RD3.isChecked -> 3
            binding.RDR.isChecked -> 4
            else -> {
                Log.i(TAG, "Error in getting setNumber from radioButton")
                return
            }
        }
        viewModel.updateCurrentSetInDatabase(setNumber)

        if(setNumber == 4) {
            try {
                val wk1lum = binding.ETpar1.text.toString().toInt() // wk1lum
                val wk1hum = binding.ETpar2.text.toString().toInt() // wk1hum
                val wk2lum = binding.ETpar3.text.toString().toInt() // wk2lum
                val wk2hum = binding.ETpar4.text.toString().toInt() // wk2hum
                val sun = binding.ETpar5.text.toString().toInt() // sun
                val temp = binding.ETpar6.text.toString().toInt() // temp

                val updatedSet = hashMapOf(
                    "wk1-Light" to wk1lum,
                    "wk1-Humidity" to wk1hum,
                    "wk2-Light" to wk2lum,
                    "wk2-Humidity" to wk2hum,
                    "Sunlight" to sun,
                    "Temperature" to temp
                )

                viewModel.updateDatabase(setNumber, updatedSet) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(context, "Zaktualizowano", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Aktualizacja nie powiodła się", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Wprowadź poprawne wartości liczbowe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                binding.RD1.id ->
                    if (checked) {
                        fillData(set1)
                    }
                binding.RD2.id ->
                    if (checked) {
                        fillData(set2)
                    }
                binding.RD3.id ->
                    if (checked) {
                        fillData(set3)
                    }
                binding.RDR.id ->
                    if (checked) {
                        // Rethink idea
                        // If usr wants to edit one of presets -> dont change all values
                        // Else if user want to get already stored custom set -> click on radio btn
                        fillData(set4)
                    }
            }
        }
    }

    fun RadioButtonInit(current: Long?) {
        if (current != null) {
            when(current.toInt()) {
                1 -> { binding.RD1.isChecked = true
                    fillData(set1) }
                2 -> { binding.RD2.isChecked = true
                    fillData(set2) }
                3 -> { binding.RD3.isChecked = true
                    fillData(set3) }
                4 -> { binding.RDR.isChecked = true
                    fillData(set4) }
                else -> Log.i(TAG, "Current set value is valid")
            }
        }
    }

    fun IncreasePar1(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
            val plus = 5
            val param = binding.ETpar1.text.toString().toInt()

            val result: Int = param + plus
            binding.ETpar1.setText(result.toString())
    }
    fun IncreasePar2(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val plus = 5
        val param = binding.ETpar2.text.toString().toInt()

        val result: Int = param + plus
        binding.ETpar2.setText(result.toString())
    }
    fun IncreasePar3(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val plus = 5
        val param = binding.ETpar3.text.toString().toInt()

        val result: Int = param + plus
        binding.ETpar3.setText(result.toString())
    }
    fun IncreasePar4(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val plus = 5
        val param = binding.ETpar4.text.toString().toInt()

        val result: Int = param + plus
        binding.ETpar4.setText(result.toString())
    }
    fun IncreasePar5(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val plus = 5
        val param = binding.ETpar5.text.toString().toInt()

        val result: Int = param + plus
        binding.ETpar5.setText(result.toString())
    }
    fun IncreasePar6(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val plus = 5
        val param = binding.ETpar6.text.toString().toInt()

        val result: Int = param + plus
        binding.ETpar6.setText(result.toString())
    }
//////////////////////////////////////////////////////////////
    fun DecreasePar1(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val minus = 5
        val param = binding.ETpar1.text.toString().toInt()

        val result: Int = param - minus
        binding.ETpar1.setText(result.toString())
    }
    fun DecreasePar2(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val minus = 5
        val param = binding.ETpar2.text.toString().toInt()

        val result: Int = param - minus
        binding.ETpar2.setText(result.toString())
    }
    fun DecreasePar3(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val minus = 5
        val param = binding.ETpar3.text.toString().toInt()

        val result: Int = param - minus
        binding.ETpar3.setText(result.toString())
    }
    fun DecreasePar4(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val minus = 5
        val param = binding.ETpar4.text.toString().toInt()

        val result: Int = param - minus
        binding.ETpar4.setText(result.toString())
    }
    fun DecreasePar5(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val minus = 5
        val param = binding.ETpar5.text.toString().toInt()

        val result: Int = param - minus
        binding.ETpar5.setText(result.toString())
    }
    fun DecreasePar6(view: View) {
        if (!binding.RDR.isChecked) {
            binding.RDR.isChecked = true
        }
        val minus = 5
        val param = binding.ETpar6.text.toString().toInt()

        val result: Int = param - minus
        binding.ETpar6.setText(result.toString())
    }
}