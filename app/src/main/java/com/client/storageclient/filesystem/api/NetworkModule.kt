package com.client.storageclient.filesystem.api

import android.util.Log
import androidx.compose.runtime.MutableState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun sendRequest(
    rStateName: MutableState<String>,
    rStateAge: MutableState<Int>,
    rStateProgress: MutableState<Boolean>
) {
    rStateProgress.value = true

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.agify.io")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(AgeApi::class.java)

    val call: Call<AgeModel?>? = api.getAgeByName(rStateName.value)

    call!!.enqueue(
        object : Callback<AgeModel?> {
            override fun onResponse(call: Call<AgeModel?>, response: Response<AgeModel?>) {
                if (response.isSuccessful) {
                    Log.d("TAG", "" + response.body())
                    rStateProgress.value = false
                    rStateAge.value = response.body()!!.age
                }
            }

            override fun onFailure(call: Call<AgeModel?>, response: Throwable) {
                Log.d("TAG", "Failure")
                rStateProgress.value = false
            }
        }
    )
}