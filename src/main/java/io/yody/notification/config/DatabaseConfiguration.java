package io.yody.notification.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech.jhipster.config.JHipsterConstants;

@Configuration
@EntityScan({ "org.nentangso.core.domain", "io.yody.notification.domain" })
@EnableJpaRepositories({ "org.nentangso.core.repository", "io.yody.notification.repository" })
@EnableJpaAuditing(auditorAwareRef = "ntsSpringSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {}
