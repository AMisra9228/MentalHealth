package com.sample.mentalhealth.retrofit

import com.sample.mentalhealth.home.OverviewItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("overview.php")
    suspend fun getOverviewItems(): Response<List<OverviewItem>>
}