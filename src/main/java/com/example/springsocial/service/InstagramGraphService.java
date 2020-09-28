package com.example.springsocial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springsocial.exception.InstagramException;
import com.example.springsocial.model.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class InstagramGraphService {
    @Value("${facebook.graph.secret.id}")
    private String clientSecretId;
    @Value("${facebook.graph.client.id}")
    private String clientId;
    @Value("${instagram.redirect.auth.link}")
    private String callbackURL;
    @Value("${facebook.graph.longlife.token.endpoint}")
    private String longLifeTokenURL;
    @Value("${facebook.graph.endpoint}")
    private String fbGraphEndpoint;
    @Value("${facebook.graph.accounts.pathparams}")
    private String fbGraphAccounts;
    @Value("${facebook.graph.media.pathparams}")
    private String fbGraphMedia;
    @Value("${facebook.graph.stories.pathparams}")
    private String fbGraphStories;
    @Value("${facebook.graph.IGaccount.pathparams}")
    private String igAccount;
    @Value("${facebook.graph.image.media.insight.pathparams}")
    private String fbGraphImageMedia;
    @Value("${facebook.graph.video.media.insight.pathparams}")
    private String fbGraphVideoMedia;
    @Value("${facebook.graph.story.media.insight.pathparams}")
    private String fbGraphStoryMedia;
    @Value("${facebook.graph.album.media.insight.pathparams}")
    private String fbGraphAlbumMedia;
    @Value("${facebook.graph.ig.account.details}")
    private String fbGraphAccountDetails;
    @Value("${facebook.graph.insights.lifetime.pathparams}")
    private String fbGraphAccountInsights;


    public AccessTokenGraphModel getAccessToken(String accessCode) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(longLifeTokenURL +
                        "?grant_type=fb_exchange_token&client_id=" + clientId +
                        "&client_secret=" + clientSecretId +
                        "&fb_exchange_token=" + accessCode))
                .GET()
                .build();
        String responseMessage = "";
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
                return new ObjectMapper().readValue(responseMessage, AccessTokenGraphModel.class);
            }
        } catch (IOException | InterruptedException exception) {
            throw new InstagramException("Access Token cannot be retrived" + responseMessage);
        }
        throw new InstagramException("Access Token cannot be retrived" + responseMessage);

    }

    public FBGraphAccounts.Data getPageAccessToken(String accessToken) {
        HttpClient client = HttpClient.newHttpClient();
        FBGraphAccounts graphAccounts = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fbGraphEndpoint + fbGraphAccounts
                        + accessToken))
                .GET()
                .build();
        String responseMessage = "";
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
                graphAccounts = new ObjectMapper().readValue(responseMessage, FBGraphAccounts.class);
            }
            if (graphAccounts != null) {
                for (FBGraphAccounts.Data data : graphAccounts.getData()) {
                    if (!StringUtils.isEmpty(data.getCategory()) && data.getCategory().equalsIgnoreCase("Advertising/Marketing")) {
                        return data;
                    }
                }
            }
        } catch (IOException | InterruptedException exception) {
            throw new InstagramException("Page Access Token cannot be retrived" + responseMessage);
        }
        throw new InstagramException("Page Access Token cannot be retrived" + responseMessage);

    }

    public long retriveInstagramId(String pageAccessToken, String accountId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fbGraphEndpoint + "/" +
                        accountId + igAccount + pageAccessToken))
                .GET()
                .build();
        String responseMessage = "";
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
                FBGraphIGAccount fbGraphIGAccount = new ObjectMapper().readValue(responseMessage, FBGraphIGAccount.class);
                if (fbGraphIGAccount.getInstagramBusinessAccount() != null) {
                    return fbGraphIGAccount.getInstagramBusinessAccount().getId();
                }
            }
        } catch (IOException | InterruptedException exception) {
            throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
        }
        throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
    }

    public IGMediaResponse retriveIGMedia(String pageAccessToken, long IgAccountId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fbGraphEndpoint + "/" +
                        IgAccountId + fbGraphMedia + pageAccessToken))
                .GET()
                .build();
        String responseMessage = "";
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
                return (new ObjectMapper().readValue(responseMessage, IGMediaResponse.class));
            }
        } catch (IOException | InterruptedException exception) {
            throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
        }
        throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
    }

    public IGMediaResponse retriveIGStories(String pageAccessToken, long IgAccountId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fbGraphEndpoint + "/" +
                        IgAccountId + fbGraphStories + pageAccessToken))
                .GET()
                .build();
        String responseMessage = "";
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
                IGMediaResponse igStories = new ObjectMapper().readValue(responseMessage, IGMediaResponse.class);
                igStories.getData().stream().forEach(data -> data.setMedia_type("story"));
                return igStories;

            }
        } catch (IOException | InterruptedException exception) {
            throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
        }
        throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
    }

    public IGAccountResponse retriveIGAccount(String pageAccessToken, long IgAccountId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestDetails = HttpRequest.newBuilder()
                .uri(URI.create(fbGraphEndpoint + "/" +
                        IgAccountId + fbGraphAccountDetails + pageAccessToken))
                .GET()
                .build();
        HttpRequest requestInsight = HttpRequest.newBuilder()
                .uri(URI.create(fbGraphEndpoint + "/" +
                        IgAccountId + fbGraphAccountInsights + pageAccessToken))
                .GET()
                .build();
        String responseMessage = "";
        IGAccountResponse igAccountResponse = new IGAccountResponse();
        try {
            HttpResponse<String> response = client.send(requestDetails,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
               igAccountResponse =  new ObjectMapper().readValue(responseMessage, IGAccountResponse.class);
            }
            response = client.send(requestInsight,
                    HttpResponse.BodyHandlers.ofString());
            responseMessage = response.body();
            if (response.statusCode() == 200) {
                IGAccountResponse data = new ObjectMapper().readValue(responseMessage,IGAccountResponse.class);
                igAccountResponse.setData(data.getData());
            }
            return igAccountResponse;
        } catch (IOException | InterruptedException exception) {
            throw new InstagramException("FB linked Instagram account id cannot be retrived" + responseMessage);
        }
    }

    public void addMediaInsights(IGMediaResponse igMediaResponse, String pageAccessToken, long IgAccountId) {
        final Map<String, String> mediaInsightMap = new HashMap<String, String>() {{
            put("IMAGE", fbGraphImageMedia);
            put("VIDEO", fbGraphVideoMedia);
            put("CAROUSEL_ALBUM", fbGraphAlbumMedia);
            put("STORY", fbGraphStoryMedia);
        }};
        HttpClient client = HttpClient.newHttpClient();
        if (igMediaResponse != null) {
            for (IGMediaResponse.Data data : igMediaResponse.getData()) {
                String mediaParams = mediaInsightMap.get(data.getMedia_type().toUpperCase());
                if (mediaParams == null) {
                    throw new InstagramException("unsupported media type returned from API " + data.getMedia_type());
                }
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(fbGraphEndpoint + "/" +
                                data.getId() + mediaParams + pageAccessToken))
                        .GET()
                        .build();
                String responseMessage = "";
                try {
                    HttpResponse<String> response = client.send(request,
                            HttpResponse.BodyHandlers.ofString());
                    responseMessage = response.body();
                    if (response.statusCode() == 200) {
                        IGMediaInsightResponse igMediaInsight = new ObjectMapper().readValue(responseMessage, IGMediaInsightResponse.class);
                        if (igMediaInsight != null && !igMediaResponse.getData().isEmpty()) {
                            for (IGMediaInsightResponse.Data insightData : igMediaInsight.getData())
                                data.getMediaInsights().put(insightData.getName(), insightData.getValues().get(0));
                        }
                    } else{
                        FBGraphErrorResponse errResp = new ObjectMapper().readValue(responseMessage, FBGraphErrorResponse.class);
                        data.getMediaInsights().put("Error", errResp.getError().getError_user_msg());
                    }
                } catch (IOException | InterruptedException exception) {
                    throw new InstagramException("FB Media Insights cannot be retrived" + responseMessage);
                }
            }
        }
    }
    public void addHashTagInsights(IGMediaResponse igMediaResponse,IGAccountResponse igAccountResponse) {
        for(IGMediaResponse.Data igMediaData: igMediaResponse.getData()) {
            if(!StringUtils.isEmpty(igMediaData.getCaption())) {
                List<String> hashTags = extractHashtags(igMediaData.getCaption());
                for(String hashtag:hashTags) {
                    if (igAccountResponse.getHashTagInsights().get(hashtag) == null) {
                        igAccountResponse.getHashTagInsights().put(hashtag,igMediaData.getComments_count().intValue() + igMediaData.getLike_count().intValue());
                        igAccountResponse.getHashTagUsage().put(hashtag,1);
                    } else{
                        igAccountResponse.getHashTagInsights().put(hashtag, igAccountResponse.getHashTagInsights().get(hashtag)+igMediaData.getComments_count().intValue()+ igMediaData.getLike_count().intValue());
                        igAccountResponse.getHashTagUsage().put(hashtag, igAccountResponse.getHashTagUsage().get(hashtag)+1);
                    }
                }
            }
        }
        for(Map.Entry<String,Integer> insight: igAccountResponse.getHashTagInsights().entrySet()) {
            igAccountResponse.getHashTagInsights().put(insight.getKey(), insight.getValue()/(igAccountResponse.getHashTagUsage().get(insight.getKey())));
        }
        igAccountResponse.setHashTagInsights( igAccountResponse.getHashTagInsights().entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
        igAccountResponse.setHashTagUsage( igAccountResponse.getHashTagUsage().entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
    }

    private List<String> extractHashtags(String caption) {
        List<String> hashTags = Lists.newArrayList();
        String regexPattern = "(#\\w+)";

        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(caption);
        while (m.find()) {
            hashTags.add(m.group(1));
        }
        return (hashTags.stream()
                .distinct()
                .collect(Collectors.toList()));
    }
}


