package pwr.project.interaktywna_szklarnia.ui.stats

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import pwr.project.interaktywna_szklarnia.R
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StatsAdapter(context: Context, private val rawData: List<StatsViewModel.DataModel>, private val timeRange: TimeRange
) : ArrayAdapter<StatsViewModel.ChartsDataModel>(context, 0) {

    private class ViewHolder {
        lateinit var chart: LineChart
        lateinit var chartTitle: TextView
    }

    var numberOfMeasurements = 0
    init {
        // Przetwarzanie danych
        val processedData = processData(rawData, timeRange)
        addAll(processedData)

        // Ustawienie liczby pomiarów
        numberOfMeasurements = rawData.size
    }

        private fun processData(rawData: List<StatsViewModel.DataModel>, timeRange: TimeRange): List<StatsViewModel.ChartsDataModel> {
            val generalData = ArrayList<StatsViewModel.ChartsDataModel>()

            if(timeRange == TimeRange.DAY) {
                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Wilgotność",
                    label1 = "Stanowisko 1 - [%]",
                    data1 = rawData.mapIndexed { index, data -> index to (data.humWk1_avg ?: 0.0) }.toMap(),
                    color1 = Color.BLUE,
                    label2 = "Stanowisko 2 - [%]",
                    data2 = rawData.mapIndexed { index, data -> index to (data.humWk2_avg ?: 0.0) }.toMap(),
                    color2 = Color.RED
                ))

                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Natężenie światła",
                    label1 = "Stanowisko 1 - [lx]",
                    data1 = rawData.mapIndexed { index, data -> index to (data.lightWk1_avg ?: 0.0) }.toMap(),
                    color1 = Color.BLUE,
                    label2 = "Stanowisko 2 - [lx]",
                    data2 = rawData.mapIndexed { index, data -> index to (data.lightWk2_avg ?: 0.0) }.toMap(),
                    color2 = Color.RED
                ))

                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Temperatura",
                    label1 = "Temperatura - [℃]",
                    data1 = rawData.mapIndexed { index, data -> index to (data.temp_avg ?: 0.0) }.toMap(),
                    color1 = Color.MAGENTA,
                    label2 = "Stanowisko 2",
                    data2 = rawData.mapIndexed { index, data -> index to (data.temp_avg ?: 0.0) }.toMap(),
                    color2 = Color.CYAN
                ))

                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Światło słoneczne",
                    label1 = "Natężenie światła - [lx]",
                    data1 = rawData.mapIndexed { index, data -> index to (data.lux_avg ?: 0.0) }.toMap(),
                    color1 = Color.CYAN,
                    label2 = "Stanowisko 2",
                    data2 = rawData.mapIndexed { index, data -> index to (data.temp_avg ?: 0.0) }.toMap(),
                    color2 = Color.CYAN
                ))
            }

            if(timeRange == TimeRange.WEEK) {
                val weekHumidityData = prepareWeekHumidityData(rawData)
                val humidityData1 = mutableMapOf<Int, Double>()
                val humidityData2 = mutableMapOf<Int, Double>()

                weekHumidityData.forEachIndexed { index, dayData ->
                    val dayIndex = index * 24 // Przykład, gdzie każdy dzień ma 24 pomiary
                    humidityData1[dayIndex] = dayData.second.first
                    humidityData2[dayIndex] = dayData.second.second
                }
                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Wilgotność w tygodniu",
                    label1 = "Stanowisko 1 - [%]",
                    data1 = humidityData1,
                    color1 = Color.BLUE,
                    label2 = "Stanowisko 2 - [%]",
                    data2 = humidityData2,
                    color2 = Color.RED
                ))

                val weekLightData = prepareWeekLightxData(rawData)
                val lightData1 = mutableMapOf<Int, Double>()
                val lightData2 = mutableMapOf<Int, Double>()

                weekLightData.forEachIndexed { index, dayData ->
                    val dayIndex = index * 24 // Przykład, gdzie każdy dzień ma 24 pomiary
                    lightData1[dayIndex] = dayData.second.first
                    lightData2[dayIndex] = dayData.second.second
                }

                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Natężenie światła",
                    label1 = "Stanowisko 1 - [lx]",
                    data1 = lightData1,
                    color1 = Color.BLUE,
                    label2 = "Stanowisko 2 - [lx]",
                    data2 = lightData2,
                    color2 = Color.RED
                ))

                val weekTempData = prepareWeekTempData(rawData)
                val tempData = mutableMapOf<Int, Double>()

                weekTempData.forEachIndexed { index, dayData ->
                    val dayIndex = index * 24 // Przykład, gdzie każdy dzień ma 24 pomiary
                    tempData[dayIndex] = dayData.second
                }

                generalData.add(StatsViewModel.ChartsDataModel(
                    title = "Temperatura",
                    label1 = "Temperatura - [℃]",
                    data1 = tempData,
                    color1 = Color.MAGENTA,
                    label2 = "Temperatura - [℃]",
                    data2 = tempData,
                    color2 = Color.MAGENTA
                ))
            }

            val weekLuxData = prepareWeekLuxData(rawData)
            val luxData = mutableMapOf<Int, Double>()

            weekLuxData.forEachIndexed { index, dayData ->
                val dayIndex = index * 24 // Przykład, gdzie każdy dzień ma 24 pomiary
                luxData[dayIndex] = dayData.second
            }

            generalData.add(StatsViewModel.ChartsDataModel(
                title = "Światło słoneczne",
                label1 = "Natężenie światła - [lx]",
                data1 = luxData,
                color1 = Color.CYAN,
                label2 = "Natężenie światła - [lx]",
                data2 = luxData,
                color2 = Color.CYAN
            ))

            return generalData
        }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder.chart = view.findViewById(R.id.chart)
            viewHolder.chartTitle = view.findViewById(R.id.chart_title)
            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        val dataModel = getItem(position)
        val lineData = LineData()

        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.statsAxisColor, typedValue, true)
        val axisColor = typedValue.data

        theme.resolveAttribute(R.attr.statsTextColor, typedValue, true)
        val textColor = typedValue.data

        // Color from theme
        val xAxis = viewHolder.chart.xAxis
        val yAxis = viewHolder.chart.axisLeft
        xAxis.axisLineColor = axisColor
        yAxis.axisLineColor = axisColor
        xAxis.textColor = textColor
        yAxis.textColor = textColor
        val legend = viewHolder.chart.legend
        legend.textColor = textColor

        if (dataModel != null) {
            val entries1 = ArrayList<Entry>()
            for ((x, y) in dataModel.data1) {
                entries1.add(Entry(x.toFloat(), y.toFloat()))
            }
            val lineDataSet1 = LineDataSet(entries1, dataModel.label1)
            lineDataSet1.color = dataModel.color1
            lineData.addDataSet(lineDataSet1)

            // Sprawdź tytuł wykresu, aby zdecydować, czy dodać drugi zestaw danych
            if (dataModel.title != "Światło słoneczne" && dataModel.title != "Temperatura") {
                val entries2 = ArrayList<Entry>()
                for ((x, y) in dataModel.data2) {
                    entries2.add(Entry(x.toFloat(), y.toFloat()))
                }
                val lineDataSet2 = LineDataSet(entries2, dataModel.label2)
                lineDataSet2.color = dataModel.color2
                lineData.addDataSet(lineDataSet2)
            }
        }


        viewHolder.chart.data = lineData
        viewHolder.chartTitle.text = dataModel?.title ?: ""

        configureXAxis(xAxis, timeRange)

        yAxis.setDrawLabels(true)
        yAxis.setDrawAxisLine(true)
        yAxis.axisLineWidth = 2f
        yAxis.gridLineWidth = 1f

        val rightAxis = viewHolder.chart.axisRight
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawAxisLine(false)

        viewHolder.chart.description.isEnabled = false

        viewHolder.chart.invalidate()

        return view
    }

    private fun configureXAxis(xAxis: XAxis, timeRange: TimeRange) {
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(true)
        xAxis.axisLineWidth = 2f
        xAxis.setDrawGridLines(false)

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (timeRange) {
                    TimeRange.DAY -> {
                        val labels = mutableListOf<String>()
                        val now = LocalDateTime.now()
                        var currentHour = now.hour

                        // Tworzenie etykiet zaczynając od ostatniej pełnej godziny i idąc wstecz
                        for (i in 0 until numberOfMeasurements) {
                            var hourLabel = currentHour - i
                            if (hourLabel <= 0) hourLabel += 24  // Korekta dla godzin mniejszych niż 0
                            labels.add(0, "$hourLabel:00")  // Dodawanie etykiet w odwrotnej kolejności
                        }

                        labels.getOrElse(value.toInt()) { "" }
                    }
                    TimeRange.WEEK -> {
                        val now = LocalDateTime.now()
                        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
                        val measurementDateTime = now.minusDays(numberOfMeasurements.toLong() - value.toLong())
                        measurementDateTime.format(dayOfWeekFormatter)
                    }
                    TimeRange.MONTH -> {
                        val dayOfMonth = value.toInt() // Zakładając, że wartości X to dni miesiąca
                        dayOfMonth.toString()
                    }
                    else -> super.getFormattedValue(value)
                }
            }
        }

    }


    fun prepareWeekHumidityData(rawData: List<StatsViewModel.DataModel>): List<Pair<String, Pair<Double, Double>>> {
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val preparedData = mutableListOf<Pair<String, Pair<Double, Double>>>()

        val noonIndex = rawData.indexOfFirst {
            val hourDifference = Duration.between(LocalDateTime.now().withHour(12), now).toHours()
            it == rawData.getOrNull(rawData.size - hourDifference.toInt())
        }

        if (noonIndex != -1) {
            for (i in noonIndex downTo 0 step 24) {
                val dayLabel = now.minusDays((noonIndex - i) / 24L).format(dayOfWeekFormatter)
                val humidityWk1 = rawData.getOrNull(i)?.humWk1_avg ?: 0.0
                val humidityWk2 = rawData.getOrNull(i)?.humWk2_avg ?: 0.0
                preparedData.add(0, Pair(dayLabel, Pair(humidityWk1, humidityWk2)))
            }
        }

        while (preparedData.size < 7) {
            val dayLabel = now.minusDays(preparedData.size.toLong()).format(dayOfWeekFormatter)
            preparedData.add(0, Pair(dayLabel, Pair(0.0, 0.0)))
        }

        return preparedData
    }

    fun prepareWeekLightxData(rawData: List<StatsViewModel.DataModel>): List<Pair<String, Pair<Double, Double>>> {
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val preparedData = mutableListOf<Pair<String, Pair<Double, Double>>>()

        val noonIndex = rawData.indexOfFirst {
            val hourDifference = Duration.between(LocalDateTime.now().withHour(12), now).toHours()
            it == rawData.getOrNull(rawData.size - hourDifference.toInt())
        }

        if (noonIndex != -1) {
            for (i in noonIndex downTo 0 step 24) {
                val dayLabel = now.minusDays((noonIndex - i) / 24L).format(dayOfWeekFormatter)
                val humidityWk1 = rawData.getOrNull(i)?.lightWk1_avg ?: 0.0
                val humidityWk2 = rawData.getOrNull(i)?.lightWk2_avg ?: 0.0
                preparedData.add(0, Pair(dayLabel, Pair(humidityWk1, humidityWk2)))
            }
        }

        while (preparedData.size < 7) {
            val dayLabel = now.minusDays(preparedData.size.toLong()).format(dayOfWeekFormatter)
            preparedData.add(0, Pair(dayLabel, Pair(0.0, 0.0)))
        }

        return preparedData
    }

    fun prepareWeekLuxData(rawData: List<StatsViewModel.DataModel>): List<Pair<String, Double>> {
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val preparedData = mutableListOf<Pair<String, Double>>()

        val noonIndex = rawData.indexOfFirst {
            val hourDifference = Duration.between(LocalDateTime.now().withHour(12), now).toHours()
            it == rawData.getOrNull(rawData.size - hourDifference.toInt())
        }

        if (noonIndex != -1) {
            for (i in noonIndex downTo 0 step 24) {
                val dayLabel = now.minusDays((noonIndex - i) / 24L).format(dayOfWeekFormatter)
                val lux_Avg = rawData.getOrNull(i)?.lux_avg ?: 0.0
                preparedData.add(0, Pair(dayLabel, lux_Avg))
            }
        }

        while (preparedData.size < 7) {
            val dayLabel = now.minusDays(preparedData.size.toLong()).format(dayOfWeekFormatter)
            preparedData.add(0, Pair(dayLabel, 0.0))
        }

        return preparedData
    }

    fun prepareWeekTempData(rawData: List<StatsViewModel.DataModel>): List<Pair<String, Double>> {
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val preparedData = mutableListOf<Pair<String, Double>>()

        val noonIndex = rawData.indexOfFirst {
            val hourDifference = Duration.between(LocalDateTime.now().withHour(12), now).toHours()
            it == rawData.getOrNull(rawData.size - hourDifference.toInt())
        }

        if (noonIndex != -1) {
            for (i in noonIndex downTo 0 step 24) {
                val dayLabel = now.minusDays((noonIndex - i) / 24L).format(dayOfWeekFormatter)
                val temp_Avg = rawData.getOrNull(i)?.temp_avg ?: 0.0
                preparedData.add(0, Pair(dayLabel, temp_Avg))
            }
        }

        while (preparedData.size < 7) {
            val dayLabel = now.minusDays(preparedData.size.toLong()).format(dayOfWeekFormatter)
            preparedData.add(0, Pair(dayLabel, 0.0))
        }

        return preparedData
    }
// TODO test above


    private fun dayOfWeekToString(day: Int): String {
        return when (day) {
            1 -> "Pon"
            2 -> "Wt"
            3 -> "Śr"
            4 -> "Czw"
            5 -> "Pt"
            6 -> "Sob"
            7 -> "Ndz"
            else -> ""
        }
    }

}

enum class TimeRange {
    DAY, WEEK, MONTH
}

