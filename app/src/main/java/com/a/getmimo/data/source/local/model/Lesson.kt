package com.a.getmimo.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "lesson")
class Lesson (@ColumnInfo(name = "startDate") var startDate: Long = 0,
              @ColumnInfo(name = "endDate") var endDate: Long = 0,
              @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString())