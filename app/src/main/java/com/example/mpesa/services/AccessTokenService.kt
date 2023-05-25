package com.example.mpesa.services
import com.example.mpesa.pojos.AccessTokenResponse
import com.example.mpesa.pojos.STKPushResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AccessTokenService {
    //https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest
    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun getAccessData(@Header("Authorization") credential:String):retrofit2.Call<AccessTokenResponse>

    @POST("mpesa/stkpush/v1/processrequest")
    fun postData(@Header("Authorization") bearerCredential:String, @Body requestBody: RequestBody):retrofit2.Call<STKPushResponse>

}