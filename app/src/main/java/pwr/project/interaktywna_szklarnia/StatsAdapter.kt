package pwr.project.interaktywna_szklarnia

import android.content.Context
import android.graphics.Color
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
import pwr.project.interaktywna_szklarnia.ui.stats.StatsFragment

class StatsAdapter(context: Context, data: List<StatsFragment.DataModel>) : ArrayAdapter<StatsFragment.DataModel>(context, 0, data) {
    private class ViewHolder {
        lateinit var chart: LineChart
        lateinit var chartTitle: TextView
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

        if (dataModel != null) {
            val entries1 = ArrayList<Entry>()
            for ((x, y) in dataModel.data1) {
                entries1.add(Entry(x.toFloat(), y.toFloat()))
            }
            val lineDataSet1 = LineDataSet(entries1, dataModel.label1)
            lineDataSet1.color = dataModel.color1
            lineData.addDataSet(lineDataSet1)

            val entries2 = ArrayList<Entry>()
            for ((x, y) in dataModel.data2) {
                entries2.add(Entry(x.toFloat(), y.toFloat()))
            }
            val lineDataSet2 = LineDataSet(entries2, dataModel.label2)
            lineDataSet2.color = dataModel.color2
            lineData.addDataSet(lineDataSet2)
        }

        viewHolder.chart.data = lineData

        // Ustawienie tytułu dla wykresu
        viewHolder.chartTitle.text = dataModel?.title ?: ""

        // Dostosowanie wyglądu osi X i Y
        val xAxis = viewHolder.chart.xAxis
        val yAxis = viewHolder.chart.axisLeft
        xAxis.axisLineColor = Color.BLACK
        yAxis.axisLineColor = Color.BLACK

        xAxis.position = XAxis.XAxisPosition.BOTTOM // Ustawienie pozycji osi X na dole
        xAxis.setDrawLabels(true) // Włącz wyświetlanie etykiet na osi X
        xAxis.setDrawAxisLine(true) // Włącz rysowanie linii osi X
        xAxis.axisLineWidth = 2f // Pogrubienie rysowania osi X
        xAxis.setDrawGridLines(false) // Wyłącz rysowanie linii siatki osi X

        yAxis.setDrawLabels(true) // Włącz wyświetlanie etykiet na osi Y
        yAxis.setDrawAxisLine(true) // Włącz rysowanie linii osi Y
        yAxis.axisLineWidth = 2f // Pogrubienie rysowania osi Y
        yAxis.gridLineWidth = 1f // Pogrubienie rysowania linii siatki osi Y

        val rightAxis = viewHolder.chart.axisRight
        rightAxis.setDrawLabels(false) // Wyłącz wyświetlanie etykiet na osi Y po prawej stronie
        rightAxis.setDrawAxisLine(false) // Wyłącz rysowanie linii osi Y po prawej stronie

        // Wyłącz wyświetlanie opisu (description label)
        viewHolder.chart.description.isEnabled = false

        viewHolder.chart.invalidate()

        return view
    }
}


//class StatsAdapter(context: Context, data: List<StatsFragment.DataModel>) : ArrayAdapter<StatsFragment.DataModel>(context, 0, data) {
//    private class ViewHolder {
//        lateinit var chart: LineChart
//        lateinit var chartTitle: TextView
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val viewHolder: ViewHolder
//        val view: View
//
//        if (convertView == null) {
//            viewHolder = ViewHolder()
//            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
//            viewHolder.chart = view.findViewById(R.id.chart)
//            viewHolder.chartTitle = view.findViewById(R.id.chart_title)
//            view.tag = viewHolder
//        } else {
//            viewHolder = convertView.tag as ViewHolder
//            view = convertView
//        }
//
//        val dataModel = getItem(position)
//        val entries = ArrayList<Entry>()
//
//        if (dataModel != null) {
//            for ((x, y) in dataModel.data) {
//                entries.add(Entry(x.toFloat(), y.toFloat()))
//            }
//        }
//
//        val lineDataSet = LineDataSet(entries, dataModel?.label ?: "")
//
//        // Ustawienie kolorów dla paczek danych
//        lineDataSet.setColors(dataModel?.color ?: Color.BLACK)
//
//        viewHolder.chart.data = LineData(lineDataSet)
//
//        // Ustawienie tytułu dla wykresu
//        viewHolder.chartTitle.text = dataModel?.title ?: ""
//
//        // Dostosowanie wyglądu osi X i Y
//        val xAxis = viewHolder.chart.xAxis
//        val yAxis = viewHolder.chart.axisLeft
//        xAxis.axisLineColor = Color.BLACK
//        yAxis.axisLineColor = Color.BLACK
//
//        xAxis.position = XAxis.XAxisPosition.BOTTOM // Ustawienie pozycji osi X na dole
//        xAxis.setDrawLabels(true) // Włącz wyświetlanie etykiet na osi X
//        xAxis.setDrawAxisLine(true) // Włącz rysowanie linii osi X
//        xAxis.axisLineWidth = 2f // Pogrubienie rysowania osi X
//        xAxis.setDrawGridLines(false) // Wyłącz rysowanie linii siatki osi X
//
//        yAxis.setDrawLabels(true) // Włącz wyświetlanie etykiet na osi Y
//        yAxis.setDrawAxisLine(true) // Włącz rysowanie linii osi Y
//        yAxis.axisLineWidth = 2f // Pogrubienie rysowania osi Y
//        yAxis.gridLineWidth = 1f // Pogrubienie rysowania linii siatki osi Y
//
//        val rightAxis = viewHolder.chart.axisRight
//        rightAxis.setDrawLabels(false) // Wyłącz wyświetlanie etykiet na osi Y po prawej stronie
//        rightAxis.setDrawAxisLine(false) // Wyłącz rysowanie linii osi Y po prawej stronie
//
//        // Wyłącz wyświetlanie opisu (description label)
//        viewHolder.chart.description.isEnabled = false
//
//        viewHolder.chart.invalidate()
//
//        return view
//    }



