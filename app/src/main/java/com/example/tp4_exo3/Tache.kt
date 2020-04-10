package com.example.tp4_exo3

class Tache(taskName : String, mYear : Int, mMonth: Int, mDay: Int) : Comparable<Tache>{
    var name = taskName
    var year = mYear
    var month = mMonth
    var day = mDay

    fun dateToString() : String{
        return "$day-${month+1}-$year"
    }

    override fun compareTo(other: Tache): Int {
        return ("$name-$year-$month-$day").compareTo("${other.name}-${other.year}-${other.month}-${other.day}")
    }

    override fun hashCode(): Int {
        return ("$name-$year-$month-$day").hashCode()
    }
}