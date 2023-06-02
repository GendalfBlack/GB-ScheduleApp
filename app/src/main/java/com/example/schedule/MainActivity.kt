package com.example.schedule

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.time.LocalDate
import java.time.LocalTime


lateinit var week: Week

class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        week = Week(ArrayList())

        var file = File(applicationContext.filesDir, "base.json")
        if (file.exists()){
            week.parseJson(file.readText())
        }else{
            file = File(applicationContext.filesDir, "base.json")
            file.writeText(week.createJson())
        }

        val adapter = WeekAdapter(week)
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
        val dateView : TextView = findViewById(R.id.editTextDate)
        var pickedDate = ""
        dateView.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                val localDate = LocalDate.now()
                val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    pickedDate = "%02d".format(dayOfMonth)+".%02d".format(monthOfYear)+".$year"
                    dateView.text = pickedDate
                }, localDate.year,localDate.monthValue,localDate.dayOfMonth )
                datePickerDialog.show()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        val timeView : TextView = findViewById(R.id.editTextTime)
        var pickedTime = ""
        timeView.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP){
                val localTime = LocalTime.now()
                val timePickerDialog = TimePickerDialog(
                    this, { _, hour, minute ->
                        pickedTime = "%02d".format(hour)+":%02d".format(minute)
                        timeView.text = pickedTime
                    }, localTime.hour, localTime.minute, false)
                timePickerDialog.show()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
        bAdd.setOnClickListener {
            val nameView : TextView = findViewById(R.id.editTextName)
            val groupView : TextView = findViewById(R.id.editTextGroup)
            val roomView : TextView = findViewById(R.id.editTextRoom)
            val day  = week.days.find { it.date == pickedDate }
            if (day != null){
                val newLesson = day.addLesson(
                        nameView.text.toString(),
                        groupView.text.toString(),
                        pickedTime,
                        roomView.text.toString()
                    )
                day.lessonsViewAdapter.notifyItemInserted(newLesson.position)
            }else{
                val newDay = week.addDay("text", pickedDate)
                newDay.addLesson(
                        nameView.text.toString(),
                        groupView.text.toString(),
                        pickedTime,
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
        file.writeText(week.createJson())
        super.onDestroy()
    }
    override fun onStop() {
        val file = File(applicationContext.filesDir, "base.json")
        file.writeText(week.createJson())
        super.onStop()
    }
}