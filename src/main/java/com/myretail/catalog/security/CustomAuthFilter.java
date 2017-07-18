package com.myretail.catalog.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomAuthFilter extends OncePerRequestFilter {

	private static final String AUTH_TOKEN = "Auth-Token";
	private AuthenticationManager authenticationManager;

	public CustomAuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(AUTH_TOKEN);

		PreAuthenticatedAuthenticationToken authToken = new PreAuthenticatedAuthenticationToken(token, null);
		try {
			Authentication authResult = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authResult);
			filterChain.doFilter(request, response);
		} catch (AuthenticationException authException) {
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		}

	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		return HttpMethod.GET.name().equals(request.getMethod());
	}
	

}
