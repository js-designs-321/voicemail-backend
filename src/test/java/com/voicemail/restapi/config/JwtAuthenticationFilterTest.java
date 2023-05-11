package com.voicemail.restapi.config;

import com.voicemail.restapi.enums.Role;
import com.voicemail.restapi.model.Token;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.TokenRepository;
import com.voicemail.restapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Test
    public void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String authHeader = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername("invalidToken")).thenReturn(null);
        when(request.getServletPath()).thenReturn("/voicemail/v1/draft");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_NullAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getServletPath()).thenReturn("/voicemail/v1/draft");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_NoAuthentication() throws ServletException, IOException {
        String authHeader = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getServletPath()).thenReturn("/voicemail/v1/draft");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.empty());
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_ValidAuthentication() throws ServletException, IOException {
        String authHeader = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getServletPath()).thenReturn("/voicemail/v1/draft");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(new Token()));
        UserDetails userDetails = User.builder()
                .firstname("first_name")
                .lastname("last_name")
                .email("user@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        ArgumentCaptor<UsernamePasswordAuthenticationToken> argumentCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(securityContext, times(1)).setAuthentication(argumentCaptor.capture());
        assertEquals(userDetails, argumentCaptor.getValue().getPrincipal());
    }

}
