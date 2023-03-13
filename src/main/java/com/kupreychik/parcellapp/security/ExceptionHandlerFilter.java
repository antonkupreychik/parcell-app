package com.kupreychik.parcellapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kupreychik.parcellapp.controller.ExceptionController;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final ExceptionController exceptionController;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CredentialsExpiredException ex) {
            ResponseEntity<UiErrorDTO> responseEntity = exceptionController.handleCredentialsExpiredException(ex);
            setResponseData(response, responseEntity);
        } catch (InternalAuthenticationServiceException ex) {
            ResponseEntity<UiErrorDTO> responseEntity = exceptionController.handleInternalAuthenticationServiceException(ex);
            setResponseData(response, responseEntity);
        }
    }

    private void setResponseData(HttpServletResponse response, ResponseEntity<UiErrorDTO> responseEntity) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
    }
}
