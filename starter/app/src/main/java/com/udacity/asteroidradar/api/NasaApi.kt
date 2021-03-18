package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate


private const val BASE_URL ="https://api.nasa.gov/"
private const val API_KEY = "LgFXcMEQ4OpGVe2DBOQzpXGtRpkC8z3NwgNlrybn"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface NasaApi {
    @GET("planetary/apod?&api_key=${API_KEY}")
    suspend fun getImageOfTheDay(
        @Query("date") date: LocalDate
    ): PictureOfDay


    @GET("neo/rest/v1/feed?&api_key=${API_KEY}")
    suspend fun getAsteroids(): String
}

object NasaApiBuilder {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val retrofitService: NasaApi = retrofit.create(NasaApi::class.java)
}