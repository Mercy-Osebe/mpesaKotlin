package com.example.mpesa.services
import com.example.mpesa.pojos.AccessTokenResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface AccessTokenService {
//https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest
    @GET("stkpush/v1/processrequest")
    fun getAccessData(@Header("Authorization") credential:String):retrofit2.Call<AccessTokenResponse>

    }