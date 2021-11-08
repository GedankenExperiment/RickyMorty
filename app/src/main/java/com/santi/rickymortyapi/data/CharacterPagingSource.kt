package com.santi.rickymortyapi.data

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.santi.rickymortyapi.model.FullCharacter
import com.santi.rickymortyapi.model.FullLocation
import com.santi.rickymortyapi.network.RetrofitService
import java.lang.Exception

class CharacterPagingSource (private val retrofitService: RetrofitService) : PagingSource<Int, FullCharacter>() {
    override fun getRefreshKey(state: PagingState<Int, FullCharacter>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FullCharacter> {
        return try {
            val nextPage : Int = params.key ?: FIRST_PAGE_INDEX
            val response = retrofitService.getCharacters(nextPage)
            var nextPageNumber : Int? = null

            if (response.info.next != null){
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(data = response.results, prevKey = null, nextKey = nextPageNumber)

        }catch(e: Exception){
            LoadResult.Error(e)
        }
    }

    companion object{
        private const val FIRST_PAGE_INDEX = 1
    }

    // Character by ID
    suspend fun getLocation(locationUrl: String): FullLocation {
        return try {
            val response = retrofitService.getLocation(locationUrl.replace("https://rickandmortyapi.com/api/location/", ""))
            FullLocation(created = response.created,
                dimension = response.dimension,
                id = response.id,
                name = response.name,
                type = response.type,
                url = response.url)
        }catch(e: Exception){
            FullLocation("error",
            "error", -1, "name",
            "error", "error")
        }
    }
}