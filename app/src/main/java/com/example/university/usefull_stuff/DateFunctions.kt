package com.example.university.usefull_stuff

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

val stdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val simpleFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
val simpleFormatterWithYear = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))

fun getDaysFromToday(n: Int): List<LocalDate> {
    var dates = ArrayList<LocalDate>()
    var date = LocalDate.now()

    repeat(n) {
        dates.add(date)
        date = date.plusDays(1)
    }

    return dates
}

fun getDateNDaysLater(n: Int): String {
    var date = LocalDate.now()

    date = date.plusDays(n.toLong())

    return date.format(stdFormatter)
}

fun getTodayDate(): String{
    return LocalDate.now().format(stdFormatter)
}

fun formatDate(date: LocalDate): String{
    return date.format(stdFormatter)
}
