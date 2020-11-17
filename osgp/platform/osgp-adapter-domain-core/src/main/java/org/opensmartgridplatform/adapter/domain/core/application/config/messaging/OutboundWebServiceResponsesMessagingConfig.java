/**
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.core.application.config.messaging;

import javax.jms.ConnectionFactory;
import javax.net.ssl.SSLException;

import org.opensmartgridplatform.shared.application.config.messaging.DefaultJmsConfiguration;
import org.opensmartgridplatform.shared.application.config.messaging.JmsConfigurationFactory;
import org.opensmartgridplatform.shared.application.config.messaging.JmsConfigurationNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configuration class for outbound web service responses
 */
@Configuration
public class OutboundWebServiceResponsesMessagingConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundWebServiceResponsesMessagingConfig.class);

    private JmsConfigurationFactory jmsConfigurationFactory;

    public OutboundWebServiceResponsesMessagingConfig(final Environment environment,
            final DefaultJmsConfiguration defaultJmsConfiguration) throws SSLException {
        this.jmsConfigurationFactory = new JmsConfigurationFactory(environment, defaultJmsConfiguration,
                JmsConfigurationNames.JMS_COMMON_WS_RESPONSES);
    }

    @Bean(destroyMethod = "stop", name = "domainCoreOutboundWebServiceResponsesConnectionFactory")
    public ConnectionFactory connectionFactory() {
        LOGGER.info("Initializing domainCoreOutboundWebServiceResponseConnectionFactory bean.");
        return this.jmsConfigurationFactory.getPooledConnectionFactory();
    }

    @Bean(name = "domainCoreOutboundWebServiceResponsesJmsTemplate")
    public JmsTemplate jmsTemplate() {
        return this.jmsConfigurationFactory.initJmsTemplate();
    }
}
