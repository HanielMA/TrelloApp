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
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.exception.TrelloHttpException;
import com.julienvey.trello.impl.TrelloImpl;
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

    	Authentication authentication = null;
    	final UserParams params = new ObjectMapper().readValue(request.getInputStream(), UserParams.class);
        
        try {
        	//TODO Trello Developer API KEYS should be in a properties file
        	Trello trello = new TrelloImpl("2b07d968e083f4a28a283a9370df0c26", params.getPassword().get());
        	Member member = trello.getMemberInformation(params.getEmail().get());
        	logger.info("El usuario con email " + member.getEmail() + " esta autorizado por Trello");
            
            createOrUpdateUsers(params);
            
            final UsernamePasswordAuthenticationToken loginToken = params.toAuthenticationToken();
            authentication = getAuthenticationManager().authenticate(loginToken);
            
        } catch (TrelloHttpException e) {
        	
        	logger.error("El usuario con email " + params.getEmail().get() + " no esta utenticado por Trello.");
        	new org.apache.http.auth.AuthenticationException("Error Autentication with Trello.");
        }
        
		return authentication;   
    }
    
    /**
    * Every time we authenticate with Trello, 
    * we check if we are registered in our database.
    * If we are registered we update,
    * but we register a new field.
    * And we always update the password,
    * which really is a token of the Trello API
    * @param params
    */
	private void createOrUpdateUsers(final UserParams params) {
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
