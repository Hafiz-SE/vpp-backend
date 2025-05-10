package org.opensource.energy.vpp_backend.config;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing only in non-test environments.
 *
 * <p>This bean activates {@link org.springframework.data.jpa.repository.config.EnableJpaAuditing}
 * when the active Spring profile is not set to "test", ensuring that auditing features
 * like @CreatedAt and @ModifiedAt work during normal application runtime but
 * do not interfere with unit or integration tests.</p>
 */
@EnableJpaAuditing
@Profile("!test")
public class ConditionalAuditBean {
}
