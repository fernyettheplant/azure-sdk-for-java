// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.netapp.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.netapp.fluent.models.SubvolumeModelInner;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;

public final class SubvolumeModelInnerTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        SubvolumeModelInner model = BinaryData.fromString(
            "{\"id\":\"byowbblgyavutp\",\"name\":\"joxoism\",\"type\":\"sbpimlq\",\"properties\":{\"path\":\"xkcgxxlxsffgcvi\",\"parentPath\":\"zdwlvwlyoupfgfb\",\"size\":7653162173647164897,\"bytesUsed\":4324822470137605092,\"permissions\":\"kfm\",\"creationTimeStamp\":\"2021-07-25T09:50:13Z\",\"accessedTimeStamp\":\"2021-10-13T02:52:40Z\",\"modifiedTimeStamp\":\"2021-07-19T15:33:36Z\",\"changedTimeStamp\":\"2021-01-19T00:17:43Z\",\"provisioningState\":\"sttktlahbqa\"}}")
            .toObject(SubvolumeModelInner.class);
        Assertions.assertEquals("xkcgxxlxsffgcvi", model.path());
        Assertions.assertEquals("zdwlvwlyoupfgfb", model.parentPath());
        Assertions.assertEquals(7653162173647164897L, model.size());
        Assertions.assertEquals(4324822470137605092L, model.bytesUsed());
        Assertions.assertEquals("kfm", model.permissions());
        Assertions.assertEquals(OffsetDateTime.parse("2021-07-25T09:50:13Z"), model.creationTimestamp());
        Assertions.assertEquals(OffsetDateTime.parse("2021-10-13T02:52:40Z"), model.accessedTimestamp());
        Assertions.assertEquals(OffsetDateTime.parse("2021-07-19T15:33:36Z"), model.modifiedTimestamp());
        Assertions.assertEquals(OffsetDateTime.parse("2021-01-19T00:17:43Z"), model.changedTimestamp());
        Assertions.assertEquals("sttktlahbqa", model.provisioningState());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        SubvolumeModelInner model = new SubvolumeModelInner().withPath("xkcgxxlxsffgcvi")
            .withParentPath("zdwlvwlyoupfgfb").withSize(7653162173647164897L).withBytesUsed(4324822470137605092L)
            .withPermissions("kfm").withCreationTimestamp(OffsetDateTime.parse("2021-07-25T09:50:13Z"))
            .withAccessedTimestamp(OffsetDateTime.parse("2021-10-13T02:52:40Z"))
            .withModifiedTimestamp(OffsetDateTime.parse("2021-07-19T15:33:36Z"))
            .withChangedTimestamp(OffsetDateTime.parse("2021-01-19T00:17:43Z")).withProvisioningState("sttktlahbqa");
        model = BinaryData.fromObject(model).toObject(SubvolumeModelInner.class);
        Assertions.assertEquals("xkcgxxlxsffgcvi", model.path());
        Assertions.assertEquals("zdwlvwlyoupfgfb", model.parentPath());
        Assertions.assertEquals(7653162173647164897L, model.size());
        Assertions.assertEquals(4324822470137605092L, model.bytesUsed());
        Assertions.assertEquals("kfm", model.permissions());
        Assertions.assertEquals(OffsetDateTime.parse("2021-07-25T09:50:13Z"), model.creationTimestamp());
        Assertions.assertEquals(OffsetDateTime.parse("2021-10-13T02:52:40Z"), model.accessedTimestamp());
        Assertions.assertEquals(OffsetDateTime.parse("2021-07-19T15:33:36Z"), model.modifiedTimestamp());
        Assertions.assertEquals(OffsetDateTime.parse("2021-01-19T00:17:43Z"), model.changedTimestamp());
        Assertions.assertEquals("sttktlahbqa", model.provisioningState());
    }
}
