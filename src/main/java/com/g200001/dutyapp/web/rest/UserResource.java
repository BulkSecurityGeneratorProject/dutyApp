package com.g200001.dutyapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.domain.User;
import com.g200001.dutyapp.domain.UserDeviceLoginInfo;
import com.g200001.dutyapp.domain.UserDeviceLoginResult;
import com.g200001.dutyapp.repository.UserRepository;
import com.g200001.dutyapp.security.AuthoritiesConstants;
import com.g200001.dutyapp.security.xauth.Token;
import com.g200001.dutyapp.security.xauth.TokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserDetailsService userDetailsService;
    @Inject
    private UserRepository userRepository;

    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll() {
        log.debug("REST request to get all Users");
        return userRepository.findAll();
    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public User getUser(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to get User : {}", login);
        User user = userRepository.findOneByLogin(login);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return user;
    }
    
    /**
     * POST  /User/Login -> User Login.
     */
    @RequestMapping(value = "/User/Login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDeviceLoginResult> Login(@RequestBody UserDeviceLoginInfo loginInfo) throws URISyntaxException {
        log.debug("REST request to LoginUser : {}", loginInfo);
        UserDeviceLoginResult rst = new UserDeviceLoginResult();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginInfo.getName(),loginInfo.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = this.userDetailsService.loadUserByUsername(loginInfo.getName());
        Token atoken= tokenProvider.createToken(details);
        rst.setToken(atoken);
        rst.setUserID(userRepository.findOneByLogin(loginInfo.getName()).getId());
        return new ResponseEntity<>(rst,HttpStatus.OK);
    }
    
    
}
