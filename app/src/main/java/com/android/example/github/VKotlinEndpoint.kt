package com.verkada.endpoint.kotlin

import androidx.lifecycle.ViewModel
import com.android.example.github.api.VJavaEndpoint
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


/**
 * Dependencies in build.gradle
 *  implementation "com.squareup.retrofit2:retrofit:2.4.0"
 *  implementation "com.squareup.retrofit2:converter-moshi:2.4.0"
 *  implementation "com.squareup.moshi:moshi:1.8.0"
 *  kapt "com.squareup.moshi:moshi-kotlin-codegen:1.8.0"
 *
 *  apply plugin: 'kotlin-kapt'
 *
 */
object VKotlinEndpoint : ViewModel() {
    private const val BASE_URL = "http://ec2-54-187-236-58.us-west-2.compute.amazonaws.com:8021"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
    }

    fun searchMotion(row: Int, column: Int, startDate: Date, timeInterval: Long = 3600, callback: CompletionCallback?) {
        val api = retrofit.create(Api::class.java)

        val startTimeSec: Long = 1582265703
        val list: MutableList<List<Int>> = ArrayList()
        for (i in 0..8) {
            val motion: MutableList<Int> = ArrayList()
            motion.add(i)
            motion.add(i)
            list.add(motion)
        }

        val body: MotionSearchBody = MotionSearchBody(
                list,
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
    @Headers("Content-Type: application/json")
    @POST("/ios/search")
    fun motionSearch(@Body motionSearchRequest: MotionSearchBody): Call<MotionSearchResponse>
}

@JsonClass(generateAdapter = true)
data class MotionSearchBody(
        val motionZones: List<List<Int>>,
        val startTimeSec: Long,
        val endTimeSec: Long
)

@JsonClass(generateAdapter = true)
data class MotionSearchResponse(
        internal var motionAt: List<List<Long>>,
        val nextEndTimeSec: Int?
) {
    fun getMotionAt(): List<Motion> {
        val list = mutableListOf<Motion>()
        for (motion in motionAt) {
            list.add(Motion(Date(motion[0] * 1000), motion[1]))
        }
        return list
    }
}

@JsonClass(generateAdapter = true)
data class Motion(
        val date: Date,
        val durationSeconds: Long
)

@JsonClass(generateAdapter = true)
data class Cell(
        val row: Int,
        val col: Int,
        var selected: Boolean = false
)
