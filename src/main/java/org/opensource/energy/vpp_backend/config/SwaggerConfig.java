package org.opensource.energy.vpp_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.opensource.energy.vpp_backend.constant.SwaggerConstants.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vppOpenAPI(@Value("${server.servlet.context-path:}") String contextPath) {
        return new OpenAPI()
                .info(new Info()
                        .title(VPP_API_TITLE)
                        .version(VPP_API_VERSION)
                        .description(VPP_API_DESCRIPTION))
                .servers(List.of(
                        new Server().url(contextPath.isEmpty() ? "/" : contextPath)));
    }
}
