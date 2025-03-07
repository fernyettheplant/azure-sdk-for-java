// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.core.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.util.logging.ClientLogger;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a linear ring that is part of a {@link GeoPolygon}.
 */
@Immutable
public final class GeoLinearRing implements JsonSerializable<GeoLinearRing> {
    // GeoLinearRing is a commonly used model class, use a static logger.
    private static final ClientLogger LOGGER = new ClientLogger(GeoLinearRing.class);

    private final GeoArray<GeoPosition> coordinates;

    /**
     * Constructs a new linear ring with the passed coordinates.
     *
     * @param coordinates The coordinates of the linear ring.
     * @throws NullPointerException If {@code coordinates} is null.
     * @throws IllegalArgumentException If {@code coordinates} has less than 4 elements or the first and last elements
     * aren't equivalent.
     */
    public GeoLinearRing(List<GeoPosition> coordinates) {
        Objects.requireNonNull(coordinates, "'coordinates' cannot be null.");

        int size = coordinates.size();
        if (size < 4) {
            throw LOGGER
                .logExceptionAsError(new IllegalArgumentException("A linear ring requires at least 4 coordinates."));
        }

        if (!Objects.equals(coordinates.get(0), coordinates.get(size - 1))) {
            throw LOGGER.logExceptionAsError(
                new IllegalArgumentException("The first and last coordinates of a linear ring must be equivalent."));
        }

        this.coordinates = new GeoArray<>(new ArrayList<>(coordinates));
    }

    /**
     * Unmodifiable representation of the {@link GeoPosition geometric positions} representing this linear ring.
     *
     * @return An unmodifiable representation of the {@link GeoPosition geometric positions} representing this linear
     * ring.
     */
    public List<GeoPosition> getCoordinates() {
        return coordinates;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GeoLinearRing)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        GeoLinearRing other = (GeoLinearRing) obj;
        return Objects.equals(coordinates, other.coordinates);
    }

    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        return jsonWriter.writeArray(getCoordinates(), JsonWriter::writeJson);
    }

    /**
     * Reads a JSON stream into a {@link GeoLinearRing}.
     *
     * @param jsonReader The {@link JsonReader} being read.
     * @return The {@link GeoLinearRing} that the JSON stream represented, or null if it pointed to JSON null.
     * @throws IOException If a {@link GeoLinearRing} fails to be read from the {@code jsonReader}.
     */
    public static GeoLinearRing fromJson(JsonReader jsonReader) throws IOException {
        List<GeoPosition> coordinates = jsonReader.readArray(GeoPosition::fromJson);
        if (coordinates == null) {
            return null;
        }

        return new GeoLinearRing(coordinates);
    }
}
