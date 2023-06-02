package com.example.schedule

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class Lesson(name:String, group:String, time:String, room:String) : Fragment() {
    private var _name : String = name
    var name : String
        get(){return _name}
        set(value){_name = value}
    private var _group : String = group
    var group : String
        get(){return _group}
        set(value){_group = value}
    private var _time : String = time
    var time : String
        get(){return _time}
        set(value){_time = value}
    private var _room : String = room
    var room : String
        get(){return _room}
        set(value){_room = value}
    var position : Int = 0
}

class LessonHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val lessonName: TextView = itemView.findViewById(R.id.lessonNameTextView)
    val lessonGroup : TextView = itemView.findViewById(R.id.lessonGroupTextView)
    val lessonTime : TextView = itemView.findViewById(R.id.lessonTimeTextView)
    val lessonRoom : TextView = itemView.findViewById(R.id.lessonRoomTextView)
    val lessonRemove : Button = itemView.findViewById(R.id.buttonRemove)
    val lessonView : ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
}