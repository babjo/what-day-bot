package com.babjo.whatdaybot.naver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

public class URLShortener {

    private String clientId;
    private String clientSecret;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static Logger logger = LoggerFactory.getLogger(URLShortener.class);

    public String shorten(String url) {
        try {
            String apiURL = "https://openapi.naver.com/v1/util/shorturl";
            HttpURLConnection con = (HttpURLConnection) new URL(apiURL).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "url=" + url;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            if (responseCode != 200) {
                logger.error("Fail to shorten by {}", response.toString());
                return url;
            }

            Response parsed = objectMapper.readValue(response.toString(), Response.class);
            return parsed.getResult().getUrl();
        } catch (IOException e) {
            logger.error("Fail to shorten", e);

            return url;
        }
    }

    @Data
    static class Response {
        private String message;
        private Result result;
        private int code;

        @Data
        static class Result {
            private String hash;
            private String url;
            private String orgUrl;
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
