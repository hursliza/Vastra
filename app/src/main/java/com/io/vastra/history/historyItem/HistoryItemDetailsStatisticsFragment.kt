package com.io.vastra.history.historyItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.io.vastra.R
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.entities.RunDescription
import com.io.vastra.utils.toVastraTimeString
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


enum class HistoryDetailsStatisticsArgs {
    RunIdx
}

@ExperimentalTime
class HistoryItemDetailsStatisticsFragment : Fragment() {
    internal lateinit var chart: BarChart;
    internal lateinit var time: TextView;
    internal lateinit var distance: TextView;
    internal lateinit var calories: TextView;
    internal lateinit var avgPace: TextView;
    internal lateinit var maxPace: TextView;

    private val viewModel: HistoryItemViewModel by viewModels({requireParentFragment()}) {
        HistoryItemViewModelFactory(viewLifecycleOwner);
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_history_item_details_statistics,
            container,
            false
        )
        bindViews(view);
        configureDataSource();
        return view
    }

    private fun bindViews(mainView: View) {
        chart = mainView.findViewById(R.id.pace_chart)
        time = mainView.findViewById(R.id.history_workout_time)
        distance = mainView.findViewById(R.id.history_workout_distance)
        calories = mainView.findViewById(R.id.history_workout_calories);
        avgPace = mainView.findViewById(R.id.history_workout_avg_pace)
        maxPace = mainView.findViewById(R.id.history_workout_max_pace)
    }

    private fun configureDataSource() {
        viewModel.runDescription.observe(viewLifecycleOwner) {
            updateView(it)
        }


        val defaultIdx = -1;
        val runIdx =  arguments?.getInt(HistoryDetailsStatisticsArgs.RunIdx.name) ?: defaultIdx;
        if (runIdx != defaultIdx) {
            UserDataSourceProvider.instance.getDataSource().currentUser.observe(viewLifecycleOwner) {
                it.runHistory?.values?.toList()?.get(runIdx)?.let { item -> updateView(item) };
            }
        }
    }


    @ExperimentalTime
    fun updateView(description: RunDescription) {
        distance.text = getString(R.string.distance, description.distance);
        avgPace.text = getString(R.string.average_pace_history_details, description.pacePerKm.average());
        maxPace.text = getString(R.string.max_pace_history_details, description.pacePerKm.maxOrNull() ?: 0f);
        val runDuration = (description.runDuration ?: 0).seconds;
        time.text =  runDuration.toVastraTimeString();
        calories.text = getString(R.string.calories, 875) //TODO count calories
        val data: BarData = createChartData(description.pacePerKm);
        configureChartAppearance();
        prepareChartData(data);
    }


    private fun createChartData(data: List<Double>): BarData {
        val values: ArrayList<BarEntry> = ArrayList()

        for (i in 0 until data.size) {
            val x = i.toFloat()
            val y = data[i].toFloat()
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