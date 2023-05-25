//package com.example.mpesa.services
//import com.example.mpesa.MainActivity
//import com.example.mpesa.pojos.STKPushResponse
//import okhttp3.RequestBody
//import retrofit2.http.Body
//import retrofit2.http.Header
//import retrofit2.http.POST
//interface STKPushService {
//    @POST("mpesa/stkpush/v1/processrequest")
//    fun postData(@Header("Authorization") bearerCredential:String, @Body requestBody: RequestBody):retrofit2.Call<STKPushResponse>
//
//}