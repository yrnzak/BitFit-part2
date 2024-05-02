package com.ssaapp.bitfit_part2.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ssaapp.bitfit_part2.DBHelper
import com.ssaapp.bitfit_part2.R

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SummaryFragment : Fragment() {
    private lateinit var lineGraphView: GraphView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.summary_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateTV : TextView = view.findViewById(R.id.date)
        val avgCalTV : TextView = view.findViewById(R.id.avg_cal)
        val minCalTV : TextView = view.findViewById(R.id.min_cal)
        val maxCalTV : TextView = view.findViewById(R.id.max_cal)

        val db = DBHelper(view.context, null)

        // date for calcing stuph
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        Log.i("DATESTUFF", dayOfYear.toString())

        // set date
        val currentLDT = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        dateTV.text = currentLDT.format(formatter)

        var pos = 1.0
        var dataArr = arrayOf(
            DataPoint(0.0, 0.0)
        )

        // populate RV
        val cursor = db.getFood()
        var minCal = 0
        var maxCal = 0

        var avgCal = 0
        var entryCount = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {

                if (cursor.getInt(cursor.getColumnIndex("day_of_year")) == dayOfYear){
                    val calCount = cursor.getInt(cursor.getColumnIndex("calorie_count"))

                    dataArr += DataPoint(pos, calCount.toDouble())
                    pos += 1.0

                    if (minCal == 0 || calCount < minCal) minCal = calCount
                    if (maxCal == 0 || calCount > maxCal) maxCal = calCount

                    avgCal += calCount
                    entryCount ++
                }
            }
        }

        if(avgCal > 0)
            avgCal /= entryCount

        avgCalTV.text = avgCal.toString()
        minCalTV.text = minCal.toString()
        maxCalTV.text = maxCal.toString()

        lineGraphView = view.findViewById(R.id.idGraphView)
        for (d in dataArr) {
            Log.i("GRAPHINF", d.toString())
        }

        // TODO: make data add dynamically
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            dataArr
        )

        lineGraphView.viewport.isXAxisBoundsManual = true
        lineGraphView.viewport.setMaxX(entryCount + 1.0)
        lineGraphView.gridLabelRenderer.isHorizontalLabelsVisible = false

        series.color = R.color.purple_200
        lineGraphView.viewport.setScalableY(false)
        lineGraphView.addSeries(series)
    }
}
