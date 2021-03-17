package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabaseDao

class MainViewModel(val database: AsteroidDatabaseDao, application: Application) : AndroidViewModel(application) {

    val asteroids = database.getAll()

    private val _asteroidDetail = MutableLiveData<Asteroid?>()

    val asteroidDetail : LiveData<Asteroid?>
        get() = _asteroidDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _asteroidDetail.value = asteroid
    }

    fun onAsteroidNavigated(){
        _asteroidDetail.value = null
    }
}