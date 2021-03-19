package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.database.Asteroid as Entity

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(val database: Repository, application: Application) : AndroidViewModel(application) {

    var asteroids = database.allAsteroids

    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    init {
        showAsteroids()
        showPictureOfDay()
    }

    private fun showAsteroids() {
        viewModelScope.launch {
            database.updateAsteroids()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showPictureOfDay() {
        viewModelScope.launch {
            database.updatePictureOfDay()
            _imageOfDay.value = database.getDailyPicture()
        }
    }

    private val _asteroidDetail = MutableLiveData<Asteroid?>()

    val showAsteroidDetail: LiveData<Asteroid?>
        get() = _asteroidDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _asteroidDetail.value = asteroid
    }

    fun onAsteroidNavigated() {
        _asteroidDetail.value = null
    }
}