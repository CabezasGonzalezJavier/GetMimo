package com.a.getmimo.domain.entity

import com.a.getmimo.domain.entity.Content

data class Lesson(
    private val id: Int?,
    private val content: List<Content>?,
    val startIndex: Int?,
    val endIndex: Int?
)