// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.core.implementation;

import com.azure.core.util.logging.ClientLogger;
import com.azure.core.util.logging.LogLevel;
import com.azure.json.JsonProviders;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonWriter;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Utility class that handles creating and using {@code JsonSerializable} and {@code XmlSerializable} reflectively while
 * they are in beta.
 * <p>
 * Once {@code azure-json} and {@code azure-xml} GA this can be replaced with direct usage of the types. This is
 * separated out from what uses it to keep those code paths clean.
 */
public final class ReflectionSerializable {
    private static final ClientLogger LOGGER = new ClientLogger(ReflectionSerializable.class);
    private static final Map<Class<?>, ReflectiveInvoker> FROM_JSON_CACHE;

    private static final Class<?> XML_SERIALIZABLE;
    private static final Class<?> XML_READER;

    private static final ReflectiveInvoker XML_READER_CREATOR;
    private static final ReflectiveInvoker XML_WRITER_CREATOR;
    private static final ReflectiveInvoker XML_WRITER_WRITE_XML_START_DOCUMENT;
    private static final ReflectiveInvoker XML_WRITER_WRITE_XML_SERIALIZABLE;
    private static final ReflectiveInvoker XML_WRITER_FLUSH;
    static final boolean XML_SERIALIZABLE_SUPPORTED;
    private static final Map<Class<?>, ReflectiveInvoker> FROM_XML_CACHE;

    static {
        FROM_JSON_CACHE = new ConcurrentHashMap<>();

        Class<?> xmlSerializable = null;
        Class<?> xmlReader = null;
        ReflectiveInvoker xmlReaderCreator = null;
        ReflectiveInvoker xmlWriterCreator = null;
        ReflectiveInvoker xmlWriterWriteStartDocument = null;
        ReflectiveInvoker xmlWriterWriteXmlSerializable = null;
        ReflectiveInvoker xmlWriterFlush = null;
        boolean xmlSerializableSupported = false;
        try {
            xmlSerializable = Class.forName("com.azure.xml.XmlSerializable");
            xmlReader = Class.forName("com.azure.xml.XmlReader");

            Class<?> xmlProviders = Class.forName("com.azure.xml.XmlProviders");

            xmlReaderCreator = ReflectionUtils.getMethodInvoker(xmlProviders,
                xmlProviders.getDeclaredMethod("createReader", byte[].class));

            xmlWriterCreator = ReflectionUtils.getMethodInvoker(xmlProviders,
                xmlProviders.getDeclaredMethod("createWriter", OutputStream.class));

            Class<?> xmlWriter = Class.forName("com.azure.xml.XmlWriter");

            xmlWriterWriteStartDocument
                = ReflectionUtils.getMethodInvoker(xmlWriter, xmlWriter.getDeclaredMethod("writeStartDocument"));

            xmlWriterWriteXmlSerializable
                = ReflectionUtils.getMethodInvoker(xmlWriter, xmlWriter.getDeclaredMethod("writeXml", xmlSerializable));

            xmlWriterFlush = ReflectionUtils.getMethodInvoker(xmlWriter, xmlWriter.getDeclaredMethod("flush"));

            xmlSerializableSupported = true;
        } catch (Throwable e) {
            if (e instanceof LinkageError || e instanceof Exception) {
                LOGGER.log(LogLevel.VERBOSE, () -> "XmlSerializable serialization and deserialization isn't supported. "
                    + "If it is required add a dependency of 'com.azure:azure-xml', or another dependencies which "
                    + "include 'com.azure:azure-xml' as a transitive dependency. If your application runs as expected "
                    + "this informational message can be ignored.", e);
            } else {
                throw (Error) e;
            }
        }

        XML_SERIALIZABLE = xmlSerializable;
        XML_READER = xmlReader;
        XML_READER_CREATOR = xmlReaderCreator;
        XML_WRITER_CREATOR = xmlWriterCreator;
        XML_WRITER_WRITE_XML_START_DOCUMENT = xmlWriterWriteStartDocument;
        XML_WRITER_WRITE_XML_SERIALIZABLE = xmlWriterWriteXmlSerializable;
        XML_WRITER_FLUSH = xmlWriterFlush;
        XML_SERIALIZABLE_SUPPORTED = xmlSerializableSupported;
        FROM_XML_CACHE = XML_SERIALIZABLE_SUPPORTED ? new ConcurrentHashMap<>() : null;
    }

    /**
     * Whether {@code JsonSerializable} is supported and the {@code bodyContentClass} is an instance of it.
     *
     * @param bodyContentClass The body content class.
     * @return Whether {@code bodyContentClass} can be used as {@code JsonSerializable}.
     */
    public static boolean supportsJsonSerializable(Class<?> bodyContentClass) {
        return JsonSerializable.class.isAssignableFrom(bodyContentClass);
    }

    /**
     * Serializes the {@code jsonSerializable} as an instance of {@code JsonSerializable}.
     *
     * @param jsonSerializable The {@code JsonSerializable} body content.
     * @return The {@link ByteBuffer} representing the serialized {@code jsonSerializable}.
     * @throws IOException If an error occurs during serialization.
     */
    public static ByteBuffer serializeJsonSerializableToByteBuffer(JsonSerializable<?> jsonSerializable)
        throws IOException {
        return serializeJsonSerializableWithReturn(jsonSerializable, AccessibleByteArrayOutputStream::toByteBuffer);
    }

    /**
     * Serializes the {@code jsonSerializable} as an instance of {@code JsonSerializable}.
     *
     * @param jsonSerializable The {@code JsonSerializable} content.
     * @return The {@code byte[]} representing the serialized {@code jsonSerializable}.
     * @throws IOException If an error occurs during serialization.
     */
    public static byte[] serializeJsonSerializableToBytes(JsonSerializable<?> jsonSerializable) throws IOException {
        return serializeJsonSerializableWithReturn(jsonSerializable, AccessibleByteArrayOutputStream::toByteArray);
    }

    /**
     * Serializes the {@code jsonSerializable} as an instance of {@code JsonSerializable}.
     *
     * @param jsonSerializable The {@code JsonSerializable} content.
     * @return The {@link String} representing the serialized {@code jsonSerializable}.
     * @throws IOException If an error occurs during serialization.
     */
    public static String serializeJsonSerializableToString(JsonSerializable<?> jsonSerializable) throws IOException {
        return serializeJsonSerializableWithReturn(jsonSerializable, aos -> aos.toString(StandardCharsets.UTF_8));
    }

    private static <T> T serializeJsonSerializableWithReturn(JsonSerializable<?> jsonSerializable,
        Function<AccessibleByteArrayOutputStream, T> returner) throws IOException {
        try (AccessibleByteArrayOutputStream outputStream = new AccessibleByteArrayOutputStream();
            JsonWriter jsonWriter = JsonProviders.createWriter(outputStream)) {
            jsonWriter.writeJson(jsonSerializable).flush();

            return returner.apply(outputStream);
        }
    }

    /**
     * Serializes the {@code jsonSerializable} as an instance of {@code JsonSerializable}.
     *
     * @param jsonSerializable The {@code JsonSerializable} content.
     * @param outputStream Where the serialized {@code JsonSerializable} will be written.
     * @throws IOException If an error occurs during serialization.
     */
    public static void serializeJsonSerializableIntoOutputStream(JsonSerializable<?> jsonSerializable,
        OutputStream outputStream) throws IOException {
        try (JsonWriter jsonWriter = JsonProviders.createWriter(outputStream)) {
            jsonWriter.writeJson(jsonSerializable).flush();
        }
    }

    /**
     * Deserializes the {@code json} as an instance of {@code JsonSerializable}.
     *
     * @param jsonSerializable The {@code JsonSerializable} represented by the {@code json}.
     * @param json The JSON being deserialized.
     * @return An instance of {@code jsonSerializable} based on the {@code json}.
     * @throws IOException If an error occurs during deserialization.
     * @throws IllegalStateException If the {@code jsonSerializable} does not have a static {@code fromJson} method
     * @throws Error If an error occurs during deserialization.
     */
    public static Object deserializeAsJsonSerializable(Class<?> jsonSerializable, byte[] json) throws IOException {
        if (FROM_JSON_CACHE.size() >= 10000) {
            FROM_JSON_CACHE.clear();
        }

        ReflectiveInvoker readJson = FROM_JSON_CACHE.computeIfAbsent(jsonSerializable, clazz -> {
            try {
                return ReflectionUtils.getMethodInvoker(clazz,
                    jsonSerializable.getDeclaredMethod("fromJson", JsonReader.class));
            } catch (Exception e) {
                throw LOGGER.logExceptionAsError(new IllegalStateException(e));
            }
        });

        try (JsonReader jsonReader = JsonProviders.createReader(json)) {
            return readJson.invokeStatic(jsonReader);
        } catch (Throwable e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof Exception) {
                throw new IOException(e);
            } else {
                throw (Error) e;
            }
        }
    }

    /**
     * Whether {@code XmlSerializable} is supported and the {@code bodyContentClass} is an instance of it.
     *
     * @param bodyContentClass The body content class.
     * @return Whether {@code bodyContentClass} can be used as {@code XmlSerializable}.
     */
    public static boolean supportsXmlSerializable(Class<?> bodyContentClass) {
        return XML_SERIALIZABLE_SUPPORTED && XML_SERIALIZABLE.isAssignableFrom(bodyContentClass);
    }

    /**
     * Serializes the {@code bodyContent} as an instance of {@code XmlSerializable}.
     *
     * @param xmlSerializable The {@code XmlSerializable} body content.
     * @return The {@link ByteBuffer} representing the serialized {@code bodyContent}.
     * @throws IOException If the XmlWriter fails to close properly.
     */
    public static ByteBuffer serializeXmlSerializableToByteBuffer(Object xmlSerializable) throws IOException {
        return serializeXmlSerializableWithReturn(xmlSerializable, AccessibleByteArrayOutputStream::toByteBuffer);
    }

    /**
     * Serializes the {@code bodyContent} as an instance of {@code XmlSerializable}.
     *
     * @param xmlSerializable The {@code XmlSerializable} body content.
     * @return The {@code byte[]} representing the serialized {@code bodyContent}.
     * @throws IOException If the XmlWriter fails to close properly.
     */
    public static byte[] serializeXmlSerializableToBytes(Object xmlSerializable) throws IOException {
        return serializeXmlSerializableWithReturn(xmlSerializable, AccessibleByteArrayOutputStream::toByteArray);
    }

    /**
     * Serializes the {@code bodyContent} as an instance of {@code XmlSerializable}.
     *
     * @param xmlSerializable The {@code XmlSerializable} body content.
     * @return The {@link String} representing the serialized {@code bodyContent}.
     * @throws IOException If the XmlWriter fails to close properly.
     */
    public static String serializeXmlSerializableToString(Object xmlSerializable) throws IOException {
        return serializeXmlSerializableWithReturn(xmlSerializable, aos -> aos.toString(StandardCharsets.UTF_8));
    }

    private static <T> T serializeXmlSerializableWithReturn(Object xmlSerializable,
        Function<AccessibleByteArrayOutputStream, T> returner) throws IOException {
        try (AccessibleByteArrayOutputStream outputStream = new AccessibleByteArrayOutputStream();
            AutoCloseable xmlWriter
                = callXmlInvoker(AutoCloseable.class, () -> XML_WRITER_CREATOR.invokeStatic(outputStream))) {
            callXmlInvoker(Object.class, () -> XML_WRITER_WRITE_XML_START_DOCUMENT.invokeWithArguments(xmlWriter));
            callXmlInvoker(Object.class,
                () -> XML_WRITER_WRITE_XML_SERIALIZABLE.invokeWithArguments(xmlWriter, xmlSerializable));
            callXmlInvoker(Object.class, () -> XML_WRITER_FLUSH.invokeWithArguments(xmlWriter));

            return returner.apply(outputStream);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Serializes the {@code xmlSerializable} as an instance of {@code XmlSerializable}.
     *
     * @param xmlSerializable The {@code XmlSerializable} content.
     * @param outputStream Where the serialized {@code XmlSerializable} will be written.
     * @throws IOException If an error occurs during serialization.
     */
    public static void serializeXmlSerializableIntoOutputStream(Object xmlSerializable, OutputStream outputStream)
        throws IOException {
        try (AutoCloseable xmlWriter
            = callXmlInvoker(AutoCloseable.class, () -> XML_WRITER_CREATOR.invokeStatic(outputStream))) {
            callXmlInvoker(Object.class, () -> XML_WRITER_WRITE_XML_START_DOCUMENT.invokeWithArguments(xmlWriter));
            callXmlInvoker(Object.class,
                () -> XML_WRITER_WRITE_XML_SERIALIZABLE.invokeWithArguments(xmlWriter, xmlSerializable));
            callXmlInvoker(Object.class, () -> XML_WRITER_FLUSH.invokeWithArguments(xmlWriter));
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Deserializes the {@code xml} as an instance of {@code XmlSerializable}.
     *
     * @param xmlSerializable The {@code XmlSerializable} represented by the {@code xml}.
     * @param xml The XML being deserialized.
     * @return An instance of {@code xmlSerializable} based on the {@code xml}.
     * @throws IOException If the XmlReader fails to close properly.
     * @throws IllegalStateException If the {@code xmlSerializable} does not have a static {@code fromXml} method
     * @throws Error If an error occurs during deserialization.
     */
    public static Object deserializeAsXmlSerializable(Class<?> xmlSerializable, byte[] xml) throws IOException {
        if (!XML_SERIALIZABLE_SUPPORTED) {
            return null;
        }

        if (FROM_XML_CACHE.size() >= 10000) {
            FROM_XML_CACHE.clear();
        }

        ReflectiveInvoker readXml = FROM_XML_CACHE.computeIfAbsent(xmlSerializable, clazz -> {
            try {
                return ReflectionUtils.getMethodInvoker(xmlSerializable,
                    xmlSerializable.getDeclaredMethod("fromXml", XML_READER));
            } catch (Exception e) {
                throw LOGGER.logExceptionAsError(new IllegalStateException(e));
            }
        });

        try (AutoCloseable xmlReader
            = callXmlInvoker(AutoCloseable.class, () -> XML_READER_CREATOR.invokeStatic((Object) xml))) {
            return readXml.invokeStatic(xmlReader);
        } catch (Throwable e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof Exception) {
                throw new IOException(e);
            } else {
                throw (Error) e;
            }
        }
    }

    private static <T> T callXmlInvoker(Class<T> returnType, Callable<Object> invoker) throws XMLStreamException {
        try {
            return returnType.cast(invoker.call());
        } catch (Exception exception) {
            if (exception instanceof XMLStreamException) {
                throw (XMLStreamException) exception;
            } else {
                throw new XMLStreamException(exception);
            }
        }
    }

    private ReflectionSerializable() {
    }
}
