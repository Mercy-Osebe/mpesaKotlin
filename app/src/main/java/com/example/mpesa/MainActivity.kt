package com.example.mpesa

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mpesa.pojos.AccessTokenResponse
import com.example.mpesa.services.AccessTokenService
import okhttp3.Credentials
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var payMe:Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        payMe=findViewById(R.id.pay_me)
        payMe.setOnClickListener {
            val baseUrl ="https://sandbox.safaricom.co.ke/mpesa/"
            val retrofit=Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
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
                      println(data!!.accessData)
                      println("hello")
                  }
                }

                override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                    println("failed")
                }
            })

        }

    }
    private fun credentials(): String {
        val consumer_Key = "cHedtdUEWvmZFgT7baOe6mymyDh4a2pf"
        val consumer_Secret = "j9bGiSy2G0UDWEOW"

        return Credentials.basic(consumer_Key, consumer_Secret)
    }
}