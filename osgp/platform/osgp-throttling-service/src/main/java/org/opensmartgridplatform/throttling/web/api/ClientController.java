/*
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.throttling.web.api;

import org.opensmartgridplatform.throttling.repositories.ClientRepository;
import org.opensmartgridplatform.throttling.repositories.PermitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/clients", produces = "application/json")
public class ClientController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

  private final ClientRepository clientRepository;
  private final PermitRepository permitRepository;

  public ClientController(
      final ClientRepository clientRegistrationRepository,
      final PermitRepository permitRepository) {
    this.clientRepository = clientRegistrationRepository;
    this.permitRepository = permitRepository;
  }

  /**
   * Register a client to participate in requesting and releasing permits for throttled network
   * access.
   *
   * <p>Upon registration an ID is returned that should be used to identify the client in further
   * requests.
   *
   * <p>When a client is done requesting and releasing permits for network access, {@link
   * #unregisterClient(int) unregister} the client to let the throttling service know this client
   * should no longer hold, nor request any more permits.
   *
   * @return the {@code clientId} to be provided when requesting or releasing permits for the {@code
   *     client}
   */
  @PostMapping
  public ResponseEntity<Integer> registerClient() {
    final Integer nextClientId = this.clientRepository.getNextClientId();
    LOGGER.debug("Registering client with clientId {}", nextClientId);
    return ResponseEntity.ok(nextClientId);
  }

  /**
   * Unregister a client identified by the ID returned when it was {@link #registerClient()
   * registered}.
   *
   * <p>When calling this method a client claims it no longer (intentionally) holds, nor will it
   * request any more permits for throttled network access.
   *
   * @param clientId the {@code clientId} this client received upon registration
   * @return HTTP status {@code 202 ACCEPTED}
   */
  @Transactional
  @DeleteMapping(path = "/{clientId}")
  public ResponseEntity<Void> unregisterClient(@PathVariable final int clientId) {
    LOGGER.debug("Unregistering client with clientId {}", clientId);
    final long numberOfPermits = this.permitRepository.countByClientId(clientId);
    if (numberOfPermits > 0) {
      LOGGER.warn("Client {} unregistered with {} remaining permits.", clientId, numberOfPermits);
    }
    return ResponseEntity.accepted().build();
  }
}
