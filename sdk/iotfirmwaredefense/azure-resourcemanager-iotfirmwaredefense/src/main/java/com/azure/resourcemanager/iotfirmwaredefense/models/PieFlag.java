// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.iotfirmwaredefense.models;

import com.azure.core.util.ExpandableStringEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;

/** PIE flag. */
public final class PieFlag extends ExpandableStringEnum<PieFlag> {
    /** Static value True for PieFlag. */
    public static final PieFlag TRUE = fromString("True");

    /** Static value False for PieFlag. */
    public static final PieFlag FALSE = fromString("False");

    /**
     * Creates a new instance of PieFlag value.
     *
     * @deprecated Use the {@link #fromString(String)} factory method.
     */
    @Deprecated
    public PieFlag() {
    }

    /**
     * Creates or finds a PieFlag from its string representation.
     *
     * @param name a name to look for.
     * @return the corresponding PieFlag.
     */
    @JsonCreator
    public static PieFlag fromString(String name) {
        return fromString(name, PieFlag.class);
    }

    /**
     * Gets known PieFlag values.
     *
     * @return known PieFlag values.
     */
    public static Collection<PieFlag> values() {
        return values(PieFlag.class);
    }
}
