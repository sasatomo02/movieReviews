package com.example.movie.config;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CloudSqlCredentials {

    @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
    private String encodedCredentials;

    public JSONObject getCredentials() throws JSONException, IOException {
        // ファイルから読み込むのではなく、環境変数の文字列を直接デコード
        byte[] decodedBytes = Base64.decodeBase64(encodedCredentials);
        String decodedString = new String(decodedBytes);
        return new JSONObject(decodedString);
    }

    // ... (必要に応じて他のメソッド)
}