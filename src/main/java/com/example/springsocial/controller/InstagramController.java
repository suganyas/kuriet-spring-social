package com.example.springsocial.controller;

import com.example.springsocial.exception.InstagramException;
import com.example.springsocial.model.AccessTokenModel;
import com.example.springsocial.model.IGMediaResponse;
import com.example.springsocial.service.InstagramService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope("session")
public class InstagramController {
	private InstagramService service;


	@Autowired
	public InstagramController(InstagramService instagramService) {
		this.service = instagramService;
	}

	@RequestMapping(value = "/getPostAndInsights", method = {RequestMethod.GET}, produces = "application/json")
	public ResponseEntity<IGMediaResponse> getPostInsights(
			@ApiParam(value = "AccessCode for the Instagram Account")
			@RequestParam(value = "AccessCode")
					String accessCode
	) {
		IGMediaResponse mediaResponseList = null;
		try {
			AccessTokenModel accessToken = service.getAccessToken(accessCode);
			System.out.println(accessToken.getAccess_token() + " " + accessToken.getUser_id());
			mediaResponseList = service.getUserMediaContents(accessToken);

		}catch (InstagramException exception) {
			System.out.println(exception.getMessage());
		}

		return new ResponseEntity<IGMediaResponse>(mediaResponseList, HttpStatus.OK);
	}
}