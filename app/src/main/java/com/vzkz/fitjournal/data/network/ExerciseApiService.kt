package com.vzkz.fitjournal.data.network

import com.vzkz.fitjournal.data.network.response.ExerciseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseApiService {
    @GET("?")
    suspend fun  getExerciseByName(@Query("name") name: String): List<ExerciseResponse>
}