// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.core.implementation.jackson;

import com.azure.core.annotation.HeaderCollection;
import com.azure.core.http.HttpHeader;
import com.azure.core.http.HttpHeaders;
import com.azure.core.implementation.ReflectionUtils;
import com.azure.core.implementation.ReflectiveInvoker;
import com.azure.core.implementation.TypeUtil;
import com.azure.core.util.logging.ClientLogger;
import com.azure.core.util.logging.LogLevel;
import com.azure.core.util.serializer.MemberNameConverter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Wraps {@link ObjectMapper} creation and proxies calls and provides diagnostics info in case of potential version
 * mismatch issues that manifest with {@link LinkageError}.
 */
public final class ObjectMapperShim {
    private static final ClientLogger LOGGER = new ClientLogger(ObjectMapperShim.class);

    // don't add static fields that might cause Jackson classes to initialize
    private static final int CACHE_SIZE_LIMIT = 10000;

    private static final Map<Type, JavaType> TYPE_TO_JAVA_TYPE_CACHE = new ConcurrentHashMap<>();
    private static final Map<Type, ReflectiveInvoker> TYPE_TO_STRONGLY_TYPED_HEADERS_CONSTRUCTOR_CACHE
        = new ConcurrentHashMap<>();

    // Dummy constant that indicates an HttpHeaders-based constructor wasn't found for the Type.
    private static final ReflectiveInvoker NO_CONSTRUCTOR_REFLECTIVE_INVOKER = ReflectionUtils.createNoOpInvoker();

    /**
     * Creates and configures JSON {@code ObjectMapper} capable of serializing azure.core types, with flattening and
     * additional properties support.
     *
     * @param innerMapperShim inner mapper to use for non-azure specific serialization.
     * @param configure applies additional configuration to {@code ObjectMapper}.
     * @return Instance of shimmed {@code ObjectMapperShim}.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public static ObjectMapperShim createJsonMapper(ObjectMapperShim innerMapperShim,
        BiConsumer<ObjectMapper, ObjectMapper> configure) {
        try {
            ObjectMapper mapper = ObjectMapperFactory.INSTANCE.createJsonMapper(innerMapperShim.mapper);
            configure.accept(mapper, innerMapperShim.mapper);
            return new ObjectMapperShim(mapper);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Creates and configures XML {@code ObjectMapper} capable of serializing azure.core types.
     *
     * @return Instance of shimmed {@code ObjectMapperShim}.
     * @throws LinkageError if Jackson version mismatch is detected or XML isn't available.
     */
    public static ObjectMapperShim createXmlMapper() {
        try {
            ObjectMapper mapper = ObjectMapperFactory.INSTANCE.createXmlMapper();
            return new ObjectMapperShim(mapper);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Creates and configures JSON {@code ObjectMapper}.
     *
     * @return Instance of shimmed {@code ObjectMapperShim}.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public static ObjectMapperShim createSimpleMapper() {
        try {
            ObjectMapper mapper = ObjectMapperFactory.INSTANCE.createSimpleMapper();
            return new ObjectMapperShim(mapper);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Creates JSON {@code ObjectMapper} with default Jackson settings.
     *
     * @return Instance of shimmed {@code ObjectMapperShim}.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public static ObjectMapperShim createDefaultMapper() {
        try {
            ObjectMapper mapper = ObjectMapperFactory.INSTANCE.createDefaultMapper();
            return new ObjectMapperShim(mapper);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Creates JSON {@code ObjectMapper} with default Jackson settings, but capable of pretty-printing.
     *
     * @return Instance of shimmed {@code ObjectMapperShim}.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public static ObjectMapperShim createPrettyPrintMapper() {
        try {
            ObjectMapper mapper = ObjectMapperFactory.INSTANCE.createPrettyPrintMapper();
            return new ObjectMapperShim(mapper);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Creates and configures JSON {@code ObjectMapper} for headers serialization.
     *
     * @return Instance of shimmed {@code ObjectMapperShim}.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public static ObjectMapperShim createHeaderMapper() {
        try {
            ObjectMapper mapper = ObjectMapperFactory.INSTANCE.createHeaderMapper();
            return new ObjectMapperShim(mapper);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    private final ObjectMapper mapper;
    private MemberNameConverterImpl memberNameConverter;

    /**
     * Creates instance of {@link ObjectMapperShim}.
     *
     * @param mapper {@link ObjectMapper} to wrap.
     */
    public ObjectMapperShim(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Serializes Java object as a string.
     *
     * @param value object to serialize.
     * @return Serialized string.
     * @throws IOException if serialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public String writeValueAsString(Object value) throws IOException {
        try {
            return mapper.writeValueAsString(value);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Serializes Java object as a byte array.
     *
     * @param value object to serialize.
     * @return Serialized byte array.
     * @throws IOException if serialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public byte[] writeValueAsBytes(Object value) throws IOException {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Serializes Java object and write it to stream.
     *
     * @param out stream to write serialized object to.
     * @param value object to serialize.
     * @throws IOException if serialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public void writeValue(OutputStream out, Object value) throws IOException {
        try {
            mapper.writeValue(out, value);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Deserializes Java object from a string.
     *
     * @param <T> type of the value.
     * @param content serialized object.
     * @param valueType type of the value.
     * @return Deserialized object.
     * @throws IOException if deserialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public <T> T readValue(String content, final Type valueType) throws IOException {
        try {
            final JavaType javaType = createJavaType(valueType);
            return mapper.readValue(content, javaType);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Deserializes Java object from a byte array.
     *
     * @param <T> type of the value.
     * @param src serialized object.
     * @param valueType type of the value.
     * @return Deserialized object.
     * @throws IOException if deserialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public <T> T readValue(byte[] src, final Type valueType) throws IOException {
        try {
            final JavaType javaType = createJavaType(valueType);
            return mapper.readValue(src, javaType);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Reads and deserializes Java object from a stream.
     *
     * @param <T> type of the value.
     * @param src serialized object.
     * @param valueType type of the value.
     * @return Deserialized object.
     * @throws IOException if deserialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public <T> T readValue(InputStream src, final Type valueType) throws IOException {
        try {
            final JavaType javaType = createJavaType(valueType);
            return mapper.readValue(src, javaType);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Reads JSON tree from string.
     *
     * @param content serialized JSON tree.
     * @return {@code JsonNode} instance
     * @throws IOException if deserialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public JsonNode readTree(String content) throws IOException {
        try {
            return mapper.readTree(content);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /**
     * Reads JSON tree from byte array.
     *
     * @param content serialized JSON tree.
     * @return {@code JsonNode} instance
     * @throws IOException if deserialization fails.
     * @throws LinkageError if Jackson version mismatch is detected.
     */
    public JsonNode readTree(byte[] content) throws IOException {
        try {
            return mapper.readTree(content);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    private JavaType createJavaType(Type type) {
        if (type == null) {
            return null;
        } else if (type instanceof JavaType) {
            return (JavaType) type;
        } else if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            JavaType[] javaTypeArguments = new JavaType[actualTypeArguments.length];
            for (int i = 0; i != actualTypeArguments.length; i++) {
                javaTypeArguments[i] = createJavaType(actualTypeArguments[i]);
            }

            return getFromTypeCache(type, t -> mapper.getTypeFactory()
                .constructParametricType((Class<?>) parameterizedType.getRawType(), javaTypeArguments));
        } else {
            return getFromTypeCache(type, t -> mapper.getTypeFactory().constructType(t));
        }
    }

    /**
     * Deserializes the given {@link HttpHeaders} into an instance of the given {@code deserializedHeadersType}.
     *
     * @param <T> The type of the deserialized headers.
     * @param headers The {@link HttpHeaders} to deserialize.
     * @param deserializedHeadersType The {@link Type} of the deserialized headers.
     * @return An instance of the given {@code deserializedHeadersType} with the values from {@code headers}.
     * @throws IOException If an error occurs while deserializing the headers.
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(HttpHeaders headers, Type deserializedHeadersType) throws IOException {
        if (deserializedHeadersType == null) {
            return null;
        }

        try {
            ReflectiveInvoker constructor = getFromHeadersConstructorCache(deserializedHeadersType);

            if (constructor != NO_CONSTRUCTOR_REFLECTIVE_INVOKER) {
                return (T) constructor.invokeWithArguments(headers);
            }
        } catch (Exception exception) {
            // invokeWithArguments will fail with a non-RuntimeException if the reflective call was invalid.
            if (exception instanceof RuntimeException) {
                throw LOGGER.logExceptionAsError((RuntimeException) exception);
            }

            LOGGER.log(LogLevel.VERBOSE, () -> "Failed to find or use invoker Constructor that accepts HttpHeaders for "
                + deserializedHeadersType + ".");
        }

        T deserializedHeaders = mapper.convertValue(headers, createJavaType(deserializedHeadersType));

        final Class<?> deserializedHeadersClass = TypeUtil.getRawClass(deserializedHeadersType);
        final Field[] declaredFields = deserializedHeadersClass.getDeclaredFields();

        /*
         * A list containing all handlers for header collections of the header type.
         */
        final List<HeaderCollectionHandler> headerCollectionHandlers = new ArrayList<>();

        /*
         * This set is an optimization where we track the first character of all HeaderCollections defined on the
         * deserialized headers type. This allows us to optimize away startWiths checks which are much more costly than
         * getting the first character.
         */
        final Set<Character> headerCollectionsFirstCharacters = new HashSet<>();

        /*
         * Begin by looping over all declared fields and initializing all header collection information.
         */
        for (final Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(HeaderCollection.class)) {
                continue;
            }

            final Type declaredFieldType = declaredField.getGenericType();
            if (!TypeUtil.isTypeOrSubTypeOf(declaredField.getType(), Map.class)) {
                continue;
            }

            final Type[] mapTypeArguments = TypeUtil.getTypeArguments(declaredFieldType);
            if (mapTypeArguments.length != 2 || mapTypeArguments[0] != String.class
                || mapTypeArguments[1] != String.class) {
                continue;
            }

            final HeaderCollection headerCollectionAnnotation = declaredField.getAnnotation(HeaderCollection.class);
            final String headerCollectionPrefix = headerCollectionAnnotation.value().toLowerCase(Locale.ROOT);
            final int headerCollectionPrefixLength = headerCollectionPrefix.length();
            if (headerCollectionPrefixLength == 0) {
                continue;
            }

            headerCollectionHandlers.add(new HeaderCollectionHandler(headerCollectionPrefix, declaredField));
            headerCollectionsFirstCharacters.add(headerCollectionPrefix.charAt(0));
        }

        /*
         * Then loop over all headers and check if they begin with any of the prefixes found.
         */
        for (final HttpHeader header : headers) {
            String headerNameLower = header.getName().toLowerCase(Locale.ROOT);

            /*
             * Optimization to skip this header as it doesn't begin with any character starting header collections in
             * the deserialized headers type.
             */
            if (!headerCollectionsFirstCharacters.contains(headerNameLower.charAt(0))) {
                continue;
            }

            for (HeaderCollectionHandler headerCollectionHandler : headerCollectionHandlers) {
                if (headerCollectionHandler.headerStartsWithPrefix(headerNameLower)) {
                    headerCollectionHandler.addHeader(header.getName(), header.getValue());
                }
            }
        }

        /*
         * Finally, inject all found header collection values into the deserialized headers.
         */
        headerCollectionHandlers.forEach(h -> h.injectValuesIntoDeclaringField(deserializedHeaders, LOGGER));

        return deserializedHeaders;
    }

    /**
     * Gets the name of the given {@link Member} using the configured {@link MemberNameConverter}.
     *
     * @param member The {@link Member} to get the name of.
     * @return The name of the given {@link Member}.
     */
    public String convertMemberName(Member member) {
        if (memberNameConverter == null) {
            // Defer creating the member name converter until it needs to be used.
            // This class isn't used often and performs a lot of reflection, so best it is deferred.
            // Don't both making this volatile or synchronized as this is very, very cheap to create.
            memberNameConverter = new MemberNameConverterImpl(mapper);
        }

        try {
            return memberNameConverter.convertMemberName(member);
        } catch (LinkageError ex) {
            throw LOGGER.logThrowableAsError(new LinkageError(JacksonVersion.getHelpInfo(), ex));
        }
    }

    /*
     * Helper methods that gets the value for the given key from the cache.
     */
    private static JavaType getFromTypeCache(Type key, Function<Type, JavaType> compute) {
        if (TYPE_TO_JAVA_TYPE_CACHE.size() >= CACHE_SIZE_LIMIT) {
            TYPE_TO_JAVA_TYPE_CACHE.clear();
        }

        return TYPE_TO_JAVA_TYPE_CACHE.computeIfAbsent(key, compute);
    }

    private static ReflectiveInvoker getFromHeadersConstructorCache(Type key) {
        if (TYPE_TO_STRONGLY_TYPED_HEADERS_CONSTRUCTOR_CACHE.size() >= CACHE_SIZE_LIMIT) {
            TYPE_TO_STRONGLY_TYPED_HEADERS_CONSTRUCTOR_CACHE.clear();
        }

        return TYPE_TO_STRONGLY_TYPED_HEADERS_CONSTRUCTOR_CACHE.computeIfAbsent(key, type -> {
            try {
                Class<?> headersClass = TypeUtil.getRawClass(type);
                return ReflectionUtils.getConstructorInvoker(headersClass,
                    headersClass.getDeclaredConstructor(HttpHeaders.class));
            } catch (Throwable throwable) {
                if (throwable instanceof Error) {
                    throw LOGGER.logThrowableAsError((Error) throwable);
                }

                // In a previous implementation compute returned null here in an attempt to indicate that there is no
                // HttpHeaders-based declared constructor. Unfortunately, null isn't a valid indicator to
                // computeIfAbsent that a computation has been performed and this cache would never effectively be a
                // cache as compute would always be performed when there was no matching constructor.
                //
                // Now the implementation returns a dummy constant when there is no matching HttpHeaders-based
                // constructor. This now results in this case properly inserting into the cache and only running when a
                // new type is seen or the cache is cleared due to reaching capacity.
                //
                // With this change, benchmarking deserialize(HttpHeaders, Type) saw a 20% performance improvement.
                return NO_CONSTRUCTOR_REFLECTIVE_INVOKER;
            }
        });
    }
}
