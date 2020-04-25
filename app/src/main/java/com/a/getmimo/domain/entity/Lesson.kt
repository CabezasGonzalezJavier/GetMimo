package com.a.getmimo.domain.entity

data class Lesson(
    val id: Int?,
    val content: List<Content>?,
    val startIndex: Int?,
    val endIndex: Int?,
    var startDate:Long
)