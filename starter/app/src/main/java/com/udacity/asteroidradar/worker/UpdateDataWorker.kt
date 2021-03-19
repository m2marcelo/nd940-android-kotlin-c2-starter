package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDataBase.Companion.getInstance
import com.udacity.asteroidradar.dependencyinjection.DependencyInjection
import retrofit2.HttpException

class UpdateDataWorker (appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val UPDATE_DATA_WORKER = "UpdateDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getInstance(applicationContext)
        val dependencyInjection = DependencyInjection()
        val repository = dependencyInjection.repository
        return try {
            repository.updateAsteroids()
            Result.success()
        } catch (e : HttpException) {
            Result.retry()
        }
    }
}