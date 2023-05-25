package com.example.mpesa

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mpesa.pojos.AccessTokenResponse
import com.example.mpesa.pojos.STKPushResponse
import com.example.mpesa.services.AccessTokenService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var payMe:Button
    private val baseUrl ="https://sandbox.safaricom.co.ke/"
    private val retrofit: Retrofit =Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        payMe=findViewById(R.id.pay_me)
        payMe.setOnClickListener {
            fetchAccessToken()
        }

    }
    private fun credentials(): String {
        val consumer_Key = "cHedtdUEWvmZFgT7baOe6mymyDh4a2pf"
        val consumer_Secret = "j9bGiSy2G0UDWEOW"

        return Credentials.basic(consumer_Key, consumer_Secret)
    }

    private fun fetchAccessToken() {
        val accessTokenServiceApi=retrofit.create(AccessTokenService::class.java)
//            make api call
        val credentials=credentials()
//            println(credentials)
        val apiCall=accessTokenServiceApi.getAccessData(credentials)
        apiCall.enqueue(object:Callback<AccessTokenResponse>{
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {
                println("hi success")
                println("$response hello")
                if(response.isSuccessful){
                    val data=response.body()

                    println(data!!.access_token)
                    val accessToken:String=data.access_token
                    println(data.expires_in)
                    println("hello")
                    makePushNotificationRequest(accessToken)
                }
            }
            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                println("failed")
            }
        })
    }
    private fun generateTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
    private fun generateBase64():String{
        val bizCode:String="174379"
        val passkey:String="bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
        val text="$bizCode$passkey${generateTimestamp()}"
        val encodedText=Base64.getEncoder().encodeToString(text.toByteArray())
        return encodedText
    }
    data class YourRequestData(@SerializedName("BusinessShortCode") val businessShortCode: String,
                               @SerializedName("Password") val password: String,
                               @SerializedName("Timestamp") val timestamp: String,
                               @SerializedName("TransactionType") val transactionType: String,
                               @SerializedName("Amount") val amount: String,
                               @SerializedName("PartyA") val partyA: String,
                               @SerializedName("PartyB") val partyB: String,
                               @SerializedName("PhoneNumber") val phoneNumber: String,
                               @SerializedName("CallBackURL") val callBackURL: String,
                               @SerializedName("AccountReference") val accountReference: String,
                               @SerializedName("TransactionDesc") val transactionDesc: String)
    private fun makePushNotificationRequest(accessToken: String) {
//        make request body
        val gson= Gson()
        val data=YourRequestData("174379",generateBase64(),generateTimestamp(),"CustomerPayBillOnline","1",
            "254711174275","174379","254711174275","https://mydomain.com/pat","Test","Test",
        )
        Log.d("mercy", gson.toJson(data))
        val requestBody= RequestBody.create(MediaType.parse("application/json"),gson.toJson(data))
        // define request  object
        val stkPushServiceApi=retrofit.create(AccessTokenService::class.java)
        Log.d("mercy", "Bearer $accessToken")


        val stkApiCall=stkPushServiceApi.postData("Bearer $accessToken",requestBody)
        stkApiCall.enqueue(object:Callback<STKPushResponse>{
            override fun onResponse(
                call: Call<STKPushResponse>,
                response: Response<STKPushResponse>
            ) {
                println("here")
                val body=response.body()
                val message=response.message()
                val error=response.errorBody()
                if(response.isSuccessful){
                    val data=response.body()
                    if(data!=null){
                        println(data.CustomerMessage)
                        println(data.MerchantRequestID)
                        println("stk success")
                    }
                    println("data is null")
                }
            }

            override fun onFailure(call: Call<STKPushResponse>, t: Throwable) {
                println("stk failed")
            }
        })



    }
}