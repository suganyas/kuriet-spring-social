package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IGMediaResponse {
    private List<Data> data;
    private Paging paging;
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private String caption;
        private String id;
        private Long like_count;
        private Long comments_count;
        private String media_type;
        private String media_url;
        private String permalink;
        private String thumbnail_url;
        private String timestamp;
        private String username;
        private Map mediaInsights = Maps.newHashMap();
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Paging {
        private Cursors cursors;
        private String next;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Cursors{
        private String before;
        private String after;
    }
}

