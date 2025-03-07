// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.models.OracleSink;

public final class OracleSinkTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        OracleSink model = BinaryData.fromString(
            "{\"type\":\"OracleSink\",\"preCopyScript\":\"dataynljigjcnkadmwti\",\"writeBatchSize\":\"dataknewpnpun\",\"writeBatchTimeout\":\"datajbnhbxvvufq\",\"sinkRetryCount\":\"datajuosajqnsrcqdth\",\"sinkRetryWait\":\"dataqamdlcu\",\"maxConcurrentConnections\":\"datamrvryakc\",\"disableMetricsCollection\":\"datasnprda\",\"\":{\"bwobovexsnmwwhbm\":\"datagabbxexacgmtpk\",\"nkmkcimksfejzm\":\"datajlsztpygqwkdlx\",\"nb\":\"datavlbzmngxzp\",\"kjfkaoe\":\"dataovhddvtnbtvl\"}}")
            .toObject(OracleSink.class);
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        OracleSink model = new OracleSink().withWriteBatchSize("dataknewpnpun").withWriteBatchTimeout("datajbnhbxvvufq")
            .withSinkRetryCount("datajuosajqnsrcqdth").withSinkRetryWait("dataqamdlcu")
            .withMaxConcurrentConnections("datamrvryakc").withDisableMetricsCollection("datasnprda")
            .withPreCopyScript("dataynljigjcnkadmwti");
        model = BinaryData.fromObject(model).toObject(OracleSink.class);
    }
}
