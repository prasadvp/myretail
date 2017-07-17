package com.myretail.catalog.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class CustomAuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String token = (String) authentication.getPrincipal();
		// TODO Auto-generated method stub
		System.out.println("From request " + token);
		if ("password".equals(token)) {
			System.out.println("SUccess ");
			return authentication;
		}
		else {
			throw new BadCredentialsException("Invalid token or token expired");
		}
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}

}
