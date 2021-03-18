package com.udacity.asteroidradar.dependencyinjection

import android.app.Application
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.Repository

class DependencyInjection : Application() {

    val database by lazy { AsteroidDataBase.getInstance(this) }

    val repository by lazy { Repository(database) }
} 