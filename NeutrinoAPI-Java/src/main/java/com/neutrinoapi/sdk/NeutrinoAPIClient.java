/*
 * NeutrinoAPI
 *
 * This file was automatically generated for NeutrinoAPI by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.neutrinoapi.sdk;

import com.neutrinoapi.sdk.controllers.Telephony;

public class NeutrinoAPIClient {


    /**
     * Singleton access to Telephony controller
     *
     * @return Returns the Telephony instance
     */
    public Telephony getTelephony() {
        return Telephony.getInstance();
    }


    /**
     * Client initialization constructor
     */
    public NeutrinoAPIClient(String userId, String apiKey) {
        Configuration.userId = userId;
        Configuration.apiKey = apiKey;
    }
}