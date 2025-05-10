package org.opensource.energy.vpp_backend.config;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Profile("!test")
public class ConditionalAuditBean {
}
