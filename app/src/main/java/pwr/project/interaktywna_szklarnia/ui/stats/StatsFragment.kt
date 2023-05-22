package pwr.project.interaktywna_szklarnia.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import pwr.project.interaktywna_szklarnia.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var barChart: BarChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val barChartView: View = binding.barChart
//        populateGraphData(barChartView)

        barChart = binding.barChart

        val entries = arrayListOf(BarEntry(1f, 10f))
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.setDrawValues(false)

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.axisLeft.isEnabled = true
        barChart.axisRight.isEnabled = true
        barChart.xAxis.isEnabled = true
        barChart.setDrawGridBackground(false)
        barChart.setFitBars(true)
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.animateY(1000)

// Pogrubienie linii siatki dla osi Y
        barChart.axisLeft.enableGridDashedLine(10f, 10f, 0f)
        barChart.axisLeft.gridLineWidth = 2f

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}