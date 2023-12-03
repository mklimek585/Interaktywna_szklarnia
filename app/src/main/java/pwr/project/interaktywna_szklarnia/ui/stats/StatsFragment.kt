package pwr.project.interaktywna_szklarnia.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pwr.project.interaktywna_szklarnia.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val statsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.listView


        // Określ zakres czasowy
        val timeRange = TimeRange.DAY // lub inny zakres, zależnie od potrzeb

        statsViewModel.loadDataForTimePeriod({ rawData, isDataUpToDate ->
            rawData.forEach { dataModel ->
                Log.d("DataLog", "DataModel: $dataModel")
            }

            if(isDataUpToDate) {
                val adapter = StatsAdapter(requireContext(), rawData, timeRange)
                listView.adapter = adapter
            }
            else {
                Log.e("DataLog", "Downloaded data is not up to date")
            }
        }, timeRange)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// TODO Zmiana 1-7 na dane dni tygodnia
// TODO dodanie możliwości zmiany wyświetlania zakresy wykresu w ustaweiniach np z tygodnia na miesiac