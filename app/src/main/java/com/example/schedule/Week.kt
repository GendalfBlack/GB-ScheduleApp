package com.example.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class Week(var days: ArrayList<Day>) : Fragment(R.layout.activity_main) {
    lateinit var daysViewAdapter : WeekAdapter
    fun parseJson(file: String){
        val obj = JSONObject(file)
        val week = obj.getJSONObject("week")
        val days =  week.getJSONArray("days")
        for (i in 0 until days.length()){
            val day = days.getJSONObject(i)
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
            this.days.add(dayClass)
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
        days.sortBy { it.date }
        for (i in 0 until days.size){
            days[i].position = i
        }
    }

    fun addDay(name: String, date: String) : Day {
        val day = Day(name, date, ArrayList())
        days.add(day)
        sort()
        return day
    }
    private fun removeDay(holder : DayHolder){
        days.removeAt(holder.adapterPosition)
        sort()
        daysViewAdapter.notifyItemRemoved(holder.adapterPosition)
        daysViewAdapter.notifyItemRangeChanged(holder.adapterPosition, days.size)
    }
    fun updateDay(holder : DayHolder){ if(days.size == 0){ removeDay(holder) } }
}

class WeekAdapter(private val week: Week) : RecyclerView.Adapter<DayHolder>() {
    private lateinit var parent : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day, parent,false)
        return DayHolder(view)
    }
    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.dayName.text = week.days[position].name
        holder.dayDate.text = week.days[position].date

        week.days[position].lessonsViewAdapter = DayAdapter(week.days[position])
        holder.dayLessons.adapter = week.days[position].lessonsViewAdapter
        holder.dayLessons.layoutManager = LinearLayoutManager(parent.context)

        week.days[position].lessonsViewAdapter.notifyItemInserted(week.days[position].lessons.lastIndex)
        week.days[position].updateDays = {week.updateDay(holder)}
    }
    override fun getItemCount(): Int {
        return week.days.size
    }
}