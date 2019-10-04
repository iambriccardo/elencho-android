package com.riccardobusetti.unibztimetable

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.riccardobusetti.unibztimetable.network.WebSiteLink
import com.riccardobusetti.unibztimetable.network.WebSiteScraper
import com.riccardobusetti.unibztimetable.ui.adapters.DayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var adapter = DayAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        activity_main_recycler_view.layoutManager = layoutManager
        activity_main_recycler_view.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            val webSiteLink = WebSiteLink.Builder()
                .useDeviceLanguage()
                .withDepartment("22")
                .withDegree("13205")
                .withAcademicYear("16858")
                .fromToday()
                .toNext7Days()
                .atPage("1")
                .build()

            Log.d("DATE", webSiteLink.toString())

            val timetable = withContext(Dispatchers.IO) {
                WebSiteScraper(webSiteLink).getTimetable()
            }

            adapter.updateDays(timetable)
        }
    }
}
