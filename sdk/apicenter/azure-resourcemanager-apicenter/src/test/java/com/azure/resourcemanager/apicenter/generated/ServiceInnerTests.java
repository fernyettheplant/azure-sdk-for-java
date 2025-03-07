// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.apicenter.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.apicenter.fluent.models.ServiceInner;
import com.azure.resourcemanager.apicenter.models.ManagedServiceIdentity;
import com.azure.resourcemanager.apicenter.models.ManagedServiceIdentityType;
import com.azure.resourcemanager.apicenter.models.UserAssignedIdentity;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class ServiceInnerTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        ServiceInner model =
            BinaryData
                .fromString(
                    "{\"properties\":{\"provisioningState\":\"Failed\"},\"identity\":{\"principalId\":\"d5d0df07-26af-4baa-92d9-47bdb47df4cd\",\"tenantId\":\"61afb0e5-40b8-41b6-b5f2-f835b8c17c59\",\"type\":\"UserAssigned\",\"userAssignedIdentities\":{\"ryffdfdosy\":{\"principalId\":\"356f73d2-54c6-46f0-a527-369175dd5245\",\"clientId\":\"aa470577-53b5-49ee-8603-8a643614b9c7\"},\"paojakhmsbzjh\":{\"principalId\":\"c10a0704-c7b2-414b-af28-c00ed76ecf8e\",\"clientId\":\"6a27b9b8-d17c-40cb-934f-e9f2df2efccf\"},\"evdphlxaol\":{\"principalId\":\"0a2e3c70-446a-4bbd-95bf-7be6cab57a01\",\"clientId\":\"00efb53b-6b97-46dc-a25e-19af51ee25d9\"},\"trg\":{\"principalId\":\"b7da0e44-2f8c-47a5-8c82-8f8b6a90433a\",\"clientId\":\"0921520b-f5ba-4d8c-af40-e6fd9eb5e887\"}}},\"location\":\"bpf\",\"tags\":{\"wzo\":\"inzgvfcj\"},\"id\":\"xjtfelluwfzit\",\"name\":\"np\",\"type\":\"qfpjk\"}")
                .toObject(ServiceInner.class);
        Assertions.assertEquals("bpf", model.location());
        Assertions.assertEquals("inzgvfcj", model.tags().get("wzo"));
        Assertions.assertEquals(ManagedServiceIdentityType.USER_ASSIGNED, model.identity().type());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        ServiceInner model =
            new ServiceInner()
                .withLocation("bpf")
                .withTags(mapOf("wzo", "inzgvfcj"))
                .withIdentity(
                    new ManagedServiceIdentity()
                        .withType(ManagedServiceIdentityType.USER_ASSIGNED)
                        .withUserAssignedIdentities(
                            mapOf(
                                "ryffdfdosy",
                                new UserAssignedIdentity(),
                                "paojakhmsbzjh",
                                new UserAssignedIdentity(),
                                "evdphlxaol",
                                new UserAssignedIdentity(),
                                "trg",
                                new UserAssignedIdentity())));
        model = BinaryData.fromObject(model).toObject(ServiceInner.class);
        Assertions.assertEquals("bpf", model.location());
        Assertions.assertEquals("inzgvfcj", model.tags().get("wzo"));
        Assertions.assertEquals(ManagedServiceIdentityType.USER_ASSIGNED, model.identity().type());
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
