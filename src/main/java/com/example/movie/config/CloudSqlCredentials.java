package com.example.movie.config;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CloudSqlCredentials {

    @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
    private String encodedCredentials;

    public JSONObject getCredentials() throws JSONException {
        byte[] decodedBytes = Base64.decodeBase64(encodedCredentials);
        String decodedString = new String(decodedBytes);
        return new JSONObject(decodedString);
    }
}