// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.cosmosdbforpostgresql.implementation;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.core.http.rest.SimpleResponse;
import com.azure.core.util.Context;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.cosmosdbforpostgresql.fluent.PrivateLinkResourcesClient;
import com.azure.resourcemanager.cosmosdbforpostgresql.fluent.models.PrivateLinkResourceInner;
import com.azure.resourcemanager.cosmosdbforpostgresql.models.PrivateLinkResource;
import com.azure.resourcemanager.cosmosdbforpostgresql.models.PrivateLinkResources;

public final class PrivateLinkResourcesImpl implements PrivateLinkResources {
    private static final ClientLogger LOGGER = new ClientLogger(PrivateLinkResourcesImpl.class);

    private final PrivateLinkResourcesClient innerClient;

    private final com.azure.resourcemanager.cosmosdbforpostgresql.CosmosDBForPostgreSqlManager serviceManager;

    public PrivateLinkResourcesImpl(
        PrivateLinkResourcesClient innerClient,
        com.azure.resourcemanager.cosmosdbforpostgresql.CosmosDBForPostgreSqlManager serviceManager) {
        this.innerClient = innerClient;
        this.serviceManager = serviceManager;
    }

    public PagedIterable<PrivateLinkResource> listByCluster(String resourceGroupName, String clusterName) {
        PagedIterable<PrivateLinkResourceInner> inner =
            this.serviceClient().listByCluster(resourceGroupName, clusterName);
        return Utils.mapPage(inner, inner1 -> new PrivateLinkResourceImpl(inner1, this.manager()));
    }

    public PagedIterable<PrivateLinkResource> listByCluster(
        String resourceGroupName, String clusterName, Context context) {
        PagedIterable<PrivateLinkResourceInner> inner =
            this.serviceClient().listByCluster(resourceGroupName, clusterName, context);
        return Utils.mapPage(inner, inner1 -> new PrivateLinkResourceImpl(inner1, this.manager()));
    }

    public Response<PrivateLinkResource> getWithResponse(
        String resourceGroupName, String clusterName, String privateLinkResourceName, Context context) {
        Response<PrivateLinkResourceInner> inner =
            this.serviceClient().getWithResponse(resourceGroupName, clusterName, privateLinkResourceName, context);
        if (inner != null) {
            return new SimpleResponse<>(
                inner.getRequest(),
                inner.getStatusCode(),
                inner.getHeaders(),
                new PrivateLinkResourceImpl(inner.getValue(), this.manager()));
        } else {
            return null;
        }
    }

    public PrivateLinkResource get(String resourceGroupName, String clusterName, String privateLinkResourceName) {
        PrivateLinkResourceInner inner =
            this.serviceClient().get(resourceGroupName, clusterName, privateLinkResourceName);
        if (inner != null) {
            return new PrivateLinkResourceImpl(inner, this.manager());
        } else {
            return null;
        }
    }

    private PrivateLinkResourcesClient serviceClient() {
        return this.innerClient;
    }

    private com.azure.resourcemanager.cosmosdbforpostgresql.CosmosDBForPostgreSqlManager manager() {
        return this.serviceManager;
    }
}
