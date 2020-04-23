package com.a.getmimo.domain.entity

import com.a.getmimo.domain.entity.Content

data class Lesson(
    val id: Int?,
    val content: List<Content>?,
    val startIndex: Int?,
    val endIndex: Int?
)