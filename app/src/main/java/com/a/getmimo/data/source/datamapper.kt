package com.a.getmimo.data.source

import com.a.getmimo.data.source.remote.Content as ServerContent
import com.a.getmimo.data.source.remote.Lesson as ServerLesson
import com.a.getmimo.domain.Lesson as DomainLesson
import com.a.getmimo.domain.Content as DomainContent

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