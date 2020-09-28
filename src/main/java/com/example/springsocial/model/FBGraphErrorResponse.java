package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class FBGraphErrorResponse {
    Error error;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static  class Error {
        private String message;
        private String type;
        private float code;
        private float error_subcode;
        private boolean is_transient;
        private String error_user_title;
        private String error_user_msg;
        private String fbtrace_id;
    }
}
