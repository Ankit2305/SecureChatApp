package com.ankitkumar.securechatapplication.network

import androidx.lifecycle.LiveData
import com.ankitkumar.securechatapplication.model.GroupDetail
import retrofit2.http.GET
import retrofit2.http.Path


interface ChatApiService {

    @GET("/group/{groupId}")
    suspend fun fetchGroupById(@Path("groupId") groupId: String): GroupDetail


}