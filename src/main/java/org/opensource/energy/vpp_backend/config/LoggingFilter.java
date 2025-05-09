package org.opensource.energy.vpp_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_LENGTH = 1000;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        Instant start = Instant.now();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = java.time.Duration.between(start, Instant.now()).toMillis();

            String requestBody = getBody(wrappedRequest.getContentAsByteArray());
            log.info("Request -> {} {} | Body: {}", request.getMethod(), request.getRequestURI(), requestBody);

            String responseBody = getBody(wrappedResponse.getContentAsByteArray());
            log.info("Response -> {} {} | Status: {} | Time: {}ms | Body: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    wrappedResponse.getStatus(),
                    duration,
                    responseBody
            );

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getBody(byte[] content) {
        if (content == null || content.length == 0) return "[empty]";

        String body = new String(content, StandardCharsets.UTF_8);
        return body.length() > MAX_BODY_LENGTH ? body.substring(0, MAX_BODY_LENGTH) + "..." : body;
    }
}
