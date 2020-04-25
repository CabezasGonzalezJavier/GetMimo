package com.a.getmimo.data.source

import com.a.getmimo.domain.entity.SimpleLesson
import com.a.getmimo.data.source.remote.model.Content as ServerContent
import com.a.getmimo.data.source.remote.model.Lesson as ServerLesson
import com.a.getmimo.domain.entity.Lesson as DomainLesson
import com.a.getmimo.domain.entity.Content as DomainContent
import com.a.getmimo.data.source.local.model.Lesson as RoomLesson

fun ServerLesson.toDomainLesson():  DomainLesson= DomainLesson(
    id,
    content?.map { it.toDomainContent() },
    input?.startIndex,
    input?.endIndex
)

fun ServerContent.toDomainContent(): DomainContent = DomainContent(
    color,
    text

)

fun SimpleLesson.toRoomLesson(): RoomLesson = RoomLesson(
    startDate,
    endDate,
    id.toString()
)