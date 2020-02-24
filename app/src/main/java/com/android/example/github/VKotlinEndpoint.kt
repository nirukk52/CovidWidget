package com.verkada.endpoint.kotlin

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
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
import javax.inject.Singleton


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
@Singleton
object VKotlinEndpoint {
    private const val BASE_URL = "http://ec2-54-187-236-58.us-west-2.compute.amazonaws.com:8021"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
    }

    fun searchMotion(motionSearchBody: MotionSearchBody, callback: CompletionCallback?) {
        val api = retrofit.create(Api::class.java)

        api.motionSearch(motionSearchBody).enqueue(object : Callback<MotionSearchResponse> {
            override fun onFailure(call: Call<MotionSearchResponse>, t: Throwable) {
                callback?.invoke(emptyList(), t)
            }

            override fun onResponse(call: Call<MotionSearchResponse>, response: Response<MotionSearchResponse>) {
                Log.d("Call ", call.request().url().toString())
                Log.d("Call ", call.request().body().toString())
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
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(row)
        writeInt(col)
        writeInt((if (selected) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cell> = object : Parcelable.Creator<Cell> {
            override fun createFromParcel(source: Parcel): Cell = Cell(source)
            override fun newArray(size: Int): Array<Cell?> = arrayOfNulls(size)
        }
    }
}
