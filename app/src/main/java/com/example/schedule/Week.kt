package com.example.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
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

    fun CreateJson() : String{
        val jsonObject = JSONObject()
        val weekObject = JSONObject()
        val daysArray = JSONArray()
        for (day in week.days) {
            val dayObject = JSONObject()
            dayObject.put("name", day.name)
            dayObject.put("date", day.date)
            val lessonsArray = JSONArray()
            for (lesson in day.lessons) {
                val lessonObject = JSONObject()
                lessonObject.put("name", lesson.name)
                lessonObject.put("group", lesson.group)
                lessonObject.put("time", lesson.time)
                lessonObject.put("room", lesson.room)
                lessonsArray.put(lessonObject)
            }
            dayObject.put("lessons", lessonsArray)
            daysArray.put(dayObject) }
        weekObject.put("days", daysArray)
        jsonObject.put("week", weekObject)
        return jsonObject.toString()
    }

    fun AddDay(name: String, date: String) : Day {
        val day = Day(name, date, ArrayList())
        days.add(day)
        days.sortBy { it.date }
        for (i in 0 until days.size) {
            days[i].position = i
        }
        return day
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
        day.position = position
        holder.dayName.text = day.name
        holder.dayDate.text = day.date

        val adapter2 = DayAdapter(week.days[position].lessons)
        holder.dayLessons.adapter = adapter2
        holder.dayLessons.layoutManager = LinearLayoutManager(parent.context)

        day.lessonsViewAdapter = adapter2

        adapter2.notifyItemInserted(week.days[position].lessons.lastIndex)
        for (i in position+1 until week.days.size){
            week.days[i].position += 1
        }
    }
    override fun getItemCount(): Int {
        return daysList.size
    }
}