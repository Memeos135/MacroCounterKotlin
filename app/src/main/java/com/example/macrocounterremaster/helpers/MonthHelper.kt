package com.example.macrocounterremaster.helpers

class MonthHelper {
    companion object {
        fun getMonth(index: Int): String {
            val monthNames = arrayOf(
                "JAN",
                "FEB",
                "MAR",
                "APR",
                "MAY",
                "JUN",
                "JUL",
                "AUG",
                "SEP",
                "OCT",
                "NOV",
                "DEC"
            )
            return monthNames[index-1]
        }
    }
}