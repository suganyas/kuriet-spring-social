package com.example.springsocial.controller;

import com.example.springsocial.exception.InstagramException;
import com.example.springsocial.model.AccessTokenGraphModel;
import com.example.springsocial.model.FBGraphAccounts;
import com.example.springsocial.model.IGAccountResponse;
import com.example.springsocial.model.IGInsightResponse;
import com.example.springsocial.model.IGMediaResponse;
import com.example.springsocial.model.InstagramAccess;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.InstagramAccessRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.InstagramGraphService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class InstagramGraphController {

    private InstagramGraphService service;
    private UserRepository userRepository;
    private InstagramAccessRepository instagramAccessRepository;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    public InstagramGraphController(InstagramGraphService instagramGraphService, UserRepository userRepository, InstagramAccessRepository instagramAccessRepository) {

        this.service = instagramGraphService;
        this.userRepository = userRepository;
        this.instagramAccessRepository = instagramAccessRepository;
    }

    @RequestMapping(value = "/getInstagramInsights", method = {RequestMethod.GET}, produces = "application/json")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IGInsightResponse> getPostInsights(
            @ApiParam(value = "AccessCode for the Instagram Account")
            @RequestParam(value = "accessCode")
                    String accessCode
    ) {
        IGInsightResponse igInsightResponse = new IGInsightResponse();
        try {
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userRepository.findById(principal.getId()).get();
            Optional<InstagramAccess> instagramAccessValue = instagramAccessRepository.findByUser(currentUser);
            InstagramAccess instagramAccess = null;
            if (instagramAccessValue.isPresent()) {
                instagramAccess = instagramAccessValue.get();
            }
            if (instagramAccessValue == null) {
                AccessTokenGraphModel accessToken = service.getAccessToken(accessCode);
                FBGraphAccounts.Data pageAccessToken = service.getPageAccessToken(accessToken.getAccess_token());
                long igAccountId = service.retriveInstagramId(pageAccessToken.getAccess_token(), pageAccessToken.getId());
                instagramAccess = new InstagramAccess();
                instagramAccess.setId(igAccountId);
                instagramAccess.setUser(currentUser);
                instagramAccess.setIgAccessToken(pageAccessToken.getAccess_token());
                instagramAccessRepository.save(instagramAccess);
            }
            IGMediaResponse igMediaResponse = service.retriveIGMedia(instagramAccess.getIgAccessToken(), instagramAccess.getId());
            IGMediaResponse igStoryResponse = service.retriveIGStories(instagramAccess.getIgAccessToken(), instagramAccess.getId());
            if(!igMediaResponse.getData().isEmpty()) {
                service.addMediaInsights(igMediaResponse, instagramAccess.getIgAccessToken(), instagramAccess.getId());
            }
            if(!igStoryResponse.getData().isEmpty()) {
                service.addMediaInsights(igStoryResponse, instagramAccess.getIgAccessToken(), instagramAccess.getId());
            }
            IGAccountResponse igAccountResponse= service.retriveIGAccount(instagramAccess.getIgAccessToken(), instagramAccess.getId());
            igInsightResponse.setIgAccountResponse(igAccountResponse);
            igInsightResponse.setIgMediaResponse(igMediaResponse);
            igInsightResponse.setIgStoryResponse(igStoryResponse);
            service.addHashTagInsights(igMediaResponse,igAccountResponse);
        } catch (InstagramException exception) {
            logger.log(Level.ALL, exception.getMessage());
            throw new InstagramException(exception.getMessage());
        }

        return new ResponseEntity<>(igInsightResponse, HttpStatus.OK);
    }
}