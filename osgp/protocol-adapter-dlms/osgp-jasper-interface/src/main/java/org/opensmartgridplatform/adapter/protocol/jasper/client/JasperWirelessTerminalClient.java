package org.opensmartgridplatform.adapter.protocol.jasper.client;

import org.opensmartgridplatform.adapter.protocol.jasper.exceptions.OsgpJasperException;
import org.opensmartgridplatform.adapter.protocol.jasper.response.GetSessionInfoResponse;

public interface JasperWirelessTerminalClient {

  public GetSessionInfoResponse getSession(final String iccid) throws OsgpJasperException;
}
