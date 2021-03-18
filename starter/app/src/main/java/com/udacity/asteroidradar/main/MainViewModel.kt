package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.database.Asteroid as Entity

class MainViewModel(val database: AsteroidDatabaseDao, application: Application) : AndroidViewModel(application) {

    //  For now, commenting on this, later back to database.getAll() when API is done
    //    val asteroids = database.getAll()
    var asteroids = populateAsteroids()

    private val _asteroidDetail = MutableLiveData<Asteroid?>()

    val asteroidDetail : LiveData<Asteroid?>
        get() = _asteroidDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _asteroidDetail.value = asteroid
    }

    fun onAsteroidNavigated(){
        _asteroidDetail.value = null
    }

    fun populateAsteroids() : MutableLiveData<List<Entity>>{

        val dummyAsteroids = mutableListOf<Entity>()
        dummyAsteroids.add((Entity(codename = "Dummy Asteroid 1")))
        dummyAsteroids.add((Entity(codename = "Dummy Asteroid 2")))
        dummyAsteroids.add((Entity(codename = "Dummy Asteroid 3")))


        val liveAsteroidData : MutableLiveData<List<Entity>> = MutableLiveData()
        liveAsteroidData.value = dummyAsteroids
        return liveAsteroidData
    }

    private suspend fun insertAsteroid(asteroid: Entity) {
        withContext(Dispatchers.IO) {
            database.insert(asteroid)
        }
    }
}