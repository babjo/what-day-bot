package com.babjo.whatdaybot.naver.openapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class URLShortenerClient {

    var clientId: String? = null
    var clientSecret: String? = null

    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val logger = LoggerFactory.getLogger(URLShortenerClient::class.java)

    fun shorten(url: String): String {
        try {
            val apiURL = "https://openapi.naver.com/v1/util/shorturl"
            val con = URL(apiURL).openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("X-Naver-Client-Id", clientId)
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
            // post request
            val postParams = "url=$url"
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode = con.responseCode
            val br = if (responseCode == 200) { // 정상 호출
                BufferedReader(InputStreamReader(con.inputStream))
            } else {  // 에러 발생
                BufferedReader(InputStreamReader(con.errorStream))
            }
            val response = br.use(BufferedReader::readText)
            if (responseCode != 200) {
                logger.error("Fail to shorten by {}", response)
                return url
            }

            val parsed = objectMapper.readValue(response, Response::class.java)
            return parsed.result.url
        } catch (e: IOException) {
            logger.error("Fail to shorten", e)
            return url
        }
    }

    data class Response(val message: String, val result: Result, val code: Int)
    data class Result(val hash: String, val url: String, val orgUrl: String)
}