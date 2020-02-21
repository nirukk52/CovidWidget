package com.verkada.endpoint.kotlin

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.*


object VKotlinEndpoint {
    private const val BASE_URL = "http://ec2-54-187-236-58.us-west-2.compute.amazonaws.com:8021"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
    }

    fun searchMotion(row: Int, column: Int, startDate: Date, timeInterval: Long = 3600, callback: CompletionCallback?) {
        val api = retrofit.create(Api::class.java)

        val startTimeSec = startDate.time / 1000
        val body = MotionSearchBody(
            listOf(listOf(row, column)),
            startTimeSec,
            startTimeSec + timeInterval
        )
        api.motionSearch(body).enqueue(object : Callback<MotionSearchResponse> {
            override fun onFailure(call: Call<MotionSearchResponse>, t: Throwable) {
                callback?.invoke(emptyList(), t)
            }

            override fun onResponse(call: Call<MotionSearchResponse>, response: Response<MotionSearchResponse>) {
                response.body()?.let {
                    callback?.invoke(it.getMotionAt(), null)
                } ?: callback?.invoke(emptyList(), null)
            }

        })
    }
}

typealias CompletionCallback = ((List<Motion>, Throwable?) -> Unit)

interface Api {
    @Headers("Accept: application/json")
    @POST("/ios/search")
    fun motionSearch(@Body motionSearchRequest: MotionSearchBody): Call<MotionSearchResponse>
}

@JsonClass(generateAdapter = true)
data class MotionSearchBody(
    val motionZones: List<List<Int>>,
    val startTimeSec: Long,
    val endTimeSec: Long
)

data class MotionSearchResponse(
        @field:Json(name = "motionAt") internal var motionAt: List<List<Long>>,
        @field:Json(name = "nextEndTimeSec") val nextEndTimeSec: Int?
) {
    fun getMotionAt(): List<Motion> {
        val list = mutableListOf<Motion>()
//        for (motion in motionAt) {
//            list.add(Motion(Date(motion[0] * 1000), motion[1]))
//        }
        return list
    }
}

data class Motion(
    val date: Date,
    val durationSeconds: Long
)
