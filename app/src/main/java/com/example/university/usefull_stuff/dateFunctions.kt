package com.example.university.usefull_stuff

import java.time.LocalDate

fun getDaysFromToday(n: Int): List<LocalDate> {
    var dates = ArrayList<LocalDate>()
    var date = LocalDate.now()

    repeat(n) {
        dates.add(date)
        date = date.plusDays(1)
    }

    return dates

}

