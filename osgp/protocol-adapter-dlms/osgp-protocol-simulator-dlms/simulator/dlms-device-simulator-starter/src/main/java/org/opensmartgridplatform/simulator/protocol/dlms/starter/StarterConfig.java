/*
 * Copyright 2018 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.simulator.protocol.dlms.starter;

import java.io.IOException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan()
public class StarterConfig {
  public Starter starter(final ApplicationArguments applicationArguments) throws IOException {
    return new Starter(applicationArguments);
  }
}
