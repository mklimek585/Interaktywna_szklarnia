package pwr.project.interaktywna_szklarnia.ui.home

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import pwr.project.interaktywna_szklarnia.Measurement
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import pwr.project.interaktywna_szklarnia.Workstation


class HomeFragment : Fragment() {
    val TAG = "HomeF"
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var barChartWater: BarChart
    private lateinit var barChartSun: BarChart
    private lateinit var barChartWater2: BarChart
    private lateinit var barChartSun2: BarChart
    private lateinit var barChartTemp: BarChart
    private lateinit var barChartSun3: BarChart

    private val barWidthValue = 0.6f
    private var lightMax = 1000f
    val decimalFormat = DecimalFormat("###'%'")

    val Sun = "#f4cb58"
    val Water = "#497dcb"
    val Temp = "#f8bb97"
    // Thresholds
    var currentSet = arrayOf(40,60,50,65,75,23)
    var values = arrayOf(80,60,40,70,90,30) // (wilgotnosc1, swiatlo1, wilgotnosc2, swiatlo2, sloneczne, temp szklarni)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(R.attr.statsAxisColor, typedValue, true)
        val axisColor = typedValue.data

        theme.resolveAttribute(R.attr.statsTextColor, typedValue, true)
        val textColor = typedValue.data

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java) // Init
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        barChartWater = binding.bcWATER
        barChartSun = binding.bcSUN
        barChartWater2 = binding.bcWATER2
        barChartSun2 = binding.bcSUN2
        barChartTemp = binding.bcTEMP
        barChartSun3 = binding.bcSUN3

        // LiveData
        viewModel.currentSet.observe(viewLifecycleOwner, Observer { setNumber ->
            Log.i(TAG, "Threshold set currently in use: $setNumber")
            viewModel.loadSet(setNumber, object : HomeViewModel.DataCallback {
                override fun onDataLoaded(data: Array<Int>) {
                    currentSet[4] = data[0] // sun gen
                    currentSet[5] = data[1] // temp
                    currentSet[0] = data[2] // wk1 hum
                    currentSet[1] = data[3] // wk1 temp
                    currentSet[2] = data[4] // wk2 hum
                    currentSet[3] = data[5] // wk2 temp
                    for (value in data) { Log.i(TAG, "Value: $value") }
                    for (value in currentSet) { Log.i(TAG, "CurrentSet: $value") }
                    // Update UI
                    UpdateWk1UI(axisColor, textColor)
                    UpdateWk2UI(axisColor, textColor)
                    UpdateGeneralUI(axisColor, textColor)
                }
            })
        })

        //TODO zrobienie by aktualne pomiary faktycznie sie zmienialy live
        viewModel.currentMes.observe(viewLifecycleOwner, Observer { json ->
            val gson = Gson()
            try {
                val measurement = gson.fromJson(json, Measurement::class.java)
                values[4] = measurement.lux.toInt()
                values[5] = measurement.temp.toInt()
                Log.i(TAG, "General Temp: ${measurement.temp}, Lux: ${measurement.lux}")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        })

        viewModel.currentWk1Mes.observe(viewLifecycleOwner, Observer { json ->
            val gson = Gson()
            try {
                val workstation1 = gson.fromJson(json, Workstation::class.java)
                values[0] = workstation1.Humidity.toInt()
                values[1] = workstation1.Light.toInt()
                Log.i(TAG, "Wk1 humidity: ${workstation1.Humidity}, Light: ${workstation1.Light}")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        })

        viewModel.currentWk2Mes.observe(viewLifecycleOwner, Observer { json ->
            val gson2 = Gson()
            try {
                val workstation2 = gson2.fromJson(json, Workstation::class.java)
                values[2] = workstation2.Humidity.toInt()
                values[3] = workstation2.Light.toInt()
                Log.i(TAG, "Wk2 humidity: ${workstation2.Humidity}, Light: ${workstation2.Light}")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        })

        Log.i(TAG, "Values Array: ${values[0]}, ${values[1]}, ${values[2]}, ${values[3]}, ${values[4]}, ${values[5]}")

        return root
    }

    fun UpdateWk1UI(axisColor: Int, textColor: Int) {
        val entriesWater = arrayListOf(BarEntry(1f, values[0].toFloat()))
        val dataSetWater = BarDataSet(entriesWater, "")
        dataSetWater.setDrawValues(false)

        val barDataWater = BarData(dataSetWater)
        dataSetWater.color = Color.parseColor(Water)
        barDataWater.barWidth = barWidthValue

        barChartWater.data = barDataWater
        barChartWater.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartWater.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartWater.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartWater.axisLeft.valueFormatter = PercentFormatter() // formatowanie wartości osi Y
        // Settings
        barChartWater.description.isEnabled = false
        barChartWater.legend.isEnabled = false
        barChartWater.axisLeft.isEnabled = true
        barChartWater.axisRight.isEnabled = false
        barChartWater.xAxis.isEnabled = false
        barChartWater.setDrawGridBackground(false)
        barChartWater.setFitBars(false)
        barChartWater.setTouchEnabled(false)
        barChartWater.isDragEnabled = false
        barChartWater.setScaleEnabled(true)
        barChartWater.animateY(1000)
        // Color of axis and text defined by theme
        barChartWater.axisLeft.axisLineColor = axisColor
        barChartWater.axisRight.axisLineColor = axisColor
        barChartWater.axisLeft.textColor = textColor
        barChartWater.axisRight.textColor = textColor

        barChartWater.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String { return decimalFormat.format(value) }
        }

        // Plot threshold
        val threshold1 = currentSet[0].toFloat()
        val yAxis1 = barChartWater.axisLeft
        val gridLine1 = LimitLine(threshold1, "")
        gridLine1.lineWidth = 2f
        gridLine1.lineColor = Color.RED
        yAxis1.addLimitLine(gridLine1)

        barChartWater.invalidate() // Odśwież wykres
////////////////////////////////////////////////////////////////////////////
        val entriesSUN = arrayListOf(BarEntry(1f, values[1].toFloat()))
        val dataSetSUN = BarDataSet(entriesSUN, "")
        dataSetSUN.setDrawValues(false)

        val barDataSUN = BarData(dataSetSUN)
        dataSetSUN.color = Color.parseColor(Sun)
        barDataSUN.barWidth = barWidthValue

        barChartSun.data = barDataSUN
        barChartSun.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartSun.axisLeft.axisMinimum = 0f // minimum osi Y
        //         && (currentSet[1].toFloat() < 300) {
        if(values[1] in 1..299) {
            lightMax = 500f
        }
        else if(values[1] in 301..599) {
            lightMax = 800f
        }
        else if(values[1] in 601..999) {
            lightMax = 1200f
        }
        else if(values[1] in 1001..1699) {
            lightMax = 2000f
        }
        if(currentSet[1].toFloat() > lightMax) {
            if(currentSet[1].toFloat() >= 800) {

            } else {
                lightMax = currentSet[1].toFloat()+200 }
            }
        // TODO dla dwoch pozostalych
        // TODO konwersja jak jest np 750 to zeby rzucalo do 900 lub 1000

        barChartSun.axisLeft.axisMaximum = lightMax // maksimum osi Y
        barChartSun.axisLeft.valueFormatter = LuxFormatter() // formatowanie wartości osi Y
        // Settings
        barChartSun.description.isEnabled = false
        barChartSun.legend.isEnabled = false
        barChartSun.axisLeft.isEnabled = true
        barChartSun.axisRight.isEnabled = false
        barChartSun.xAxis.isEnabled = false
        barChartSun.setDrawGridBackground(false)
        barChartSun.setFitBars(true)
        barChartSun.setTouchEnabled(false)
        barChartSun.isDragEnabled = false
        barChartSun.setScaleEnabled(true)
        barChartSun.animateY(1000)
        // Color of axis and text defined by theme
        barChartSun.axisLeft.axisLineColor = axisColor
        barChartSun.axisRight.axisLineColor = axisColor
        barChartSun.axisLeft.textColor = textColor
        barChartSun.axisRight.textColor = textColor

        // Plot threshold
        val threshold6 = currentSet[1].toFloat()
        val yAxis6 = barChartSun.axisLeft
        val gridLine6 = LimitLine(threshold6, "")
        gridLine6.lineWidth = 2f
        gridLine6.lineColor = Color.RED
        yAxis6.addLimitLine(gridLine6)

        barChartSun.invalidate() // Odśwież wykres
    }

    fun UpdateWk2UI(axisColor: Int, textColor: Int) {
        val entriesWater2 = arrayListOf(BarEntry(1f, values[2].toFloat()))
        val dataSetWater2 = BarDataSet(entriesWater2, "")
        dataSetWater2.setDrawValues(false)

        val barDataWater2 = BarData(dataSetWater2)
        dataSetWater2.color = Color.parseColor(Water)
        barDataWater2.barWidth = barWidthValue

        barChartWater2.data = barDataWater2
        barChartWater2.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartWater2.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartWater2.axisLeft.axisMaximum = 100f // maksimum osi Y
        barChartWater2.axisLeft.valueFormatter = PercentFormatter() // formatowanie wartości osi Y
        // Settings
        barChartWater2.description.isEnabled = false
        barChartWater2.legend.isEnabled = false
        barChartWater2.axisLeft.isEnabled = true
        barChartWater2.axisRight.isEnabled = false
        barChartWater2.xAxis.isEnabled = false
        barChartWater2.setDrawGridBackground(false)
        barChartWater2.setFitBars(true)
        barChartWater2.setTouchEnabled(false)
        barChartWater2.isDragEnabled = false
        barChartWater2.setScaleEnabled(true)
        barChartWater2.animateY(1000)
        // Color of axis and text defined by theme
        barChartWater2.axisLeft.axisLineColor = axisColor
        barChartWater2.axisRight.axisLineColor = axisColor
        barChartWater2.axisLeft.textColor = textColor
        barChartWater2.axisRight.textColor = textColor

        barChartWater2.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String { return decimalFormat.format(value) }
        }

        // Plot threshold
        val threshold3 = currentSet[2].toFloat()
        val yAxis3 = barChartWater2.axisLeft
        val gridLine3 = LimitLine(threshold3, "")
        gridLine3.lineWidth = 2f
        gridLine3.lineColor = Color.RED
        yAxis3.addLimitLine(gridLine3)

        barChartWater2.invalidate() // Odśwież wykres

////////////////////////////////////////////////////////////////////////////
        // TODO jak sa wieksze dane na wejsciu to sie nie buguje if dostosowujacy LightMax w zaleznosci od wartosci
        val entriesSUN2 = arrayListOf(BarEntry(1f, values[3].toFloat()))
        val dataSetSUN2 = BarDataSet(entriesSUN2, "")
        dataSetSUN2.setDrawValues(false)

        val barDataSUN2 = BarData(dataSetSUN2)
        dataSetSUN2.color = Color.parseColor(Sun)
        barDataSUN2.barWidth = barWidthValue

        barChartSun2.data = barDataSUN2
        barChartSun2.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartSun2.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartSun2.axisLeft.axisMaximum = lightMax // maksimum osi Y
        barChartSun2.axisLeft.valueFormatter = LuxFormatter() // formatowanie wartości osi Y
        // Settings
        barChartSun2.description.isEnabled = false
        barChartSun2.legend.isEnabled = false
        barChartSun2.axisLeft.isEnabled = true
        barChartSun2.axisRight.isEnabled = false
        barChartSun2.xAxis.isEnabled = false
        barChartSun2.setDrawGridBackground(false)
        barChartSun2.setFitBars(true)
        barChartSun2.setTouchEnabled(false)
        barChartSun2.isDragEnabled = false
        barChartSun2.setScaleEnabled(true)
        barChartSun2.animateY(1000)
        // Color of axis and text defined by theme
        barChartSun2.axisLeft.axisLineColor = axisColor
        barChartSun2.axisRight.axisLineColor = axisColor
        barChartSun2.axisLeft.textColor = textColor
        barChartSun2.axisRight.textColor = textColor

        // Plot threshold
        val threshold4 = currentSet[3].toFloat()
        val yAxis4 = barChartSun2.axisLeft
        val gridLine4 = LimitLine(threshold4, "")
        gridLine4.lineWidth = 2f
        gridLine4.lineColor = Color.RED
        yAxis4.addLimitLine(gridLine4)

        barChartSun2.invalidate() // Odśwież wykres
    }

    fun UpdateGeneralUI(axisColor: Int, textColor: Int) {
        val entriesTemp = arrayListOf(BarEntry(1f, values[5].toFloat()))
        val dataSetTemp = BarDataSet(entriesTemp, "")
        dataSetTemp.setDrawValues(false)

        val barDataTemp = BarData(dataSetTemp)
        dataSetTemp.color = Color.parseColor(Temp)
        barDataTemp.barWidth = barWidthValue

        barChartTemp.data = barDataTemp
        barChartTemp.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartTemp.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartTemp.axisLeft.axisMaximum = 50f // maksimum osi Y
        barChartTemp.axisLeft.valueFormatter = CelsiusFormatter() // formatowanie wartości osi Y
        // Settings
        barChartTemp.description.isEnabled = false
        barChartTemp.legend.isEnabled = false
        barChartTemp.axisLeft.isEnabled = true
        barChartTemp.axisRight.isEnabled = false
        barChartTemp.xAxis.isEnabled = false
        barChartTemp.setDrawGridBackground(false)
        barChartTemp.setFitBars(true)
        barChartTemp.setTouchEnabled(false)
        barChartTemp.isDragEnabled = false
        barChartTemp.setScaleEnabled(true)
        barChartTemp.animateY(1000)
        // Color of axis and text defined by theme
        barChartTemp.axisLeft.axisLineColor = axisColor
        barChartTemp.axisRight.axisLineColor = axisColor
        barChartTemp.axisLeft.textColor = textColor
        barChartTemp.axisRight.textColor = textColor

        // Plot threshold
        val threshold5 = currentSet[5].toFloat()
        val yAxis5 = barChartTemp.axisLeft
        val gridLine5 = LimitLine(threshold5, "")
        gridLine5.lineWidth = 2f
        gridLine5.lineColor = Color.RED
        yAxis5.addLimitLine(gridLine5)

        barChartTemp.invalidate() // Odśwież wykres

////////////////////////////////////////////////////////////////////////////
        val entriesSUN3 = arrayListOf(BarEntry(1f, values[4].toFloat()))
        val dataSetSUN3 = BarDataSet(entriesSUN3, "")
        dataSetSUN3.setDrawValues(false)

        val barDataSUN3 = BarData(dataSetSUN3)
        dataSetSUN3.color = Color.parseColor(Sun)
        barDataSUN3.barWidth = barWidthValue

        barChartSun3.data = barDataSUN3
        barChartSun3.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
        barChartSun3.axisLeft.axisMinimum = 0f // minimum osi Y
        barChartSun3.axisLeft.axisMaximum = lightMax // maksimum osi Y
        barChartSun3.axisLeft.valueFormatter = LuxFormatter() // formatowanie wartości osi Y
        // Settings
        barChartSun3.description.isEnabled = false
        barChartSun3.legend.isEnabled = false
        barChartSun3.axisLeft.isEnabled = true
        barChartSun3.axisRight.isEnabled = false
        barChartSun3.xAxis.isEnabled = false
        barChartSun3.setDrawGridBackground(false)
        barChartSun3.setFitBars(true)
        barChartSun3.setTouchEnabled(false)
        barChartSun3.isDragEnabled = false
        barChartSun3.setScaleEnabled(true)
        barChartSun3.animateY(1000)
        // Color of axis and text defined by theme
        barChartSun3.axisLeft.axisLineColor = axisColor
        barChartSun3.axisRight.axisLineColor = axisColor
        barChartSun3.axisLeft.textColor = textColor
        barChartSun3.axisRight.textColor = textColor

        // Plot threshold
        val threshold6 = currentSet[4].toFloat()
        val yAxis6 = barChartSun3.axisLeft
        val gridLine6 = LimitLine(threshold6, "")
        gridLine6.lineWidth = 2f
        gridLine6.lineColor = Color.RED
        yAxis6.addLimitLine(gridLine6)

        barChartSun3.invalidate() // Odśwież wykres
    }

    class PercentFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String { return "${value.toInt()}%" }
    }

    class CelsiusFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String { return "${value.toInt()}°C" }
    }

    class LuxFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String { return "${value.toInt()}lx" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}