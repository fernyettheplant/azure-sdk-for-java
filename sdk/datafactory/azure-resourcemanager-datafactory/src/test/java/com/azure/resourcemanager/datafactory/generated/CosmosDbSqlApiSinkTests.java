// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.models.CosmosDbSqlApiSink;

public final class CosmosDbSqlApiSinkTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        CosmosDbSqlApiSink model = BinaryData.fromString(
            "{\"type\":\"CosmosDbSqlApiSink\",\"writeBehavior\":\"datall\",\"writeBatchSize\":\"datavhthxcrwee\",\"writeBatchTimeout\":\"datadmpfmcrcelsnj\",\"sinkRetryCount\":\"datanfdcjtveibntwi\",\"sinkRetryWait\":\"datagwxysut\",\"maxConcurrentConnections\":\"datafdhrifekstrms\",\"disableMetricsCollection\":\"datadgrzkeuplorn\",\"\":{\"zcdlnvupi\":\"datasmaa\",\"elv\":\"datacbzyhtbjyycac\"}}")
            .toObject(CosmosDbSqlApiSink.class);
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        CosmosDbSqlApiSink model = new CosmosDbSqlApiSink().withWriteBatchSize("datavhthxcrwee")
            .withWriteBatchTimeout("datadmpfmcrcelsnj").withSinkRetryCount("datanfdcjtveibntwi")
            .withSinkRetryWait("datagwxysut").withMaxConcurrentConnections("datafdhrifekstrms")
            .withDisableMetricsCollection("datadgrzkeuplorn").withWriteBehavior("datall");
        model = BinaryData.fromObject(model).toObject(CosmosDbSqlApiSink.class);
    }
}
