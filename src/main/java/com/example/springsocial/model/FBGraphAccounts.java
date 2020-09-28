package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class FBGraphAccounts {
    private List<Data> data;
    private Paging paging;
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private String access_token;
        private String name;
        private String id;
        private String category;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Paging {
        private IGMediaResponse.Cursors cursors;
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
