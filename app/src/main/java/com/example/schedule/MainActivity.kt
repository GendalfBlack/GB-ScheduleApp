package com.example.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


lateinit var week: Week

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        week = Week(ArrayList())

        var file = File(applicationContext.filesDir, "base.json")
        if (file.exists()){
            week.ParseJson(file.readText())
        }else{
            file = File(applicationContext.filesDir, "base.json")
            file.writeText(week.CreateJson())
        }

        val adapter = WeekAdapter(week.days)
        val weekView : RecyclerView = findViewById(R.id.weekDays)
        weekView.adapter = adapter
        weekView.layoutManager = layoutManager

        adapter.notifyItemInserted(week.days.lastIndex)

        val fabOpen : FloatingActionButton = findViewById(R.id.fabOpenAdd)
        val bCancel : Button = findViewById(R.id.buttonCancel)
        val bAdd : Button = findViewById(R.id.buttonAdd)

        val mainView : ConstraintLayout = findViewById(R.id.scheduleLayout)
        val addView : ConstraintLayout = findViewById(R.id.addLessonLayout)

        fabOpen.setOnClickListener {
            mainView.visibility = View.GONE
            addView.visibility = View.VISIBLE
        }

        bCancel.setOnClickListener {
            mainView.visibility = View.VISIBLE
            addView.visibility = View.GONE
        }

        bAdd.setOnClickListener {
            val nameView : TextView = findViewById(R.id.editTextName)
            val groupView : TextView = findViewById(R.id.editTextGroup)
            val roomView : TextView = findViewById(R.id.editTextRoom)
            val dateView : TextView = findViewById(R.id.editTextDate)
            val timeView : TextView = findViewById(R.id.editTextTime)
            val day = week.days.find { it.date == dateView.text.toString() }
            if (day != null){
                val newLesson = day.AddLesson(
                        nameView.text.toString(),
                        groupView.text.toString(),
                        timeView.text.toString(),
                        roomView.text.toString()
                    )
                day.lessonsViewAdapter.notifyItemInserted(newLesson.position)
            }else{
                val newDay = week.AddDay("text", dateView.text.toString())
                newDay.AddLesson(
                        nameView.text.toString(),
                        groupView.text.toString(),
                        timeView.text.toString(),
                        roomView.text.toString()
                    )
                adapter.notifyItemInserted(newDay.position)
            }
            mainView.visibility = View.VISIBLE
            addView.visibility = View.GONE
        }
    }
    override fun onDestroy() {
        val file = File(applicationContext.filesDir, "base.json")
        file.writeText(week.CreateJson())
        super.onDestroy()
    }
    override fun onStop() {
        val file = File(applicationContext.filesDir, "base.json")
        file.writeText(week.CreateJson())
        super.onStop()
    }
}