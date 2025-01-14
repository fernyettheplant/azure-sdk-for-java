// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.fluent.models.HDInsightSparkActivityTypeProperties;
import com.azure.resourcemanager.datafactory.models.HDInsightActivityDebugInfoOption;
import com.azure.resourcemanager.datafactory.models.LinkedServiceReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class HDInsightSparkActivityTypePropertiesTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        HDInsightSparkActivityTypeProperties model = BinaryData.fromString(
            "{\"rootPath\":\"dataeimwhot\",\"entryFilePath\":\"datacgdpqkfxdqmdvrgl\",\"arguments\":[\"datamke\",\"dataeyqotmj\",\"dataqqahmzleta\"],\"getDebugInfo\":\"Always\",\"sparkJobLinkedService\":{\"referenceName\":\"wkkvarffjuefjbmo\",\"parameters\":{\"qwa\":\"dataodmdrdtywa\",\"jrpcpgcltf\":\"dataciapvcsw\",\"ptf\":\"dataaqmrimletjv\",\"blhzfglpswg\":\"dataab\"}},\"className\":\"xup\",\"proxyUser\":\"dataizkqnbiiand\",\"sparkConfig\":{\"ku\":\"datapqha\"}}")
            .toObject(HDInsightSparkActivityTypeProperties.class);
        Assertions.assertEquals(HDInsightActivityDebugInfoOption.ALWAYS, model.getDebugInfo());
        Assertions.assertEquals("wkkvarffjuefjbmo", model.sparkJobLinkedService().referenceName());
        Assertions.assertEquals("xup", model.className());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        HDInsightSparkActivityTypeProperties model = new HDInsightSparkActivityTypeProperties()
            .withRootPath("dataeimwhot").withEntryFilePath("datacgdpqkfxdqmdvrgl")
            .withArguments(Arrays.asList("datamke", "dataeyqotmj", "dataqqahmzleta"))
            .withGetDebugInfo(HDInsightActivityDebugInfoOption.ALWAYS)
            .withSparkJobLinkedService(new LinkedServiceReference().withReferenceName("wkkvarffjuefjbmo")
                .withParameters(mapOf("qwa", "dataodmdrdtywa", "jrpcpgcltf", "dataciapvcsw", "ptf", "dataaqmrimletjv",
                    "blhzfglpswg", "dataab")))
            .withClassName("xup").withProxyUser("dataizkqnbiiand").withSparkConfig(mapOf("ku", "datapqha"));
        model = BinaryData.fromObject(model).toObject(HDInsightSparkActivityTypeProperties.class);
        Assertions.assertEquals(HDInsightActivityDebugInfoOption.ALWAYS, model.getDebugInfo());
        Assertions.assertEquals("wkkvarffjuefjbmo", model.sparkJobLinkedService().referenceName());
        Assertions.assertEquals("xup", model.className());
    }

    // Use "Map.of" if available
    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> mapOf(Object... inputs) {
        Map<String, T> map = new HashMap<>();
        for (int i = 0; i < inputs.length; i += 2) {
            String key = (String) inputs[i];
            T value = (T) inputs[i + 1];
            map.put(key, value);
        }
        return map;
    }
}
