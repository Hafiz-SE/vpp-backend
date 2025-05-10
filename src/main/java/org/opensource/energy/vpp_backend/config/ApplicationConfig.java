package org.opensource.energy.vpp_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.opensource.energy.vpp_backend.constant.SwaggerConstants.*;

@Configuration
public class ApplicationConfig {

    // TODO: Set the correct origin once the frontend URL is available
    @Value("${cors.allowed-origin:*}")
    private String allowedOrigin;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public OpenAPI swaggerConfig(@Value("${server.servlet.context-path:}") String contextPath) {
        return new OpenAPI()
                .info(new Info()
                        .title(VPP_API_TITLE)
                        .version(VPP_API_VERSION)
                        .description(VPP_API_DESCRIPTION))
                .servers(List.of(
                        new Server().url(contextPath.isEmpty() ? "/" : contextPath)));
    }
}
