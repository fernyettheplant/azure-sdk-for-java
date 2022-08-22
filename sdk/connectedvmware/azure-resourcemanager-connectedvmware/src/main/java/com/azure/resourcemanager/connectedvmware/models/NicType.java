// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.connectedvmware.models;

import com.azure.core.util.ExpandableStringEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;

/** Defines values for NicType. */
public final class NicType extends ExpandableStringEnum<NicType> {
    /** Static value vmxnet3 for NicType. */
    public static final NicType VMXNET3 = fromString("vmxnet3");

    /** Static value vmxnet2 for NicType. */
    public static final NicType VMXNET2 = fromString("vmxnet2");

    /** Static value vmxnet for NicType. */
    public static final NicType VMXNET = fromString("vmxnet");

    /** Static value e1000 for NicType. */
    public static final NicType E1000 = fromString("e1000");

    /** Static value e1000e for NicType. */
    public static final NicType E1000E = fromString("e1000e");

    /** Static value pcnet32 for NicType. */
    public static final NicType PCNET32 = fromString("pcnet32");

    /**
     * Creates or finds a NicType from its string representation.
     *
     * @param name a name to look for.
     * @return the corresponding NicType.
     */
    @JsonCreator
    public static NicType fromString(String name) {
        return fromString(name, NicType.class);
    }

    /**
     * Gets known NicType values.
     *
     * @return known NicType values.
     */
    public static Collection<NicType> values() {
        return values(NicType.class);
    }
}