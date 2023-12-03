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
import androidx.preference.PreferenceManager
import pwr.project.interaktywna_szklarnia.databinding.FragmentStatsBinding
import pwr.project.interaktywna_szklarnia.ui.settings.SettingsFragment

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val statsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val listView: ListView = binding.listView

        // Określ zakres czasowy
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val currentRange = sharedPref.getString(SettingsFragment.KEY_TIME_RANGE, SettingsFragment.DEFAULT_TIME_RANGE)
        val timeRange = when (currentRange) {
            "DAY" -> TimeRange.DAY // lub inny zakres, zależnie od potrzeb
            "WEEK" -> TimeRange.WEEK // lub inny zakres, zależnie od potrzeb
            else -> {
                Log.e("Settings-Stats", "There is no correct TimeRange")
                TimeRange.DAY // Domyślny zakres czasu
            }
        }


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