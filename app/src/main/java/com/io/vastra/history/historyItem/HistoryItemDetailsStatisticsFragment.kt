package com.io.vastra.history.historyItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.io.vastra.R
import java.math.BigDecimal
import java.math.RoundingMode


class HistoryItemDetailsStatisticsFragment : Fragment() {
    internal lateinit var chart: BarChart
    internal lateinit var time: TextView
    internal lateinit var distance: TextView
    internal lateinit var calories: TextView
    internal lateinit var avgPace: TextView
    internal lateinit var maxPace: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_history_item_details_statistics,
            container,
            false
        )

        chart = view.findViewById(R.id.pace_chart)
        time = view.findViewById(R.id.history_workout_time)
        distance = view.findViewById(R.id.history_workout_distance)
        calories = view.findViewById(R.id.history_workout_calories)
        avgPace = view.findViewById(R.id.history_workout_avg_pace)
        maxPace = view.findViewById(R.id.history_workout_max_pace)


        //TODO: Provide new array values
        var exampleArray = arrayOf(6.98f, 8.16f, 5.84f, 6.07f, 7.63f, 6.23f, 6.57f, 7.47f)

        avgPace.text = getString(R.string.average_pace_history_details, exampleArray.average())
        maxPace.text = getString(R.string.max_pace_history_details, exampleArray.max())
        calories.text = getString(R.string.calories, 875) //TODO count calories
        distance.text = getString(R.string.distance, 12.54) //TODO provide distance


        val data: BarData = createChartData(exampleArray)
        configureChartAppearance()
        prepareChartData(data)
        return view
    }

    private fun createChartData(data: Array<Float>): BarData {
        val values: ArrayList<BarEntry> = ArrayList()

        for (i in 0 until data.size) {
            val x = i.toFloat()
            val y = data[i]
            values.add(BarEntry(x, y))
        }

        val set = BarDataSet(values, "Pace")
        set.setColor( resources.getColor(R.color.colorAccent))

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set)

        dataSets.get(0).valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toString()
            }
        }
        return BarData(dataSets)
    }

    private fun configureChartAppearance(){
        chart.getDescription().setEnabled(false)
        chart.setDrawValueAboveBar(false)
        chart.legend.isEnabled = false

        val xAxis: XAxis = chart.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return BigDecimal(value.toLong()).setScale(2, RoundingMode.HALF_EVEN).toString()
            }
        }
    }

    private fun prepareChartData(data: BarData){
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }

}