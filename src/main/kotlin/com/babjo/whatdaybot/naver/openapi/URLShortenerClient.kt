package com.babjo.whatdaybot.naver.openapi

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UrlService {
    @POST("/v1/util/shorturl")
    @FormUrlEncoded
    fun shorten(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Field("url") url: String
    ): Single<Response>
}

data class Response(val message: String, val result: Result, val code: Int)
data class Result(val hash: String, val url: String, val orgUrl: String)

class URLShortenerClient {
    var clientId: String? = null
    var clientSecret: String? = null

    // TODO: Extract to Context.kt
    private var urlService: UrlService = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(UrlService::class.java)

    fun shorten(url: String): Single<String> {
        if (this.clientId == null || this.clientSecret == null) {
            return Single.just(url)
        }
        return urlService.shorten(this.clientId!!, this.clientSecret!!, url).map { it.result.url }
    }
}
