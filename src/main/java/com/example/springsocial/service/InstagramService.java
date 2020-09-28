package com.example.springsocial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springsocial.exception.InstagramException;
import com.example.springsocial.model.AccessTokenModel;
import com.example.springsocial.model.IGMediaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


@Service
public class InstagramService {
    @Value("${instagram.secret.id}")
    private String clientSecretId;
    @Value("${instagram.client.id}")
    private String clientId;
    @Value("${instagram.redirect.auth.link}")
    private String callbackURL;
    @Value("${instagram.accesstoken.endpoint}")
    private String igAcesstokenURL;
    @Value("${instagram.usermedia.endpoint}")
    private String iguserMediaURL;

    public AccessTokenModel getAccessToken(String accessCode) {
        HashMap values = new HashMap<String, String>() {{
            put("client_id", clientId);
            put ("client_secret", clientSecretId);
            put ("redirect_uri", callbackURL);
            put ("grant_type", "authorization_code");
            put ("code", accessCode);

        }};
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(igAcesstokenURL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(values))
                .build();
        String responseMessage = "";
        try {
    HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
    responseMessage = response.body();
    if (response.statusCode() == 200) {
        return new ObjectMapper().readValue(responseMessage, AccessTokenModel.class);
    }
}catch (IOException | InterruptedException exception) {
    throw new InstagramException("Access Token cannot be retrived" + responseMessage);
}
        throw new InstagramException("Access Token cannot be retrived" + responseMessage);

    }
    private static HttpRequest.BodyPublisher ofFormData(HashMap<String, String> data) {
        var builder = new StringBuilder();
        for (HashMap.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder
                    .append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder
                    .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public IGMediaResponse getUserMediaContents(AccessTokenModel accessTokenModel)  {
        HttpClient client = HttpClient.newHttpClient();
        String params = "?fields=caption,id,media_type,media_url,permalink,thumbnail_url,timestamp,username&access_token=" + accessTokenModel.getAccess_token();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(iguserMediaURL + params))
                .GET()
                .build();
        String responseMessage = "";

        try {
    HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
    responseMessage = response.body();
    if (response.statusCode() == 200) {
        return new ObjectMapper().readValue(responseMessage, IGMediaResponse.class);
    }
}catch (IOException | InterruptedException exception) {
    throw new InstagramException("Access Token cannot be retrived" + responseMessage);
}
        throw new InstagramException("userMedia cannot be retrived" + responseMessage);
    }


}
