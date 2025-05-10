package org.opensource.energy.vpp_backend.unit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensource.energy.vpp_backend.filter.LoggingFilter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LoggingFilterTest {
    private LoggingFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        filter = new LoggingFilter();
        filterChain = mock(FilterChain.class);
    }

    @Test
    void given_request_when_filtered_then_logs_request_and_response() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/test");
        mockRequest.setContent("hello".getBytes());

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        filter.doFilter(mockRequest, mockResponse, filterChain);

        // then
        verify(filterChain).doFilter(any(), any());
    }
}
