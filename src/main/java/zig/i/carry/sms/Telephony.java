/*
 * NeutrinoAPI
 *
 * This file was automatically generated for NeutrinoAPI by APIMATIC v2.0 ( https://apimatic.io ).
 */
package zig.i.carry.sms;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Telephony extends BaseController {
    //private static variables for the singleton pattern
    private static Object syncObject = new Object();
    private static Telephony instance = null;

    /**
     * Singleton pattern implementation
     *
     * @return The singleton instance of the Telephony class
     */
    public static Telephony getInstance() {
        synchronized (syncObject) {
            if (null == instance) {
                instance = new Telephony();
            }
        }
        return instance;
    }

    /**
     * Send a unique security code to any mobile device via SMS
     *
     * @param number       Required parameter: The phone number to send a verification code to
     * @param codeLength   Optional parameter: The number of digits to use in the security code (must be between 4 and 12)
     * @param securityCode Optional parameter: ass in your own security code. This is useful if you have implemented TOTP or similar 2FA methods. If not set then we will generate a secure random code (only numerical security codes are currently supported)
     * @param countryCode  Optional parameter: ISO 2-letter country code, assume numbers are based in this country. If not set numbers are assumed to be in international format (with or without the leading + sign)
     * @param languageCode Optional parameter: The language to send the verification code in, available languages are: de - German, en - English, es - Spanish, fr - Fench, it - Italian, pt - Portuguese, ru - Russian
     * @return Returns the SMSVerifyResponse response from the API call
     */
    public SMSVerifyResponse sMSVerify(
            final String number,
            final Integer codeLength,
            final Integer securityCode,
            final String countryCode,
            final String languageCode
    ) throws Throwable {
        APICallBackCatcher<SMSVerifyResponse> callback = new APICallBackCatcher<>();
        sMSVerifyAsync(number, codeLength, securityCode, countryCode, languageCode, callback);
        if (!callback.isSuccess())
            throw callback.getError();
        return callback.getResult();
    }

    /**
     * Send a unique security code to any mobile device via SMS
     *
     * @param number       Required parameter: The phone number to send a verification code to
     * @param codeLength   Optional parameter: The number of digits to use in the security code (must be between 4 and 12)
     * @param securityCode Optional parameter: ass in your own security code. This is useful if you have implemented TOTP or similar 2FA methods. If not set then we will generate a secure random code (only numerical security codes are currently supported)
     * @param countryCode  Optional parameter: ISO 2-letter country code, assume numbers are based in this country. If not set numbers are assumed to be in international format (with or without the leading + sign)
     * @param languageCode Optional parameter: The language to send the verification code in, available languages are: de - German, en - English, es - Spanish, fr - Fench, it - Italian, pt - Portuguese, ru - Russian
     * @return Returns the void response from the API call
     */
    public void sMSVerifyAsync(final String number, final Integer codeLength, final Integer securityCode, final String countryCode,
                               final String languageCode, final APICallBack<SMSVerifyResponse> callBack) {
        //the base uri for api requests
        String _baseUri = Configuration.baseUri;

        //prepare query string for API call
        StringBuilder _queryBuilder = new StringBuilder(_baseUri);
        _queryBuilder.append("/sms-verify");

        //process query parameters
        Configuration configuration = new Configuration();
        APIHelper.appendUrlWithQueryParameters(_queryBuilder, new HashMap<>() {
            private static final long serialVersionUID = 4902901676399440164L;

            {
                put("user-id", Configuration.userId);
                put("api-key", Configuration.apiKey);
            }
        });
        //validate and preprocess url
        String _queryUrl = APIHelper.cleanUrl(_queryBuilder);

        //load all headers for the outgoing API request
        Map<String, String> _headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 5116114496558888989L;

            {
                put("user-agent", "APIMATIC 2.0");
                put("accept", "application/json");
            }
        };

        //load all fields for the outgoing API request
        Map<String, Object> _parameters = new HashMap<String, Object>() {
            private static final long serialVersionUID = 5450318225184861132L;

            {
                put("output-case", "camel");
                put("number", number);
                put("code-length", (codeLength != null) ? codeLength : 5);
                put("security-code", securityCode);
                put("country-code", countryCode);
                put("language-code", (languageCode != null) ? languageCode : "en");
            }
        };

        //prepare and invoke the API call request to fetch the response
        final HttpRequest _request = getClientInstance().post(_queryUrl, _headers, APIHelper.prepareFormFields(_parameters));

        //invoke the callback before request if its not null
        if (getHttpCallBack() != null) {
            getHttpCallBack().OnBeforeRequest(_request);
        }

        //invoke request and get response
        Runnable _responseTask = new Runnable() {
            public void run() {
                //make the API call
                getClientInstance().executeAsStringAsync(_request, new APICallBack<HttpResponse>() {
                    public void onSuccess(HttpContext _context, HttpResponse _response) {
                        try {

                            //invoke the callback after response if its not null
                            if (getHttpCallBack() != null) {
                                getHttpCallBack().OnAfterResponse(_context);
                            }

                            //handle errors defined at the API level
                            validateResponse(_response, _context);

                            //extract result from the http response
                            String _responseBody = ((HttpStringResponse) _response).getBody();
                            SMSVerifyResponse _result = APIHelper.deserialize(_responseBody,
                                    new TypeReference<SMSVerifyResponse>() {
                                    });

                            //let the caller know of the success
                            callBack.onSuccess(_context, _result);
                        } catch (APIException error) {
                            //let the caller know of the error
                            callBack.onFailure(_context, error);
                        } catch (IOException ioException) {
                            //let the caller know of the caught IO Exception
                            callBack.onFailure(_context, ioException);
                        } catch (Exception exception) {
                            //let the caller know of the caught Exception
                            callBack.onFailure(_context, exception);
                        }
                    }

                    public void onFailure(HttpContext _context, Throwable _error) {
                        //invoke the callback after response if its not null
                        if (getHttpCallBack() != null) {
                            getHttpCallBack().OnAfterResponse(_context);
                        }

                        //let the caller know of the failure
                        callBack.onFailure(_context, _error);
                    }
                });
            }
        };

        //execute async using thread pool
        APIHelper.getScheduler().execute(_responseTask);
    }

}