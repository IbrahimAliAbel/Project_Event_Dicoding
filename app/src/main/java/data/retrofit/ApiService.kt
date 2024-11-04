package data.retrofit

import data.response.EventDetailResponse
import data.response.Responsedicoding
import retrofit2.Call
import retrofit2.http.*

interface ApiService  {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<Responsedicoding>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<EventDetailResponse>

    @GET("events")
    fun searchEvents(@Query("q") keyword: String): Call<Responsedicoding>
}