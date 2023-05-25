package com.example.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Day(name:String, date:String, lessons: ArrayList<Lesson>) {
    private var _name : String = name
    var name : String
        get(){return _name}
        set(value){_name = value}
    private var _date : String = date
    var date : String
        get(){return _date}
        set(value){_date = value}
    var lessons : ArrayList<Lesson> = lessons
}

class DayAdapter(private val taskList:ArrayList<Lesson>) : RecyclerView.Adapter<LessonHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson, parent, false)
        return LessonHolder(view)
    }
    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        val lesson = taskList[position]
        holder.lessonName.text = lesson.name
        holder.lessonTime.text = lesson.time
        holder.lessonGroup.text = lesson.group
        holder.lessonRoom.text = lesson.room
    }
    override fun getItemCount(): Int {
        return taskList.size
    }
}