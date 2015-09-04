/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;

import com.alliander.osgp.adapter.ws.endpointinterceptors.AnnotationMethodArgumentResolver;
import com.alliander.osgp.adapter.ws.endpointinterceptors.CertificateAndSoapHeaderAuthorizationEndpointInterceptor;
import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.adapter.ws.endpointinterceptors.SoapHeaderEndpointInterceptor;
import com.alliander.osgp.adapter.ws.endpointinterceptors.WebServiceMonitorInterceptor;
import com.alliander.osgp.adapter.ws.endpointinterceptors.X509CertificateRdnAttributeValueEndpointInterceptor;
import com.alliander.osgp.adapter.ws.smartmetering.application.exceptionhandling.DetailSoapFaultMappingExceptionResolver;
import com.alliander.osgp.adapter.ws.smartmetering.application.exceptionhandling.SoapFaultMapper;
import com.alliander.osgp.adapter.ws.smartmetering.application.mapping.NotificationMapper;
import com.alliander.osgp.adapter.ws.smartmetering.infra.ws.SendNotificationServiceClient;
import com.alliander.osgp.adapter.ws.smartmetering.infra.ws.WebServiceTemplateFactory;

@Configuration
@PropertySource("file:${osp/osgpAdapterWsSmartMetering/config}")
public class WebServiceConfig {

    private static final String PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_SMART_METERING_MANAGEMENT = "jaxb2.marshaller.context.path.smartmetering.management";
    private static final String PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_SMART_METERING_INSTALLATION = "jaxb2.marshaller.context.path.smartmetering.installation";

    private static final String ORGANISATION_IDENTIFICATION_HEADER = "OrganisationIdentification";
    private static final String ORGANISATION_IDENTIFICATION_CONTEXT = ORGANISATION_IDENTIFICATION_HEADER;

    private static final String USER_NAME_HEADER = "UserName";

    private static final String APPLICATION_NAME_HEADER = "ApplicationName";

    private static final String X509_RDN_ATTRIBUTE_ID = "cn";
    private static final String X509_RDN_ATTRIBUTE_VALUE_CONTEXT_PROPERTY_NAME = "CommonNameSet";

    private static final String PROPERTY_NAME_APPLICATION_NAME = "application.name";

    private static final String PROPERTY_NAME_WEBSERVICETEMPLATE_BASE_URI = "base.uri";
    // TODO save in database
    private static final String PROPERTY_NAME_WEBSERVICETEMPLATE_DEFAULT_URI_SMARTMETERING_NOTIFICATION = "web.service.template.default.uri.smartmetering.notification";

    private static final String PROPERTY_NAME_WEBSERVICE_TRUSTSTORE_LOCATION = "web.service.truststore.location";
    private static final String PROPERTY_NAME_WEBSERVICE_TRUSTSTORE_PASSWORD = "web.service.truststore.password";
    private static final String PROPERTY_NAME_WEBSERVICE_TRUSTSTORE_TYPE = "web.service.truststore.type";
    private static final String PROPERTY_NAME_WEBSERVICE_KEYSTORE_LOCATION = "web.service.keystore.location";
    private static final String PROPERTY_NAME_WEBSERVICE_KEYSTORE_PASSWORD = "web.service.keystore.password";
    private static final String PROPERTY_NAME_WEBSERVICE_KEYSTORE_TYPE = "web.service.keystore.type";

    private static final String PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_SMARTMETERING_NOTIFICATION = "jaxb2.marshaller.context.path.smartmetering.notification";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceConfig.class);

    @Resource
    private Environment environment;

    // WS Notification communication

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        final SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();

        return messageFactory;
    }

    @Bean
    public KeyStoreFactoryBean webServiceTrustStoreFactory() {
        final KeyStoreFactoryBean factory = new KeyStoreFactoryBean();
        factory.setType(this.environment.getProperty(PROPERTY_NAME_WEBSERVICE_TRUSTSTORE_TYPE));
        factory.setLocation(new FileSystemResource(this.environment
                .getProperty(PROPERTY_NAME_WEBSERVICE_TRUSTSTORE_LOCATION)));
        factory.setPassword(this.environment.getProperty(PROPERTY_NAME_WEBSERVICE_TRUSTSTORE_PASSWORD));

        return factory;
    }

    @Bean
    public Jaxb2Marshaller notificationSenderMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(this.environment
                .getRequiredProperty(PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_SMARTMETERING_NOTIFICATION));
        return marshaller;
    }

    @Bean
    public SendNotificationServiceClient sendNotificationServiceClient() throws java.security.GeneralSecurityException {
        return new SendNotificationServiceClient(this.createWebServiceTemplateFactory(
                PROPERTY_NAME_WEBSERVICETEMPLATE_BASE_URI,
                PROPERTY_NAME_WEBSERVICETEMPLATE_DEFAULT_URI_SMARTMETERING_NOTIFICATION,
                this.notificationSenderMarshaller()), this.notificationMapper());
    }

    private WebServiceTemplateFactory createWebServiceTemplateFactory(final String baseUriKey, final String uriKey,
            final Jaxb2Marshaller marshaller) {
        return new WebServiceTemplateFactory(marshaller, this.messageFactory(), this.environment
                .getProperty(baseUriKey).concat(this.environment.getProperty(uriKey)),
                this.environment.getProperty(PROPERTY_NAME_WEBSERVICE_KEYSTORE_TYPE),
                this.environment.getProperty(PROPERTY_NAME_WEBSERVICE_KEYSTORE_LOCATION),
                this.environment.getProperty(PROPERTY_NAME_WEBSERVICE_KEYSTORE_PASSWORD),
                this.webServiceTrustStoreFactory(),
                this.environment.getRequiredProperty(PROPERTY_NAME_APPLICATION_NAME));
    }

    @Bean
    public NotificationMapper notificationMapper() {
        return new NotificationMapper();
    }

    // Client WS code

    /**
     * Method for creating the Marshaller for smart metering management.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller smartMeteringManagementMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.environment
                .getRequiredProperty(PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_SMART_METERING_MANAGEMENT));

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for Smart
     * Metering management.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor smartMeteringManagementMarshallingPayloadMethodProcessor() {
        final MarshallingPayloadMethodProcessor marshallingPayloadMethodProcessor = new MarshallingPayloadMethodProcessor(
                this.smartMeteringManagementMarshaller(), this.smartMeteringManagementMarshaller());

        return marshallingPayloadMethodProcessor;
    }

    /**
     * Method for creating the Marshaller for smart metering installation.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller smartMeteringInstallationMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.environment
                .getRequiredProperty(PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_SMART_METERING_INSTALLATION));

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for Smart
     * Metering installation.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor smartMeteringInstallationMarshallingPayloadMethodProcessor() {
        final MarshallingPayloadMethodProcessor marshallingPayloadMethodProcessor = new MarshallingPayloadMethodProcessor(
                this.smartMeteringInstallationMarshaller(), this.smartMeteringInstallationMarshaller());

        return marshallingPayloadMethodProcessor;
    }

    /**
     * Method for creating the Default Method Endpoint Adapter.
     *
     * @return DefaultMethodEndpointAdapter
     */
    @Bean
    public DefaultMethodEndpointAdapter defaultMethodEndpointAdapter() {
        final DefaultMethodEndpointAdapter defaultMethodEndpointAdapter = new DefaultMethodEndpointAdapter();

        final List<MethodArgumentResolver> methodArgumentResolvers = new ArrayList<MethodArgumentResolver>();

        // SMART METERING
        methodArgumentResolvers.add(this.smartMeteringManagementMarshallingPayloadMethodProcessor());
        methodArgumentResolvers.add(this.smartMeteringInstallationMarshallingPayloadMethodProcessor());

        methodArgumentResolvers.add(new AnnotationMethodArgumentResolver(ORGANISATION_IDENTIFICATION_CONTEXT,
                OrganisationIdentification.class));
        defaultMethodEndpointAdapter.setMethodArgumentResolvers(methodArgumentResolvers);

        final List<MethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<MethodReturnValueHandler>();

        // SMART METERING
        methodReturnValueHandlers.add(this.smartMeteringManagementMarshallingPayloadMethodProcessor());
        methodReturnValueHandlers.add(this.smartMeteringInstallationMarshallingPayloadMethodProcessor());

        defaultMethodEndpointAdapter.setMethodReturnValueHandlers(methodReturnValueHandlers);

        return defaultMethodEndpointAdapter;
    }

    @Bean
    public DetailSoapFaultMappingExceptionResolver exceptionResolver() {

        LOGGER.debug("Creating Detail Soap Fault Mapping Exception Resolver Bean");

        final DetailSoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultMappingExceptionResolver(
                new SoapFaultMapper());
        exceptionResolver.setOrder(1);

        final Properties props = new Properties();
        props.put("com.alliander.osgp.shared.exceptionhandling.FunctionalException", "SERVER");
        exceptionResolver.setExceptionMappings(props);
        return exceptionResolver;
    }

    @Bean
    public X509CertificateRdnAttributeValueEndpointInterceptor x509CertificateSubjectCnEndpointInterceptor() {
        return new X509CertificateRdnAttributeValueEndpointInterceptor(X509_RDN_ATTRIBUTE_ID,
                X509_RDN_ATTRIBUTE_VALUE_CONTEXT_PROPERTY_NAME);
    }

    /**
     * @return
     */
    @Bean
    public SoapHeaderEndpointInterceptor organisationIdentificationInterceptor() {
        return new SoapHeaderEndpointInterceptor(ORGANISATION_IDENTIFICATION_HEADER,
                ORGANISATION_IDENTIFICATION_CONTEXT);
    }

    /**
     * @return
     */
    @Bean
    public CertificateAndSoapHeaderAuthorizationEndpointInterceptor organisationIdentificationInCertificateCnEndpointInterceptor() {
        return new CertificateAndSoapHeaderAuthorizationEndpointInterceptor(
                X509_RDN_ATTRIBUTE_VALUE_CONTEXT_PROPERTY_NAME, ORGANISATION_IDENTIFICATION_CONTEXT);
    }

    @Bean
    public WebServiceMonitorInterceptor webServiceMonitorInterceptor() {
        return new WebServiceMonitorInterceptor(ORGANISATION_IDENTIFICATION_HEADER, USER_NAME_HEADER,
                APPLICATION_NAME_HEADER);
    }

}
