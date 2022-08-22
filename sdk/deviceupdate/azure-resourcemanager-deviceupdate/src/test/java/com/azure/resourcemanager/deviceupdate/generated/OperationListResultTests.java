// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.deviceupdate.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.deviceupdate.models.OperationListResult;
import org.junit.jupiter.api.Test;

public final class OperationListResultTests {
    @Test
    public void testDeserialize() {
        OperationListResult model =
            BinaryData
                .fromString(
                    "{\"value\":[{\"name\":\"mefqsgzvahapjyzh\",\"isDataAction\":false,\"display\":{\"provider\":\"cjrvxdjzlmwlxkv\",\"resource\":\"fhzovawjvzunluth\",\"operation\":\"prnxipeil\",\"description\":\"zuaejxd\"},\"origin\":\"user\",\"actionType\":\"Internal\"}],\"nextLink\":\"bbtdzumvee\"}")
                .toObject(OperationListResult.class);
    }

    @Test
    public void testSerialize() {
        OperationListResult model = new OperationListResult();
        model = BinaryData.fromObject(model).toObject(OperationListResult.class);
    }
}