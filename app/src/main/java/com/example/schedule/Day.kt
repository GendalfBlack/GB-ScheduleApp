package com.example.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

typealias update_delegate = () -> Unit
class Day(name:String, date:String, var lessons: ArrayList<Lesson>) : Fragment() {
    public lateinit var updateDays : update_delegate
    var position : Int = 0
    lateinit var lessonsViewAdapter : DayAdapter

    private var _name : String = name
    var name : String
        get(){return _name}
        set(value){_name = value}
    private var _date : String = date
    var date : String
        get(){return _date}
        set(value){_date = value}

    private fun sort(){
        lessons.sortBy { it.time }
        for (i in 0 until lessons.size) {
            lessons[i].position = i
        }
    }

    fun addLesson(name: String, group:String, time: String, room: String) : Lesson {
        val lesson = Lesson(name, group, time, room)
        lessons.add(lesson)
        sort()
        return lesson
    }
    fun removeLesson(holder : LessonHolder){
        lessons.removeAt(holder.adapterPosition)
        sort()
        lessonsViewAdapter.notifyItemRemoved(holder.adapterPosition)
        lessonsViewAdapter.notifyItemRangeChanged(holder.adapterPosition, lessons.size)
        updateDays()
    }
}

typealias button_updates = (state : Int) -> Unit

class DayAdapter(private val day: Day) : RecyclerView.Adapter<LessonHolder>() {
    private val update_event : ArrayList<button_updates> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson, parent, false)
        return LessonHolder(view)
    }
    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        holder.lessonName.text = day.lessons[position].name
        holder.lessonTime.text = day.lessons[position].time
        holder.lessonGroup.text = day.lessons[position].group
        holder.lessonRoom.text = day.lessons[position].room
        holder.lessonRemove.setOnClickListener {
            day.removeLesson(holder)
            day.updateDays()
        }
        if(holder.lessonRemove.visibility == View.VISIBLE){
            holder.lessonRemove.visibility = View.GONE
            holder.lessonRemove.width = 0
        }
        update_event.add {state : Int ->
            if(holder.lessonRemove.visibility != View.VISIBLE && state == 1)
            { holder.lessonRemove.visibility = View.VISIBLE
              holder.lessonRemove.width = 150
            }
            else{ holder.lessonRemove.visibility = View.GONE
                  holder.lessonRemove.width = 0}
        }
        holder.lessonView.setOnClickListener {
            for(i in 0 until update_event.size) {
                if(i == position){update_event[i](1)}
                else{ update_event[i](0) }
            }
        }
    }
    override fun getItemCount(): Int {
        return day.lessons.size
    }
}
class DayHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val dayName: TextView = itemView.findViewById(R.id.weekDayName)
    val dayDate : TextView = itemView.findViewById(R.id.weekDayDate)
    val dayLessons : RecyclerView = itemView.findViewById(R.id.dayLessons)
}