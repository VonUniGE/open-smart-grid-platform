/*
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.application.services;

import org.opensmartgridplatform.adapter.domain.smartmetering.application.mapping.MonitoringMapper;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.JmsMessageSender;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.SmartMeter;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ActualMeterReadsQuery;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ActualPowerQualityRequest;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ActualPowerQualityResponse;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.AlarmRegister;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ClearAlarmRegisterRequest;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.GetPowerQualityProfileRequest;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.GetPowerQualityProfileResponse;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.MeterReads;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.MeterReadsGas;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodicMeterReadsContainer;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodicMeterReadsContainerGas;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodicMeterReadsQuery;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ReadAlarmRegisterRequest;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ActualMeterReadsQueryDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ActualPowerQualityRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ActualPowerQualityResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.AlarmRegisterResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ChannelDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ClearAlarmRegisterRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ConfigureDefinableLoadProfileRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.MeterReadsGasResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.MeterReadsResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodTypeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadGasResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ReadAlarmRegisterRequestDto;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalExceptionType;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "domainSmartMeteringMonitoringService")
@Transactional(value = "transactionManager")
public class MonitoringService {

  private static final String DEVICE_RESPONSE_NOT_OK_LOG_MSG =
      "Device Response not ok. Unexpected Exception";

  private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringService.class);

  @Autowired
  @Qualifier(value = "domainSmartMeteringOutboundOsgpCoreRequestsMessageSender")
  private JmsMessageSender osgpCoreRequestMessageSender;

  @Autowired private MonitoringMapper monitoringMapper;

  @Autowired private DomainHelperService domainHelperService;

  @Autowired
  @Qualifier(value = "domainSmartMeteringOutboundWebServiceResponsesMessageSender")
  private WebServiceResponseMessageSender webServiceResponseMessageSender;

  public MonitoringService() {
    // Parameterless constructor required for transactions...
  }

  public void requestPeriodicMeterReads(
      final MessageMetadata messageMetadata,
      final PeriodicMeterReadsQuery periodicMeterReadsValueQuery)
      throws FunctionalException {

    LOGGER.info(
        "requestPeriodicMeterReads for organisationIdentification: {} for deviceIdentification: {}",
        messageMetadata.getOrganisationIdentification(),
        messageMetadata.getDeviceIdentification());

    final SmartMeter smartMeter =
        this.domainHelperService.findSmartMeter(messageMetadata.getDeviceIdentification());

    if (periodicMeterReadsValueQuery.isMbusDevice()) {

      if (smartMeter.getChannel() == null) {
        /*
         * For now, throw a FunctionalException. As soon as we can
         * communicate with some types of gas meters directly, and not
         * through an M-Bus port of an energy meter, this will have to
         * be changed.
         */
        throw new FunctionalException(
            FunctionalExceptionType.VALIDATION_ERROR,
            ComponentType.DOMAIN_SMART_METERING,
            new AssertionError("Meter for gas reads should have a channel configured."));
      }
      final PeriodicMeterReadsRequestDto requestDto =
          new PeriodicMeterReadsRequestDto(
              PeriodTypeDto.valueOf(periodicMeterReadsValueQuery.getPeriodType().name()),
              periodicMeterReadsValueQuery.getBeginDate(),
              periodicMeterReadsValueQuery.getEndDate(),
              ChannelDto.fromNumber(smartMeter.getChannel()));
      final Device gatewayDevice = smartMeter.getGatewayDevice();
      if (gatewayDevice == null) {
        /*
         * For now throw a FunctionalException, based on the same
         * reasoning as with the channel a couple of lines up. As soon
         * as we have scenario's with direct communication with gas
         * meters this will have to be changed.
         */
        throw new FunctionalException(
            FunctionalExceptionType.VALIDATION_ERROR,
            ComponentType.DOMAIN_SMART_METERING,
            new AssertionError(
                "Meter for gas reads should have an energy meter as gateway device."));
      }
      this.osgpCoreRequestMessageSender.send(
          requestDto,
          messageMetadata
              .builder()
              .withDeviceIdentification(gatewayDevice.getDeviceIdentification())
              .withIpAddress(gatewayDevice.getIpAddress())
              .withNetworkSegmentIds(gatewayDevice.getBtsId(), gatewayDevice.getCellId())
              .build());
    } else {

      final PeriodicMeterReadsRequestDto requestDto =
          this.monitoringMapper.map(
              periodicMeterReadsValueQuery, PeriodicMeterReadsRequestDto.class);
      this.osgpCoreRequestMessageSender.send(
          requestDto,
          messageMetadata
              .builder()
              .withIpAddress(smartMeter.getIpAddress())
              .withNetworkSegmentIds(smartMeter.getBtsId(), smartMeter.getCellId())
              .build());
    }
  }

  public void handlePeriodicMeterReadsresponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final PeriodicMeterReadsResponseDto periodMeterReadsValueDTO) {

    LOGGER.info(
        "handlePeriodicMeterReadsresponse for MessageType: {}", messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(
                this.monitoringMapper.map(
                    periodMeterReadsValueDTO, PeriodicMeterReadsContainer.class))
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void handlePeriodicMeterReadsresponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final PeriodicMeterReadGasResponseDto periodMeterReadsValueDTO) {

    LOGGER.info(
        "handlePeriodicMeterReadsresponse for MessageType: {}", messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(
                this.monitoringMapper.map(
                    periodMeterReadsValueDTO, PeriodicMeterReadsContainerGas.class))
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void requestActualMeterReads(
      final MessageMetadata messageMetadata, final ActualMeterReadsQuery actualMeterReadsQuery)
      throws FunctionalException {

    LOGGER.info(
        "requestActualMeterReads for organisationIdentification: {} for deviceIdentification: {}",
        messageMetadata.getOrganisationIdentification(),
        messageMetadata.getDeviceIdentification());

    final SmartMeter smartMeter =
        this.domainHelperService.findSmartMeter(messageMetadata.getDeviceIdentification());

    if (actualMeterReadsQuery.isMbusDevice()) {

      if (smartMeter.getChannel() == null) {
        /*
         * For now, throw a FunctionalException. As soon as we can
         * communicate with some types of gas meters directly, and not
         * through an M-Bus port of an energy meter, this will have to
         * be changed.
         */
        throw new FunctionalException(
            FunctionalExceptionType.VALIDATION_ERROR,
            ComponentType.DOMAIN_SMART_METERING,
            new AssertionError("Meter for gas reads should have a channel configured."));
      }
      final Device gatewayDevice = smartMeter.getGatewayDevice();
      if (gatewayDevice == null) {
        /*
         * For now throw a FunctionalException, based on the same
         * reasoning as with the channel a couple of lines up. As soon
         * as we have scenario's with direct communication with gas
         * meters this will have to be changed.
         */
        throw new FunctionalException(
            FunctionalExceptionType.VALIDATION_ERROR,
            ComponentType.DOMAIN_SMART_METERING,
            new AssertionError(
                "Meter for gas reads should have an energy meter as gateway device."));
      }
      final ActualMeterReadsQueryDto requestDto =
          new ActualMeterReadsQueryDto(ChannelDto.fromNumber(smartMeter.getChannel()));
      this.osgpCoreRequestMessageSender.send(
          requestDto,
          messageMetadata
              .builder()
              .withDeviceIdentification(gatewayDevice.getDeviceIdentification())
              .withIpAddress(gatewayDevice.getIpAddress())
              .withNetworkSegmentIds(gatewayDevice.getBtsId(), gatewayDevice.getCellId())
              .build());
    } else {
      this.osgpCoreRequestMessageSender.send(
          new ActualMeterReadsQueryDto(),
          messageMetadata
              .builder()
              .withIpAddress(smartMeter.getIpAddress())
              .withNetworkSegmentIds(smartMeter.getBtsId(), smartMeter.getCellId())
              .build());
    }
  }

  public void handleActualMeterReadsResponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final MeterReadsResponseDto actualMeterReadsDto) {

    LOGGER.info(
        "handleActualMeterReadsResponse for MessageType: {}", messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(this.monitoringMapper.map(actualMeterReadsDto, MeterReads.class))
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void handleActualMeterReadsResponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final MeterReadsGasResponseDto actualMeterReadsGas) {

    LOGGER.info(
        "handleActualMeterReadsResponse for MessageType: {}", messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(this.monitoringMapper.map(actualMeterReadsGas, MeterReadsGas.class))
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void requestActualPowerQuality(
      final MessageMetadata messageMetadata, final ActualPowerQualityRequest request)
      throws FunctionalException {

    LOGGER.info(
        "requestActualPowerQuality for organisationIdentification: {} for deviceIdentification: {}",
        messageMetadata.getOrganisationIdentification(),
        messageMetadata.getDeviceIdentification());

    final SmartMeter smartMeter =
        this.domainHelperService.findSmartMeter(messageMetadata.getDeviceIdentification());

    final ActualPowerQualityRequestDto requestDto =
        this.monitoringMapper.map(request, ActualPowerQualityRequestDto.class);

    this.osgpCoreRequestMessageSender.send(
        requestDto,
        messageMetadata
            .builder()
            .withIpAddress(smartMeter.getIpAddress())
            .withNetworkSegmentIds(smartMeter.getBtsId(), smartMeter.getCellId())
            .build());
  }

  public void handleActualPowerQualityResponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final ActualPowerQualityResponseDto actualPowerQualityResponseDto) {

    LOGGER.info(
        "handleGetActualPowerQualityResponse for MessageType: {}",
        messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final ActualPowerQualityResponse actualPowerQualityResponse =
        this.monitoringMapper.map(actualPowerQualityResponseDto, ActualPowerQualityResponse.class);

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(actualPowerQualityResponse)
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void requestReadAlarmRegister(
      final MessageMetadata messageMetadata,
      final ReadAlarmRegisterRequest readAlarmRegisterRequestValueObject)
      throws FunctionalException {

    LOGGER.info(
        "requestReadAlarmRegister for organisationIdentification: {} for deviceIdentification: {}",
        messageMetadata.getOrganisationIdentification(),
        messageMetadata.getDeviceIdentification());

    final SmartMeter smartMeter =
        this.domainHelperService.findSmartMeter(messageMetadata.getDeviceIdentification());

    final ReadAlarmRegisterRequestDto requestDto =
        this.monitoringMapper.map(
            readAlarmRegisterRequestValueObject, ReadAlarmRegisterRequestDto.class);

    this.osgpCoreRequestMessageSender.send(
        requestDto,
        messageMetadata
            .builder()
            .withIpAddress(smartMeter.getIpAddress())
            .withNetworkSegmentIds(smartMeter.getBtsId(), smartMeter.getCellId())
            .build());
  }

  public void handleReadAlarmRegisterResponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final AlarmRegisterResponseDto alarmRegisterDto) {

    LOGGER.info(
        "handleReadAlarmRegisterResponse for MessageType: {}", messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final AlarmRegister alarmRegisterValueDomain =
        this.monitoringMapper.map(alarmRegisterDto, AlarmRegister.class);

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(alarmRegisterValueDomain)
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void requestPowerQualityProfile(
      final MessageMetadata messageMetadata, final GetPowerQualityProfileRequest request)
      throws FunctionalException {

    LOGGER.info(
        "requestPowerQualityProfile for organisationIdentification: {} for deviceIdentification: {}",
        messageMetadata.getOrganisationIdentification(),
        messageMetadata.getDeviceIdentification());

    final SmartMeter smartMeter =
        this.domainHelperService.findSmartMeter(messageMetadata.getDeviceIdentification());

    final ConfigureDefinableLoadProfileRequestDto requestDto =
        this.monitoringMapper.map(request, ConfigureDefinableLoadProfileRequestDto.class);

    this.osgpCoreRequestMessageSender.send(
        requestDto,
        messageMetadata
            .builder()
            .withIpAddress(smartMeter.getIpAddress())
            .withNetworkSegmentIds(smartMeter.getBtsId(), smartMeter.getCellId())
            .build());
  }

  public void handleGetPowerQualityProfileResponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception,
      final GetPowerQualityProfileResponseDto getPowerQualityProfileResponseDto) {

    LOGGER.info(
        "GetPowerQualityProfileResponse for MessageType: {}", messageMetadata.getMessageType());

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final GetPowerQualityProfileResponse getPowerQualityProfileResponse =
        this.monitoringMapper.map(
            getPowerQualityProfileResponseDto, GetPowerQualityProfileResponse.class);

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .withDataObject(getPowerQualityProfileResponse)
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }

  public void requestClearAlarmRegister(
      final MessageMetadata messageMetadata,
      final ClearAlarmRegisterRequest clearAlarmRegisterRequestValueObject)
      throws FunctionalException {

    LOGGER.info(
        "Request clear alarm register for organisationIdentification: {} for deviceIdentification: {}",
        messageMetadata.getOrganisationIdentification(),
        messageMetadata.getDeviceIdentification());

    final SmartMeter smartMeter =
        this.domainHelperService.findSmartMeter(messageMetadata.getDeviceIdentification());

    final ClearAlarmRegisterRequestDto requestDto =
        this.monitoringMapper.map(
            clearAlarmRegisterRequestValueObject, ClearAlarmRegisterRequestDto.class);

    this.osgpCoreRequestMessageSender.send(
        requestDto,
        messageMetadata
            .builder()
            .withIpAddress(smartMeter.getIpAddress())
            .withNetworkSegmentIds(smartMeter.getBtsId(), smartMeter.getCellId())
            .build());
  }

  public void handleClearAlarmRegisterResponse(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception) {

    LOGGER.info(
        "Handle clear alarm register response for MessageType: {}",
        messageMetadata.getMessageType());

    this.handleMetadataOnlyResponseMessage(messageMetadata, deviceResult, exception);
  }

  private void handleMetadataOnlyResponseMessage(
      final MessageMetadata messageMetadata,
      final ResponseMessageResultType deviceResult,
      final OsgpException exception) {

    ResponseMessageResultType result = deviceResult;
    if (exception != null) {
      LOGGER.error(DEVICE_RESPONSE_NOT_OK_LOG_MSG, exception);
      result = ResponseMessageResultType.NOT_OK;
    }

    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withMessageMetadata(messageMetadata)
            .withResult(result)
            .withOsgpException(exception)
            .build();
    this.webServiceResponseMessageSender.send(responseMessage, messageMetadata.getMessageType());
  }
}
