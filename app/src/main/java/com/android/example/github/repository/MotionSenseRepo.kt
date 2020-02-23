package com.android.example.github.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.github.AppExecutors
import com.android.example.github.api.GithubService
import com.android.example.github.db.GithubDb
import com.android.example.github.db.RepoDao
import com.android.example.github.vo.Resource
import com.android.example.github.vo.User
import com.verkada.endpoint.kotlin.Motion
import com.verkada.endpoint.kotlin.MotionSearchBody
import com.verkada.endpoint.kotlin.MotionSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MotionSenseRepo @Inject constructor(
        private val appExecutors: AppExecutors,
        private val githubService: GithubService
) {

    fun getMotionSearches(motionSearchBody: MotionSearchBody): LiveData<List<Motion>>{
        val liveData = MutableLiveData<List<Motion>>()
        githubService.motionSearch(motionSearchBody).enqueue(object : Callback<MotionSearchResponse> {
            override fun onFailure(call: Call<MotionSearchResponse>, t: Throwable) {
                liveData.value = emptyList<Motion>()
            }
            override fun onResponse(call: Call<MotionSearchResponse>, response: Response<MotionSearchResponse>) {
                liveData.value =  response.body()?.getMotionAt() ?: emptyList()
            }
        })
        return liveData
    }
}