package com.a.getmimo.domain

data class Lesson(
    private val id: Int?,
    private val content: List<Content>?,
    val startIndex: Int?,
    val endIndex: Int?
)