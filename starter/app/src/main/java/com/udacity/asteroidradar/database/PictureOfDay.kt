package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay as DailyImage

@Entity(tableName = "picture_of_day")
data class PictureOfDay(
    @PrimaryKey
    var url: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "media_type")
    var mediaType: String
){
    fun showDailyImage(): DailyImage {
        return DailyImage( title = this.title, url = this.url, mediaType = this.mediaType )
    }
}