package com.example.eventapp.Retrofit

import com.example.eventapp.Response.ResponseGetEvent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getUpcomingEvents(@Query("active") active: Int = 1): Response<ResponseGetEvent>

    @GET("events")
    suspend fun getFinishedEvents(@Query("active") active: Int = 0): Response<ResponseGetEvent>

    @GET("events")
    suspend fun getNearestEvent(@Query("active") active: Int = -1, @Query("limit") limit: Int = 1): Response<ResponseGetEvent>

}