package pwr.project.interaktywna_szklarnia.ui.notifications

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import pwr.project.interaktywna_szklarnia.databinding.ActivityLoginBinding
import pwr.project.interaktywna_szklarnia.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var barChart: BarChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val barChartView: View = binding.barChart
//        populateGraphData(barChartView)

        barChart = binding.barChart

        val entries = arrayListOf(BarEntry(1f, 10f))
        val dataSet = BarDataSet(entries, "Label")
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.setFitBars(true)
        barChart.invalidate()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}