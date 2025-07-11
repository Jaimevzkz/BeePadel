package com.vzkz.match.domain.model

enum class Points(val string: String) {
    Zero("00"),
    Fifteen("15"),
    Thirty("30"),
    Forty("40"),
    Advantage("Ad"),
    Won("W");

    fun addPoint(): Points{
        val newPointIndex = if (this.ordinal == Points.entries.size-1) 0 else this.ordinal+1
        return Points.entries[newPointIndex]
    }

    fun undoPoint():Points{
        val newPointIndex = if (this.ordinal == 0) Points.entries.size-1 else this.ordinal-1
        return Points.entries[newPointIndex]
    }
}