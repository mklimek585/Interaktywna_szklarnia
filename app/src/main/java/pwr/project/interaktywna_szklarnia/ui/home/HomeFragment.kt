package pwr.project.interaktywna_szklarnia.ui.home

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import pwr.project.interaktywna_szklarnia.Measurement
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.FragmentHomeBinding
import pwr.project.interaktywna_szklarnia.ui.dashboard.DashboardViewModel
import pwr.project.interaktywna_szklarnia.ui.settings.SettingsViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import pwr.project.interaktywna_szklarnia.Workstation


class HomeFragment : Fragment() {

    val TAG = "HomeF"
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

    private val barWidthValue = 0.6f
    private val lightMax = 1200f

    private lateinit var viewModel: HomeViewModel

    // Wektory z progami z bazy danych:
    var currentSet = arrayOf(40,60,50,65,75,23) // Aktualne progi
    // (wilgotnosc1, swiatlo1, wilgotnosc2, swiatlo2, sloneczne, temp szklarni)
    var values = arrayOf(80,60,40,70,90,30) // Aktualne wartosc

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java) // Init

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val Sun = "#f4cb58"
        val Water = "#497dcb"
        val Temp = "#f8bb97"


        val decimalFormat = DecimalFormat("###'%'")

        fun UpdateWk1UI() {
            barChartWater = binding.bcWATER
            val entriesWater = arrayListOf(BarEntry(1f, values[0].toFloat()))
            val dataSetWater = BarDataSet(entriesWater, "")
            dataSetWater.setDrawValues(false)

            val barDataWater = BarData(dataSetWater)
            dataSetWater.color = Color.parseColor(Water)
            barDataWater.barWidth = barWidthValue

            barChartWater.data = barDataWater
            barChartWater.description.isEnabled = false
            barChartWater.legend.isEnabled = false
            barChartWater.axisLeft.isEnabled = true
            barChartWater.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
            barChartWater.axisLeft.axisMinimum = 0f // minimum osi Y
            barChartWater.axisLeft.axisMaximum = 100f // maksimum osi Y
            barChartWater.axisLeft.valueFormatter = PercentFormatter() // formatowanie wartości osi Y

            barChartWater.axisRight.isEnabled = false
            barChartWater.xAxis.isEnabled = false
            barChartWater.setDrawGridBackground(false)
            barChartWater.setFitBars(false)
            barChartWater.setTouchEnabled(false)
            barChartWater.isDragEnabled = false
            barChartWater.setScaleEnabled(true)
            barChartWater.animateY(1000)

            barChartWater.axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return decimalFormat.format(value)
                }
            }

            // Dodaj linię siatki - próg
            val threshold1 = currentSet[0].toFloat()
            val yAxis1 = barChartWater.axisLeft
            val gridLine1 = LimitLine(threshold1, "")
            gridLine1.lineWidth = 2f
            gridLine1.lineColor = Color.RED
            yAxis1.addLimitLine(gridLine1)

            barChartWater.invalidate() // Odśwież wykres
////////////////////////////////////////////////////////////////////////////
            barChartSun = binding.bcSUN
            val entriesSUN = arrayListOf(BarEntry(1f, values[1].toFloat()))
            val dataSetSUN = BarDataSet(entriesSUN, "")
            dataSetSUN.setDrawValues(false)

            val barDataSUN = BarData(dataSetSUN)
            dataSetSUN.color = Color.parseColor(Sun)
            barDataSUN.barWidth = barWidthValue

            barChartSun.data = barDataSUN
            barChartSun.description.isEnabled = false
            barChartSun.legend.isEnabled = false
            barChartSun.axisLeft.isEnabled = true
            barChartSun.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
            barChartSun.axisLeft.axisMinimum = 0f // minimum osi Y
            barChartSun.axisLeft.axisMaximum = lightMax // maksimum osi Y
            barChartSun.axisLeft.valueFormatter = LuxFormatter() // formatowanie wartości osi Y

            barChartSun.axisRight.isEnabled = false
            barChartSun.xAxis.isEnabled = false
            barChartSun.setDrawGridBackground(false)
            barChartSun.setFitBars(true)
            barChartSun.setTouchEnabled(false)
            barChartSun.isDragEnabled = false
            barChartSun.setScaleEnabled(true)
            barChartSun.animateY(1000)

            // Dodaj linię siatki - próg
            val threshold6 = currentSet[4].toFloat()
            val yAxis6 = barChartSun.axisLeft
            val gridLine6 = LimitLine(threshold6, "")
            gridLine6.lineWidth = 2f
            gridLine6.lineColor = Color.RED
            yAxis6.addLimitLine(gridLine6)

            barChartSun.invalidate() // Odśwież wykres
        }

        fun UpdateWk2UI() {
            barChartWater2 = binding.bcWATER2
            val entriesWater2 = arrayListOf(BarEntry(1f, values[2].toFloat()))
            val dataSetWater2 = BarDataSet(entriesWater2, "")
            dataSetWater2.setDrawValues(false)

            val barDataWater2 = BarData(dataSetWater2)
            dataSetWater2.color = Color.parseColor(Water)
            barDataWater2.barWidth = barWidthValue

            barChartWater2.data = barDataWater2
            barChartWater2.description.isEnabled = false
            barChartWater2.legend.isEnabled = false
            barChartWater2.axisLeft.isEnabled = true
            barChartWater2.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
            barChartWater2.axisLeft.axisMinimum = 0f // minimum osi Y
            barChartWater2.axisLeft.axisMaximum = 100f // maksimum osi Y
            barChartWater2.axisLeft.valueFormatter = PercentFormatter() // formatowanie wartości osi Y

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

            // Dodaj linię siatki - próg
            val threshold3 = currentSet[2].toFloat()
            val yAxis3 = barChartWater2.axisLeft
            val gridLine3 = LimitLine(threshold3, "")
            gridLine3.lineWidth = 2f
            gridLine3.lineColor = Color.RED
            yAxis3.addLimitLine(gridLine3)

            barChartWater2.invalidate() // Odśwież wykres

////////////////////////////////////////////////////////////////////////////
            barChartSun2 = binding.bcSUN2
            val entriesSUN2 = arrayListOf(BarEntry(1f, values[3].toFloat()))
            val dataSetSUN2 = BarDataSet(entriesSUN2, "")
            dataSetSUN2.setDrawValues(false)

            val barDataSUN2 = BarData(dataSetSUN2)
            dataSetSUN2.color = Color.parseColor(Sun)
            barDataSUN2.barWidth = barWidthValue

            barChartSun2.data = barDataSUN2
            barChartSun2.description.isEnabled = false
            barChartSun2.legend.isEnabled = false
            barChartSun2.axisLeft.isEnabled = true
            barChartSun2.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
            barChartSun2.axisLeft.axisMinimum = 0f // minimum osi Y
            barChartSun2.axisLeft.axisMaximum = lightMax // maksimum osi Y
            barChartSun2.axisLeft.valueFormatter = LuxFormatter() // formatowanie wartości osi Y

            barChartSun2.axisRight.isEnabled = false
            barChartSun2.xAxis.isEnabled = false
            barChartSun2.setDrawGridBackground(false)
            barChartSun2.setFitBars(true)
            barChartSun2.setTouchEnabled(false)
            barChartSun2.isDragEnabled = false
            barChartSun2.setScaleEnabled(true)
            barChartSun2.animateY(1000)

            // Dodaj linię siatki - próg
            val threshold4 = currentSet[3].toFloat()
            val yAxis4 = barChartSun2.axisLeft
            val gridLine4 = LimitLine(threshold4, "")
            gridLine4.lineWidth = 2f
            gridLine4.lineColor = Color.RED
            yAxis4.addLimitLine(gridLine4)

            barChartSun2.invalidate() // Odśwież wykres
        }

        fun UpdateGeneralUI() {
            barChartTemp = binding.bcTEMP
            val entriesTemp = arrayListOf(BarEntry(1f, values[5].toFloat()))
            val dataSetTemp = BarDataSet(entriesTemp, "")
            //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
            dataSetTemp.setDrawValues(false)

            val barDataTemp = BarData(dataSetTemp)
            dataSetTemp.color = Color.parseColor(Temp)
//        dataSet.color = Color.BLUE
            barDataTemp.barWidth = barWidthValue

            barChartTemp.data = barDataTemp
            barChartTemp.description.isEnabled = false
            barChartTemp.legend.isEnabled = false
            barChartTemp.axisLeft.isEnabled = true
            barChartTemp.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
            barChartTemp.axisLeft.axisMinimum = 0f // minimum osi Y
            barChartTemp.axisLeft.axisMaximum = 50f // maksimum osi Y
            barChartTemp.axisLeft.valueFormatter = CelsiusFormatter() // formatowanie wartości osi Y

            barChartTemp.axisRight.isEnabled = false
            barChartTemp.xAxis.isEnabled = false
            barChartTemp.setDrawGridBackground(false)
            barChartTemp.setFitBars(true)
            barChartTemp.setTouchEnabled(false)
            barChartTemp.isDragEnabled = false
            barChartTemp.setScaleEnabled(true)
            barChartTemp.animateY(1000)


//        barChartTemp.axisLeft.valueFormatter = object : ValueFormatter() {
//            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                return decimalFormat.format(value)
//            }
//        }

            // Dodaj linię siatki - próg
            val threshold5 = currentSet[5].toFloat()
            val yAxis5 = barChartTemp.axisLeft
            val gridLine5 = LimitLine(threshold5, "")
            gridLine5.lineWidth = 2f
            gridLine5.lineColor = Color.RED
            yAxis5.addLimitLine(gridLine5)

            barChartTemp.invalidate() // Odśwież wykres

////////////////////////////////////////////////////////////////////////////
            barChartSun3 = binding.bcSUN3
            val entriesSUN3 = arrayListOf(BarEntry(1f, values[4].toFloat()))
            val dataSetSUN3 = BarDataSet(entriesSUN3, "")
            dataSetSUN3.setDrawValues(false)

            val barDataSUN3 = BarData(dataSetSUN3)
            dataSetSUN3.color = Color.parseColor(Sun)
            barDataSUN3.barWidth = barWidthValue

            barChartSun3.data = barDataSUN3
            barChartSun3.description.isEnabled = false
            barChartSun3.legend.isEnabled = false
            barChartSun3.axisLeft.isEnabled = true
            barChartSun3.axisLeft.axisLineWidth = 2f // pogrubienie osi Y
            barChartSun3.axisLeft.axisMinimum = 0f // minimum osi Y
            barChartSun3.axisLeft.axisMaximum = lightMax // maksimum osi Y
            barChartSun3.axisLeft.valueFormatter = LuxFormatter() // formatowanie wartości osi Y

            barChartSun3.axisRight.isEnabled = false
            barChartSun3.xAxis.isEnabled = false
            barChartSun3.setDrawGridBackground(false)
            barChartSun3.setFitBars(true)
            barChartSun3.setTouchEnabled(false)
            barChartSun3.isDragEnabled = false
            barChartSun3.setScaleEnabled(true)
            barChartSun3.animateY(1000)

            // Dodaj linię siatki - próg
            val threshold6 = currentSet[4].toFloat()
            val yAxis6 = barChartSun3.axisLeft
            val gridLine6 = LimitLine(threshold6, "")
            gridLine6.lineWidth = 2f
            gridLine6.lineColor = Color.RED
            yAxis6.addLimitLine(gridLine6)

            barChartSun3.invalidate() // Odśwież wykres
        }


        // Obserwowanie LiveData
        viewModel.currentSet.observe(viewLifecycleOwner, Observer { setNumber ->
            Log.i(TAG, "Threshold set currently in use: $setNumber")
            viewModel.loadSet(setNumber, object : HomeViewModel.DataCallback {
                override fun onDataLoaded(data: Array<Int>) {
                    for (value in data) {
                        Log.i(TAG, "Value: $value")
                    }
                    // TODO wywołanie funkcji aktualizującej UI
                    UpdateWk1UI()
                    UpdateWk2UI()
                    UpdateGeneralUI()
                    // Tutaj możesz użyć danych z zestawu, np. aktualizując UI
                    //updateUIBasedOnCurrentSet(data)
                }
            })
        })

        viewModel.currentMes.observe(viewLifecycleOwner, Observer { json ->
            val gson = Gson()
            try {
                val measurement = gson.fromJson(json, Measurement::class.java)
                values[4] = measurement.lux
                values[5] = measurement.temp
                Log.i(TAG, "General Temp: ${measurement.temp}, Lux: ${measurement.lux}")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        })


        viewModel.currentWk1Mes.observe(viewLifecycleOwner, Observer { json ->
            val gson = Gson()
            try {
                val workstation1 = gson.fromJson(json, Workstation::class.java)
                values[0] = workstation1.Humidity
                values[1] = workstation1.Light
                Log.i(TAG, "Wk1 humidity: ${workstation1.Humidity}, Light: ${workstation1.Light}")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        })


        viewModel.currentWk2Mes.observe(viewLifecycleOwner, Observer { json ->
            val gson2 = Gson()
            try {
                val workstation2 = gson2.fromJson(json, Workstation::class.java)
                values[2] = workstation2.Humidity
                values[3] = workstation2.Light
                Log.i(TAG, "Wk2 humidity: ${workstation2.Humidity}, Light: ${workstation2.Light}")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        })

        Log.i(TAG, "Values Array: ${values[0]}, ${values[1]}, ${values[2]}, ${values[3]}, ${values[4]}, ${values[5]}")


        return root
    }

    class PercentFormatter : ValueFormatter(), IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return "${value.toInt()}%"
        }
    }

    class CelsiusFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            // Tutaj wykonaj konwersję wartości na stopnie Celsjusza i zwróć sformatowany tekst
            val celsiusValue = convertToCelsius(value)
            return "${celsiusValue}°C"
        }

        // Metoda do konwersji wartości na stopnie Celsjusza
        private fun convertToCelsius(value: Float): Int {
            // Tu wykonaj konwersję wartości, np. z wartości numerycznej na stopnie Celsjusza
            return value.toInt()
        }
    }

    class LuxFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            // Tutaj wykonaj konwersję wartości na stopnie Celsjusza i zwróć sformatowany tekst
            val luxValue = convertToLux(value)
            return "${luxValue}lx"
        }

        // Metoda do konwersji wartości na stopnie Celsjusza
        private fun convertToLux(value: Float): Int {
            // Tu wykonaj konwersję wartości, np. z wartości numerycznej na stopnie Celsjusza
            return value.toInt()
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}