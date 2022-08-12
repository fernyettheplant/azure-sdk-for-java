// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.automation.generated;

import com.azure.core.util.Context;
import com.azure.resourcemanager.automation.models.Credential;

/** Samples for Credential Update. */
public final class CredentialUpdateSamples {
    /*
     * x-ms-original-file: specification/automation/resource-manager/Microsoft.Automation/preview/2020-01-13-preview/examples/updateCredential_patch.json
     */
    /**
     * Sample code: Update a credential.
     *
     * @param manager Entry point to AutomationManager.
     */
    public static void updateACredential(com.azure.resourcemanager.automation.AutomationManager manager) {
        Credential resource =
            manager
                .credentials()
                .getWithResponse("rg", "myAutomationAccount18", "myCredential", Context.NONE)
                .getValue();
        resource
            .update()
            .withName("myCredential")
            .withUsername("mylingaiah")
            .withPassword("<password>")
            .withDescription("my description goes here")
            .apply();
    }
}