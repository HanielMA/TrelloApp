package com.trelloapp.auth;


import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trelloapp.dto.UserDTO;
import com.trelloapp.dto.UserParams;
import com.trelloapp.service.UserService;

class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserService userService;

    StatelessLoginFilter(String urlMapping,
                         TokenAuthenticationService tokenAuthenticationService,
                         UserService userService,
                         AuthenticationManager authenticationManager) {
        super(urlMapping);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userService = userService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final UserParams params = new ObjectMapper().readValue(request.getInputStream(), UserParams.class);
        
        /* Every time we authenticate with Trello, 
         * we check if we are registered in our database.
         * If we are registered we update,
         * but we register a new field.
         * And we always update the password,
         * which really is a token of the Trello API.
         */
        
        Optional<UserDTO> userDTO = userService.findOneByUsername(params.getEmail().orElseThrow(() -> new UsernameNotFoundException("Error in the email field")));
    	
        userDTO.ifPresent(theUser -> {
        	userService.update(theUser, params);
			logger.debug("El usuario con email " + theUser.getEmail() +" ha sido actualizado");
    	});
        
        userDTO.orElseGet(() -> {
        	UserDTO newUserDTO = userService.create(params);
			logger.debug("El usuario con email " + newUserDTO.getEmail() +"  ha sido creado." );
			return newUserDTO;
        });
        
        final UsernamePasswordAuthenticationToken loginToken = params.toAuthenticationToken();
        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

    	final UserDetails authenticatedUser = userService.loadUserByUsername(authResult.getName());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
 
    }

    
}
