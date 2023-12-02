package pwr.project.interaktywna_szklarnia.ui.stats

import android.graphics.Color
import android.os.Bundle
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

        /** Do wstawienia dane zaciagniete z bazy **/
        // Wilogtnosc stanowisko 1 i 2
        val dataArrayWat1 = arrayListOf(26.0, 25.1, 21.5, 22.3, 24.8, 25.9, 26)
        val dataArrayWat2 = arrayListOf(23.0, 24.1, 22.5, 24.3, 22.8, 23.9, 24)
        // Natezenie swiatla stanowisko 1 i 2
        val dataArrayLux1 = arrayListOf(300.0, 270.1, 240.5, 250.3, 260.8, 270.9, 290)
        val dataArrayLux2 = arrayListOf(330.0, 300.1, 290.5, 270.3, 310.8, 300.9, 315)
        // Temperatura stanowisko 1 i 2
        val dataArrayTemp1 = arrayListOf(26.0, 25.1, 28.5, 30.3, 32.8, 30.9, 31)
        val dataArrayTemp2 = arrayListOf(25.2, 24.5, 26.5, 25.2, 28.8, 27.5, 26.2)

        val data = ArrayList<StatsViewModel.DataModel>()

        data.add(StatsViewModel.DataModel(
                "Wilgotność",
                "Stanowisko 1",
                mapOf(
                    (1 to dataArrayWat1[0]) as Pair<Int, Double>,
                    (2 to dataArrayWat1[1]) as Pair<Int, Double>,
                    (3 to dataArrayWat1[2]) as Pair<Int, Double>,
                    (4 to dataArrayWat1[3]) as Pair<Int, Double>,
                    (5 to dataArrayWat1[4]) as Pair<Int, Double>,
                    (6 to dataArrayWat1[5]) as Pair<Int, Double>,
                    (7 to dataArrayWat1[6]) as Pair<Int, Double>
                ), Color.BLUE,
                "Stanowisko 2",
                mapOf(
                    (1 to dataArrayWat2[0]) as Pair<Int, Double>,
                    (2 to dataArrayWat2[1]) as Pair<Int, Double>,
                    (3 to dataArrayWat2[2]) as Pair<Int, Double>,
                    (4 to dataArrayWat2[3]) as Pair<Int, Double>,
                    (5 to dataArrayWat2[4]) as Pair<Int, Double>,
                    (6 to dataArrayWat2[5]) as Pair<Int, Double>,
                    (7 to dataArrayWat2[6]) as Pair<Int, Double>
                ), Color.RED
            )
        )
        data.add(StatsViewModel.DataModel("Natężenie światła",
            "Stanowisko 1",
            mapOf((1 to dataArrayLux1[0]) as Pair<Int, Double>,
                (2 to dataArrayLux1[1]) as Pair<Int, Double>,
                (3 to dataArrayLux1[2]) as Pair<Int, Double>,
                (4 to dataArrayLux1[3]) as Pair<Int, Double>,
                (5 to dataArrayLux1[4]) as Pair<Int, Double>,
                (6 to dataArrayLux1[5]) as Pair<Int, Double>,
                (7 to dataArrayLux1[6]) as Pair<Int, Double>
            ), Color.BLUE,
            "Stanowisko 2",
            mapOf((1 to dataArrayLux2[0]) as Pair<Int, Double>,
                (2 to dataArrayLux2[1]) as Pair<Int, Double>,
                (3 to dataArrayLux2[2]) as Pair<Int, Double>,
                (4 to dataArrayLux2[3]) as Pair<Int, Double>,
                (5 to dataArrayLux2[4]) as Pair<Int, Double>,
                (6 to dataArrayLux2[5]) as Pair<Int, Double>,
                (7 to dataArrayLux2[6]) as Pair<Int, Double>
            ), Color.RED
        ))
        data.add(StatsViewModel.DataModel("Temperatura",
            "Stanowisko 1",
            mapOf((1 to dataArrayTemp1[0]) as Pair<Int, Double>,
                (2 to dataArrayTemp1[1]) as Pair<Int, Double>,
                (3 to dataArrayTemp1[2]) as Pair<Int, Double>,
                (4 to dataArrayTemp1[3]) as Pair<Int, Double>,
                (5 to dataArrayTemp1[4]) as Pair<Int, Double>,
                (6 to dataArrayTemp1[5]) as Pair<Int, Double>,
                (7 to dataArrayTemp1[6]) as Pair<Int, Double>
            ), Color.BLUE,
            "Stanowisko 2",
            mapOf((1 to dataArrayTemp2[0]) as Pair<Int, Double>,
                (2 to dataArrayTemp2[1]) as Pair<Int, Double>,
                (3 to dataArrayTemp2[2]) as Pair<Int, Double>,
                (4 to dataArrayTemp2[3]) as Pair<Int, Double>,
                (5 to dataArrayTemp2[4]) as Pair<Int, Double>,
                (6 to dataArrayTemp2[5]) as Pair<Int, Double>,
                (7 to dataArrayTemp2[6]) as Pair<Int, Double>
            ), Color.RED
        ))

        val adapter = StatsAdapter(requireContext(), data)
        listView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// TODO Zmiana 1-7 na dane dni tygodnia
// TODO dodanie możliwości zmiany wyświetlania zakresy wykresu w ustaweiniach np z tygodnia na miesiac

