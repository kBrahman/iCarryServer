/*
 * NeutrinoAPI
 *
 * This file was automatically generated for NeutrinoAPI by APIMATIC v2.0 ( https://apimatic.io ).
 */
package zig.i.carry.sms;


import org.springframework.beans.factory.annotation.Value;

public class Configuration {
    //The base Uri for API calls
    public static String baseUri = "https://neutrinoapi.com";

    //Your user ID
    @Value("${user-id}")
    public static String userId;

    //Your API key
    @Value("${api-key}")
    public static String apiKey;

}
