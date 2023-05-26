package com.example.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


lateinit var week: Week

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        week = Week(ArrayList())

        val jsonInputStream = resources.openRawResource(R.raw.base)
        val jsonString = jsonInputStream.bufferedReader().use { it.readText() }

        week.ParseJson(jsonString)

        val adapter = WeekAdapter(week.days)
        val weekView : RecyclerView = findViewById(R.id.weekDays)
        weekView.adapter = adapter
        weekView.layoutManager = layoutManager

        adapter.notifyItemInserted(week.days.lastIndex)

    }

}