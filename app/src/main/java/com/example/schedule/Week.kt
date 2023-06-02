package com.example.schedule

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Week : Fragment(R.layout.activity_main) {
    lateinit var daysViewAdapter : WeekAdapter
    lateinit var daysViewHolder : DayHolder
    var days: ArrayList<Day> = ArrayList()
    fun parseJson(file: String) {
        val obj = JSONObject(file)
        val week = obj.getJSONObject("week")
        val days =  week.getJSONArray("days")
        for (i in 0 until days.length()){
            val day = days.getJSONObject(i)
            val pos = this.days.indexOf(
                this.addDay( day.getString("name"), day.getString("date")))
            this.daysViewAdapter.notifyItemInserted(this.days.lastIndex)
            this.days[pos].lessonsViewAdapter = DayAdapter(this.days[pos])
            this.days[pos].updateDay = { if (this.days.size > 0) this.updateDay(this.days[pos]) }
            val lessons = day.getJSONArray("lessons")
            for (j in 0 until lessons.length()){
                val lesson = lessons.getJSONObject(j)
                this.days[pos].addLesson(
                    lesson.getString("name"),
                    lesson.getString("group"),
                    lesson.getString("time"),
                    lesson.getString("room"))
                this.days[pos].lessonsViewAdapter.notifyItemInserted(
                    this.days[pos].lessons.lastIndex)
            }
        }
    }
    fun createJson() : String{
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

    private fun sort(){
        days.sortBy { LocalDate.parse(it.date,
            DateTimeFormatter.ofPattern("dd.MM.yyyy")) }
        for (i in 0 until days.size){
            days[i].position = i
        }
    }

    fun addDay(name: String, date: String) : Day {
        val day = Day(name, date)
        days.add(day)
        sort()
        return day
    }
    private fun removeDay(){
        days.removeAt(daysViewHolder.adapterPosition)
        sort()
        daysViewAdapter.notifyItemRemoved(daysViewHolder.adapterPosition)
        daysViewAdapter.notifyItemRangeChanged(daysViewHolder.adapterPosition, days.size)
    }
    fun updateDay(day : Day){
        if(day.lessons.isEmpty()){ removeDay() } }
}

class WeekAdapter(private val week: Week) : RecyclerView.Adapter<DayHolder>() {
    private lateinit var parent : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day, parent,false)
        week.daysViewHolder = DayHolder(view)
        return week.daysViewHolder
    }
    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.dayName.text = week.days[position].name
        holder.dayDate.text = week.days[position].date

        week.days[position].lessonsViewAdapter = DayAdapter(week.days[position])
        holder.dayLessons.adapter = week.days[position].lessonsViewAdapter
        holder.dayLessons.layoutManager = LinearLayoutManager(parent.context)

        week.days[position].lessonsViewAdapter.notifyItemInserted(week.days[position].lessons.lastIndex)

    }
    override fun getItemCount(): Int {
        return week.days.size
    }
}