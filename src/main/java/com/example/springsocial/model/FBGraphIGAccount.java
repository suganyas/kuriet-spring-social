package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBGraphIGAccount {
    @JsonProperty(value = "instagram_business_account")
    private InstagramBusinessAccount instagramBusinessAccount;
    private long id;
    @Getter
    @Setter
    @NoArgsConstructor
    public static class InstagramBusinessAccount{
        private long id;
    }
}
