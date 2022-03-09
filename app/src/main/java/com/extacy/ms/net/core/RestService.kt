package com.extacy.ms.net.core
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @Headers("Content-Type: application/json")
    @POST
    fun request(@Url endPoint: String, @Body body: String): Call<String>
}

interface PutService {
    @Headers("Content-Type: application/json")
    @PUT
    fun request(@Url endPoint: String, @Body body:String) : Call<String>
}

interface GetService {
    @Headers("Content-Type: application/json")
    @GET
    fun request(@Url endPoint: String) : Call<String>
}

interface DeleteService {
    @Headers("Content-Type: application/json")
    @DELETE
    fun request(@Url endPoint: String) : Call<String>
}