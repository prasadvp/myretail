package com.myretail.catalog.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;



public class CustomAuthProvider implements AuthenticationProvider {

	private static final String AUTH_ERR_MSG = "Invalid token or token expired";
	private static final String TOKEN = "password";
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication)  {
		
		String token = (String) authentication.getPrincipal();
		
		if (TOKEN.equals(token)) {
			LOGGER.info("Authentication successful");
			return authentication;
		}
		else {
			throw new BadCredentialsException(AUTH_ERR_MSG);
		}
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}

}
