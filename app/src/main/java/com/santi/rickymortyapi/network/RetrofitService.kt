package com.santi.rickymortyapi.network

import com.santi.rickymortyapi.model.FullLocation
import com.santi.rickymortyapi.model.RickyMortyCharacter
import com.santi.rickymortyapi.utils.Constants.END_POINT
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET(END_POINT)
    suspend fun getCharacters(@Query("page") query: Int) : RickyMortyCharacter
    @GET("location/{id}")
    suspend fun getLocation(@Path("id") id: String) : FullLocation
}