package com.vzkz.match.domain.model

enum class Points(val string: String) {
    Zero("00"),
    Fifteen("15"),
    Thirty("30"),
    Forty("40"),
    Advantage("Ad"),
    Won("W");
}

fun Int.toPoints(): Points = Points.entries[this]