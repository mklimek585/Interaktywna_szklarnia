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
    val set1 = arrayOf(40,60,50,65,200,23) // wk1lum, wk1hum, wk2lum, wk2hum, mslum, mstemp
    val set2 = arrayOf(30,50,40,55,150,20)
    val set3 = arrayOf(40,60,40,70,210,30)
    val reczne = arrayOfNulls<Int>(6)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inicjalizacja UI i ViewModel
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Obserwowanie LiveData
        viewModel.currentSet.observe(viewLifecycleOwner, Observer { set ->
            RadioButtonInit(set.toLong())
            // Aktualizacja UI na podstawie wartości 'set'
        })

        viewModel.loadSets(object : DashboardViewModel.DataCallback {
            override fun onDataLoaded(data: Array<Array<Int>>) {
                data.forEachIndexed { setIndex, set ->
                    set.forEachIndexed { valueIndex, value ->
                        Log.i("ArrayLog", "Set ${setIndex + 1} - Index $valueIndex: Value $value")
                    }
                }
            }
        })

        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnConfirm.setOnClickListener { view ->
            SaveToDatabase(view)
        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = group.findViewById(checkedId)
            onRadioButtonClicked(radioButton)
        }

        binding.btnPlusPar1.setOnClickListener { view ->
            IncreasePar1(view)
        }
        binding.btnPlusPar2.setOnClickListener { view ->
            IncreasePar2(view)
        }
        binding.btnPlusPar3.setOnClickListener { view ->
            IncreasePar3(view)
        }
        binding.btnPlusPar4.setOnClickListener { view ->
            IncreasePar4(view)
        }
        binding.btnPlusPar5.setOnClickListener { view ->
            IncreasePar5(view)
        }
        binding.btnPlusPar6.setOnClickListener { view ->
            IncreasePar6(view)
        }

        binding.btnMinusPar1.setOnClickListener { view ->
            DecreasePar1(view)
        }
        binding.btnMinusPar2.setOnClickListener { view ->
            DecreasePar2(view)
        }
        binding.btnMinusPar3.setOnClickListener { view ->
            DecreasePar3(view)
        }
        binding.btnMinusPar4.setOnClickListener { view ->
            DecreasePar4(view)
        }
        binding.btnMinusPar5.setOnClickListener { view ->
            DecreasePar5(view)
        }
        binding.btnMinusPar6.setOnClickListener { view ->
            DecreasePar6(view)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun FillData(set: Array<Int>) {
        binding.ETpar1.setText(set[0].toString())
        binding.ETpar2.setText(set[1].toString())
        binding.ETpar3.setText(set[2].toString())
        binding.ETpar4.setText(set[3].toString())
        binding.ETpar5.setText(set[4].toString())
        binding.ETpar6.setText(set[5].toString())
    }

    fun SaveToDatabase(view: View) {
        Toast.makeText(requireContext(), "Zapisano", Toast.LENGTH_SHORT).show()
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                binding.RD1.id ->
                    if (checked) {
                        FillData(set1)
                    }
                binding.RD2.id ->
                    if (checked) {
                        FillData(set2)
                    }
                binding.RD3.id ->
                    if (checked) {
                        FillData(set3)
                    }
                binding.RDR.id ->
                    if (checked) {
                        // TODO
                    }
            }
        }
    }

    fun RadioButtonInit(current: Long?) {
        if (current != null) {
            when(current.toInt()) {
                1 -> binding.RD1.isChecked = true
                2 -> binding.RD2.isChecked = true
                3 -> binding.RD3.isChecked = true
                4 -> binding.RDR.isChecked = true
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