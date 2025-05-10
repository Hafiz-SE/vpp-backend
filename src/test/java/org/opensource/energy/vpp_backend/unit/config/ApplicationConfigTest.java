package org.opensource.energy.vpp_backend.unit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensource.energy.vpp_backend.config.ApplicationConfig;
import org.opensource.energy.vpp_backend.constant.SwaggerConstants;
import org.springframework.web.filter.CorsFilter;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ApplicationConfigTest {

    private ApplicationConfig config;

    @BeforeEach
    void setUp() throws Exception {
        config = new ApplicationConfig();

        Field allowedOriginField = ApplicationConfig.class.getDeclaredField("allowedOrigin");
        allowedOriginField.setAccessible(true);
        allowedOriginField.set(config, "http://localhost:3000");
    }

    @Test
    void given_application_context_when_cors_filter_is_loaded_then_instance_is_created() {
        CorsFilter filter = config.corsFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    void given_swagger_config_when_openapi_is_requested_then_openapi_contains_context_path() {
        OpenAPI openAPI = config.swaggerConfig("/api");
        assertThat(openAPI.getInfo().getTitle()).isEqualTo(SwaggerConstants.VPP_API_TITLE);
        assertThat(openAPI.getInfo().getVersion()).isEqualTo(SwaggerConstants.VPP_API_VERSION);
        assertThat(openAPI.getInfo().getDescription()).isEqualTo(SwaggerConstants.VPP_API_DESCRIPTION);
        assertThat(openAPI.getServers()).extracting(Server::getUrl).containsExactly("/api");
    }

    @Test
    void given_empty_context_path_when_openapi_is_requested_then_openapi_contains_root_path() {
        OpenAPI openAPI = config.swaggerConfig("");
        assertThat(openAPI.getServers()).extracting(Server::getUrl).containsExactly("/");
    }
}
