package com.example.mediasoftjwt.jwt;

import com.example.mediasoftjwt.requests.MutableHttpServletRequest;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;


@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        MutableHttpServletRequest mutableRequest = null;
        if (header != null) {
            String[] elements = header.split(" ");

            if (elements.length == 2 && "Bearer".equals(elements[0])) {
                try {
                    Authentication authentication = userAuthProvider.validateToken(elements[1]);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    mutableRequest = new MutableHttpServletRequest(request);
                    mutableRequest.putHeader("email-header", (String) authentication.getPrincipal());

                } catch (RuntimeException | JOSEException | ParseException e) {
                    SecurityContextHolder.clearContext();
                    try {
                        throw e;
                    } catch (JOSEException exc) {
                        exc.printStackTrace();
                    } catch (ParseException exc) {
                        System.out.println("НЕВАЛИДНЫЙ ТОКЕН");
                    }
                }
            }
        }

        if (mutableRequest != null) {
            filterChain.doFilter(mutableRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
