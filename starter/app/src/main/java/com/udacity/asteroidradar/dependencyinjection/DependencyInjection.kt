package com.udacity.asteroidradar.dependencyinjection

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.Repository
import com.udacity.asteroidradar.worker.UpdateDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DependencyInjection : Application() {

    val database by lazy { AsteroidDataBase.getInstance(this) }

    val repository by lazy { Repository(database) }

    val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        scope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<UpdateDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            UpdateDataWorker.UPDATE_DATA_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}