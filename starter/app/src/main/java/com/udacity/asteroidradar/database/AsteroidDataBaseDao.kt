package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AsteroidDatabaseDao {

    @Insert
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
}