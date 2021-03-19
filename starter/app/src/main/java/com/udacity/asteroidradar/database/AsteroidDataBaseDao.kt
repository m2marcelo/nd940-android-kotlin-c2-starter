package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: Asteroid)

    @Update
    fun update(asteroid: Asteroid)

    @Query("SELECT * from asteroids_details_table WHERE id = :key")
    fun get(key: Long): Asteroid?

    @Query("DELETE FROM asteroids_details_table")
    fun clear()

    @Query("SELECT * FROM asteroids_details_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids_details_table WHERE id = :key")
    fun getById(key: Long): LiveData<Asteroid>

    @Query("SELECT * FROM asteroids_details_table WHERE close_approach_date = :date ORDER BY close_approach_date DESC")
    fun getTodayAsteroids(date: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids_details_table WHERE close_approach_date BETWEEN :startDate AND :endDate")
    fun getWeeklyAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>

    // Picture of day

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pictureOfDay: PictureOfDay)

    @Query("SELECT * FROM picture_of_day")
    fun getPicture(): PictureOfDay
}