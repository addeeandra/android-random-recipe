package me.inibukanadit.randomrecipe.app

import android.app.Application
import com.androidnetworking.AndroidNetworking

class RecipeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(this)
    }

}