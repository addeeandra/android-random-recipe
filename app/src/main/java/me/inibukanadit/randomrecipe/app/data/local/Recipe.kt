package me.inibukanadit.randomrecipe.app.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Recipe(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var name: String,
        var category: String,
        var tags: String,
        var area: String,
        var instructions: String,
        var ingredients: String,
        var measures: String,
        var thumbUrl: String,
        var videoUrl: String,
        var sourceUrl: String,
        var lastUpdated: Long
)