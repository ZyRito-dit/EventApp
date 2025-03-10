package com.example.eventapp.Repository

import com.example.eventapp.Response.ListEventsItem
import com.example.eventapp.Response.ResponseGetEvent
import com.example.eventapp.Retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class EventRepository(private val apiService: ApiService) {

    suspend fun getUpcomingEvents(): Response<ResponseGetEvent> {
        return apiService.getUpcomingEvents()
    }
    suspend fun getFinishedEvents(): Response<ResponseGetEvent> {
        return apiService.getFinishedEvents()
    }

}
