package pwr.project.interaktywna_szklarnia.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pwr.project.interaktywna_szklarnia.StatsAdapter
import pwr.project.interaktywna_szklarnia.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    data class DataModel(val title: String, val label1: String, val data1: Map<Int, Double>, val color1: Int,
                         val label2: String, val data2: Map<Int, Double>, val color2: Int)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val statsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.listView

        // Poniższy kod jest tylko na potrzeby przykładu. Zastąp go swoimi danymi.
        val data = ArrayList<DataModel>()

        data.add(DataModel("Wilgotność",
            "Stanowisko 1",
            mapOf(1 to 26.0, 2 to 25.1, 3 to 21.5, 4 to 22.3, 5 to 24.8, 6 to 25.9, 7 to 26.0), Color.BLUE,
            "Stanowisko 2",
            mapOf(1 to 23.0, 2 to 24.1, 3 to 22.5, 4 to 24.3, 5 to 22.8, 6 to 23.9, 7 to 24.0), Color.RED
        ))
        data.add(DataModel("Wilgotność",
            "Stanowisko 1",
            mapOf(1 to 26.0, 2 to 25.1, 3 to 21.5, 4 to 22.3, 5 to 24.8, 6 to 25.9, 7 to 26.0), Color.BLUE,
            "Stanowisko 2",
            mapOf(1 to 23.0, 2 to 24.1, 3 to 22.5, 4 to 24.3, 5 to 22.8, 6 to 23.9, 7 to 24.0), Color.RED
        ))
        data.add(DataModel("Wilgotność",
            "Stanowisko 1",
            mapOf(1 to 26.0, 2 to 25.1, 3 to 21.5, 4 to 22.3, 5 to 24.8, 6 to 25.9, 7 to 26.0), Color.BLUE,
            "Stanowisko 2",
            mapOf(1 to 23.0, 2 to 24.1, 3 to 22.5, 4 to 24.3, 5 to 22.8, 6 to 23.9, 7 to 24.0), Color.RED
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


//class StatsFragment : Fragment() {
//    private var _binding: FragmentStatsBinding? = null
//    private val binding get() = _binding!!
//
//    data class DataModel(val title: String, val label: String, val data: Map<Int, Double>, val color: Int)
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        val statsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
//
//        _binding = FragmentStatsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val listView: ListView = binding.listView
//
//        // Poniższy kod jest tylko na potrzeby przykładu. Zastąp go swoimi danymi.
//        val data = ArrayList<DataModel>()
//
//        // TODO: Zmiana DataModel na przyjmujący dwie paczki danych by wykresy były dla dwóch stanowisk
//        // TODO: Zmienne kolory dla każdej paczki danych
//        data.add(DataModel("Wilgotność","Uśredniona wiglotność", mapOf(1 to 23.0, 2 to 24.1, 3 to 22.5, 4 to 24.3, 5 to 22.8, 6 to 23.9, 7 to 24.0), Color.BLUE),)
//        data.add(DataModel("Nasłonecznienie","Uśrednione nasłonecznienie", mapOf(1 to 23.2, 2 to 24.2, 3 to 22.8, 4 to 24.2, 5 to 22.7, 6 to 23.7, 7 to 24.0), Color.parseColor("#ff681f")))
//        data.add(DataModel("Temperatura w szklarni","Uśredniona temperatura", mapOf(1 to 23.1, 2 to 24.0, 3 to 22.7, 4 to 24.4, 5 to 22.9, 6 to 23.8, 7 to 23.9), Color.RED))
//
//        val adapter = StatsAdapter(requireContext(), data)
//        listView.adapter = adapter
//
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

// TODO dodanie możliwości zmiany wyświetlania zakresy wykresu w ustaweiniach np z tygodnia na miesiac

