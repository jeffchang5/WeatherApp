package me.jeffreychang.giphyapp.ui

interface GiphyService {

    @GET("v1/gifs/trending")
    fun getTrending() {

    }
}