package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IGAccountResponse {

    private Long follows_count;
    private Long followers_count;
    private String profile_picture_url;
    private String name;
    private String username;
    private Long media_count;
    private List<Data> data;
    private Map<String,Integer> hashTagUsage = new HashMap();
    private Map<String,Integer> hashTagInsights = new HashMap();
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String name;
        private String period;
        private List<Values> values;
        private String title;
        private String description;
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Values
        {
            private Map value = new HashMap();
            private String end_time;

        }
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HashTagMetrics
    {
        private Integer engagement;
        private Integer used;

    }

}

