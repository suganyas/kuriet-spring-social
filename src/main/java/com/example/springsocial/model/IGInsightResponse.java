package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IGInsightResponse {
    private IGAccountResponse igAccountResponse;
    private IGMediaResponse igMediaResponse;
    private IGMediaResponse igStoryResponse;
}

