package pwr.project.interaktywna_szklarnia.ui.home

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.FragmentHomeBinding
import pwr.project.interaktywna_szklarnia.ui.settings.SettingsViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var barChartWater: BarChart
    private lateinit var barChartSun: BarChart
    private lateinit var barChartWater2: BarChart
    private lateinit var barChartSun2: BarChart
    private lateinit var barChartTemp: BarChart
    private lateinit var barChartSun3: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val SunI = ContextCompat.getColor(requireContext(), R.color.Sun)
//        val Sun = Integer.toHexString(SunI)
//        val WaterI = ContextCompat.getColor(requireContext(), R.color.Water)
//        val Water = Integer.toHexString(WaterI)
//        val TempI = ContextCompat.getColor(requireContext(), R.color.Temp)
//        val Temp = Integer.toHexString(TempI)

        val Sun = "#f4cb58"
        val Water = "#497dcb"
        val Temp = "#f8bb97"

        barChartWater = binding.bcWATER
        val entriesWater = arrayListOf(BarEntry(1f, 40f))
        val dataSetWater = BarDataSet(entriesWater, "")
        //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSetWater.setDrawValues(false)

        val barDataWater = BarData(dataSetWater)
        dataSetWater.color = Color.parseColor(Water)
//        dataSet.color = Color.BLUE
        barDataWater.barWidth = 0.9f

        barChartWater.data = barDataWater
        barChartWater.description.isEnabled = false
        barChartWater.legend.isEnabled = false
        barChartWater.axisLeft.isEnabled = true
        barChartWater.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartWater.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartWater.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartWater.axisLeft.setValueFormatter(PercentFormatter()) // formatowanie wartości osi Y
        barChartWater.axisRight.isEnabled = false
        barChartWater.xAxis.isEnabled = false
        barChartWater.setDrawGridBackground(false)
        barChartWater.setFitBars(true)
        barChartWater.setTouchEnabled(false)
        barChartWater.isDragEnabled = false
        barChartWater.setScaleEnabled(true)
        barChartWater.animateY(1000)

        val decimalFormat = DecimalFormat("###'%'")
        barChartWater.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return decimalFormat.format(value)
            }
        }

// Dodaj linię siatki na poziomie 80%
        val threshold = 80f
        val yAxis = barChartWater.axisLeft
        val gridLine = LimitLine(threshold, "")
        gridLine.lineWidth = 2f
        gridLine.lineColor = Color.RED
        yAxis.addLimitLine(gridLine)

        barChartWater.invalidate() // Odśwież wykres
////////////////////////////////////////////////////////////////////////////
        barChartSun = binding.bcSUN
        val entriesSUN = arrayListOf(BarEntry(1f, 30f))
        val dataSetSUN = BarDataSet(entriesSUN, "")
        //dataSet.colors §= ColorTemplate.COLORFUL_COLORS.toList()
        dataSetSUN.setDrawValues(false)

        val barDataSUN = BarData(dataSetSUN)
        dataSetSUN.color = Color.parseColor(Sun)
//        dataSet.color = Color.BLUE
        barDataSUN.barWidth = 0.9f

        barChartSun.data = barDataSUN
        barChartSun.description.isEnabled = false
        barChartSun.legend.isEnabled = false
        barChartSun.axisLeft.isEnabled = true
        barChartSun.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartSun.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartSun.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartSun.axisLeft.setValueFormatter(PercentFormatter()) // formatowanie wartości osi Y
        barChartSun.axisRight.isEnabled = false
        barChartSun.xAxis.isEnabled = false
        barChartSun.setDrawGridBackground(false)
        barChartSun.setFitBars(true)
        barChartSun.setTouchEnabled(false)
        barChartSun.isDragEnabled = false
        barChartSun.setScaleEnabled(true)
        barChartSun.animateY(1000)

        barChartSun.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return decimalFormat.format(value)
            }
        }

        ////// ------------- ///////////

        barChartWater2 = binding.bcWATER2
        val entriesWater2 = arrayListOf(BarEntry(1f, 70f))
        val dataSetWater2 = BarDataSet(entriesWater2, "")
        //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSetWater2.setDrawValues(false)

        val barDataWater2 = BarData(dataSetWater2)
        dataSetWater2.color = Color.parseColor(Water)
//        dataSet.color = Color.BLUE
        barDataWater2.barWidth = 0.9f

        barChartWater2.data = barDataWater2
        barChartWater2.description.isEnabled = false
        barChartWater2.legend.isEnabled = false
        barChartWater2.axisLeft.isEnabled = true
        barChartWater2.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartWater2.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartWater2.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartWater2.axisLeft.setValueFormatter(PercentFormatter()) // formatowanie wartości osi Y
        barChartWater2.axisRight.isEnabled = false
        barChartWater2.xAxis.isEnabled = false
        barChartWater2.setDrawGridBackground(false)
        barChartWater2.setFitBars(true)
        barChartWater2.setTouchEnabled(false)
        barChartWater2.isDragEnabled = false
        barChartWater2.setScaleEnabled(true)
        barChartWater2.animateY(1000)

        barChartWater2.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return decimalFormat.format(value)
            }
        }
////////////////////////////////////////////////////////////////////////////
        barChartSun2 = binding.bcSUN2
        val entriesSUN2 = arrayListOf(BarEntry(1f, 30f))
        val dataSetSUN2 = BarDataSet(entriesSUN2, "")
        //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSetSUN2.setDrawValues(false)

        val barDataSUN2 = BarData(dataSetSUN2)
        dataSetSUN2.color = Color.parseColor(Sun)
//        dataSet.color = Color.BLUE
        barDataSUN2.barWidth = 0.9f

        barChartSun2.data = barDataSUN2
        barChartSun2.description.isEnabled = false
        barChartSun2.legend.isEnabled = false
        barChartSun2.axisLeft.isEnabled = true
        barChartSun2.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartSun2.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartSun2.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartSun2.axisLeft.setValueFormatter(PercentFormatter()) // formatowanie wartości osi Y
        barChartSun2.axisRight.isEnabled = false
        barChartSun2.xAxis.isEnabled = false
        barChartSun2.setDrawGridBackground(false)
        barChartSun2.setFitBars(true)
        barChartSun2.setTouchEnabled(false)
        barChartSun2.isDragEnabled = false
        barChartSun2.setScaleEnabled(true)
        barChartSun2.animateY(1000)

        barChartSun2.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return decimalFormat.format(value)
            }
        }

        ////// ------------->>>>>>>>>>>>><<<<<<<<<<<<<<------------------ ///////////

        barChartTemp = binding.bcTEMP
        val entriesTemp = arrayListOf(BarEntry(1f, 70f))
        val dataSetTemp = BarDataSet(entriesTemp, "")
        //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSetTemp.setDrawValues(false)

        val barDataTemp = BarData(dataSetTemp)
        dataSetTemp.color = Color.parseColor(Temp)
//        dataSet.color = Color.BLUE
        barDataTemp.barWidth = 0.9f

        barChartTemp.data = barDataTemp
        barChartTemp.description.isEnabled = false
        barChartTemp.legend.isEnabled = false
        barChartTemp.axisLeft.isEnabled = true
        barChartTemp.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartTemp.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartTemp.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartTemp.axisLeft.setValueFormatter(PercentFormatter()) // formatowanie wartości osi Y
        barChartTemp.axisRight.isEnabled = false
        barChartTemp.xAxis.isEnabled = false
        barChartTemp.setDrawGridBackground(false)
        barChartTemp.setFitBars(true)
        barChartTemp.setTouchEnabled(false)
        barChartTemp.isDragEnabled = false
        barChartTemp.setScaleEnabled(true)
        barChartTemp.animateY(1000)

        barChartTemp.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return decimalFormat.format(value)
            }
        }
////////////////////////////////////////////////////////////////////////////
        barChartSun3 = binding.bcSUN3
        val entriesSUN3 = arrayListOf(BarEntry(1f, 30f))
        val dataSetSUN3 = BarDataSet(entriesSUN3, "")
        //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSetSUN3.setDrawValues(false)

        val barDataSUN3 = BarData(dataSetSUN3)
        dataSetSUN3.color = Color.parseColor(Sun)
//        dataSet.color = Color.BLUE
        barDataSUN3.barWidth = 0.9f

        barChartSun3.data = barDataSUN3
        barChartSun3.description.isEnabled = false
        barChartSun3.legend.isEnabled = false
        barChartSun3.axisLeft.isEnabled = true
        barChartSun3.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartSun3.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartSun3.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartSun3.axisLeft.setValueFormatter(PercentFormatter()) // formatowanie wartości osi Y
        barChartSun3.axisRight.isEnabled = false
        barChartSun3.xAxis.isEnabled = false
        barChartSun3.setDrawGridBackground(false)
        barChartSun3.setFitBars(true)
        barChartSun3.setTouchEnabled(false)
        barChartSun3.isDragEnabled = false
        barChartSun3.setScaleEnabled(true)
        barChartSun3.animateY(1000)

        barChartSun3.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return decimalFormat.format(value)
            }
        }

        return root
    }

    class PercentFormatter : ValueFormatter(), IAxisValueFormatter {

        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return "${value.toInt()}%"
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}