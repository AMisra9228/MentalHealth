package com.sample.mentalhealth.retrofit

import com.sample.mentalhealth.home.OverviewItem
import com.sample.mentalhealth.login_registration.model.SignupRequest
import com.sample.mentalhealth.login_registration.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("overview.php")
    suspend fun getOverviewItems(): Response<List<OverviewItem>>

    @POST("api/signup.php")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>
}