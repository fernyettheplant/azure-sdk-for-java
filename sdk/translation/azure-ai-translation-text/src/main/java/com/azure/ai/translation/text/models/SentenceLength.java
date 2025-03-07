// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.ai.translation.text.models;

import com.azure.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** An object returning sentence boundaries in the input and output texts. */
@Immutable
public final class SentenceLength {
    /*
     * An integer array representing the lengths of the sentences in the input text.
     * The length of the array is the number of sentences, and the values are the length of each sentence.
     */
    @JsonProperty(value = "srcSentLen", required = true)
    private List<Integer> srcSentLen;

    /*
     * An integer array representing the lengths of the sentences in the translated text.
     * The length of the array is the number of sentences, and the values are the length of each sentence.
     */
    @JsonProperty(value = "transSentLen", required = true)
    private List<Integer> transSentLen;

    /**
     * Creates an instance of SentenceLength class.
     *
     * @param srcSentLen the srcSentLen value to set.
     * @param transSentLen the transSentLen value to set.
     */
    @JsonCreator
    private SentenceLength(
            @JsonProperty(value = "srcSentLen", required = true) List<Integer> srcSentLen,
            @JsonProperty(value = "transSentLen", required = true) List<Integer> transSentLen) {
        this.srcSentLen = srcSentLen;
        this.transSentLen = transSentLen;
    }

    /**
     * Get the srcSentLen property: An integer array representing the lengths of the sentences in the input text. The
     * length of the array is the number of sentences, and the values are the length of each sentence.
     *
     * @return the srcSentLen value.
     */
    public List<Integer> getSrcSentLen() {
        return this.srcSentLen;
    }

    /**
     * Get the transSentLen property: An integer array representing the lengths of the sentences in the translated text.
     * The length of the array is the number of sentences, and the values are the length of each sentence.
     *
     * @return the transSentLen value.
     */
    public List<Integer> getTransSentLen() {
        return this.transSentLen;
    }
}
