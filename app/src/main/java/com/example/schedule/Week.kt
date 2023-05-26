package com.example.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject

class Week(days : ArrayList<Day>) {
    var days : ArrayList<Day> = days

    fun ParseJson(file: String){
        val obj = JSONObject(file)
        val week = obj.getJSONArray("week")
        for (i in 0 until week.length()){
            val day = week.getJSONObject(i)
            val lessonsClass = ArrayList<Lesson>()
            val lessons = day.getJSONArray("lessons")
            for (j in 0 until lessons.length()){
                val lesson = lessons.getJSONObject(j)
                lessonsClass.add(
                    Lesson(
                        lesson.getString("name"),
                        lesson.getString("group"),
                        lesson.getString("time"),
                        lesson.getString("room")
                    )
                )
            }
            val dayClass = Day(
                day.getString("name"),
                day.getString("date"),
                lessonsClass
            )
            days.add(dayClass)
        }
    }
}

class WeekAdapter(private val daysList:ArrayList<Day>) : RecyclerView.Adapter<DayHolder>() {
    lateinit var parent : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day, parent,false)
        return DayHolder(view)
    }
    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val day = daysList[position]
        holder.dayName.text = day.name
        holder.dayDate.text = day.date
        holder.dayLessons.adapter = DayAdapter(day.lessons)

        val adapter2 = DayAdapter(week.days[position].lessons)
        holder.dayLessons.adapter = adapter2
        holder.dayLessons.layoutManager = LinearLayoutManager(parent.context)

        adapter2.notifyItemInserted(week.days[position].lessons.lastIndex)
    }
    override fun getItemCount(): Int {
        return daysList.size
    }
}