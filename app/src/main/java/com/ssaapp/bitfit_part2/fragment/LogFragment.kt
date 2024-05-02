package com.ssaapp.bitfit_part2.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssaapp.bitfit_part2.DBHelper
import com.ssaapp.bitfit_part2.data.Model
import com.ssaapp.bitfit_part2.R
import com.ssaapp.bitfit_part2.RVAdapter
import com.ssaapp.bitfit_part2.data.SummaryModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class LogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.log_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = ArrayList<Model>()
        val db = DBHelper(view.context, null)
        //db.populateDB()

        // date for calcing stuph
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        Log.i("DATESTUFF", dayOfYear.toString())

        val rView = view.findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(view.context)

        // populate RV
        val cursor = db.getFood()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                listData.add(
                    Model(
                        cursor.getString(cursor.getColumnIndex("food_name")),
                        cursor.getInt(cursor.getColumnIndex("calorie_count"))
                    )
                )
            }

            val adapter = RVAdapter(listData)
            rView.adapter = adapter
        }
    }
}