package me.inibukanadit.randomrecipe.app.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDb : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {

        private var instance: RecipeDb? = null

        fun getInstance(context: Context): RecipeDb {
            synchronized(this) {
                if (instance == null)
                    instance = Room
                            .databaseBuilder(context, RecipeDb::class.java, "recipe-db")
                            .allowMainThreadQueries()
                            .build()

                return instance!!
            }
        }

    }

}