package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenRequest {
    private String client_id;
    private String client_secret;
    private String grant_type;
    private String redirect_uri;
    private String code;

}