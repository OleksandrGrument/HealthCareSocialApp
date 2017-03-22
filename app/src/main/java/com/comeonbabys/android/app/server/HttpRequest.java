package com.comeonbabys.android.app.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by olegs on 27.12.2016.
 */

public class HttpRequest {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";
    public static final String CONTENT_TEXT = "text";
    public static final String CONTENT_JSON = "application/json";

    private URL url;                                                //request url
    private int timeout;
    private String method;                                          //request method
    private String contentType;
    public boolean isReceived = false;                              //true if request has answer
    private HttpResponse httpResponse;
    private Map<String, String> properties = new HashMap<>();

    //Request constructor
    public HttpRequest(URL url, int timeout, String method, String contentType) {
        this.url = url;
        this.timeout = timeout;
        this.method = method;
        this.contentType = contentType;
    }

    //Uses to add request properties
    public void addRequestProperty(String key, String value) {
        properties.put(key, value);
    }

    //Starts connection and executes request
    public HttpResponse start() {
        httpResponse = new HttpResponse(this);              //set httpRequest field to httpResponse
        try {
            //Start connection and set properties
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod(method);
            for(Map.Entry<String, String> entry : properties.entrySet()){
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            int responseCode=conn.getResponseCode();            //get response code
            httpResponse.setResponseCode(responseCode);         //set response code to httpResponse;
            String response = "";

            //If Response code == 200, than read input stream and build response string
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
                br.close();
            }
            else {
                throw new HttpRetryException("HTTPRequest: Server connection fail ", responseCode);
            }
            httpResponse.setReceivedDataText(response);         //set receivedDataText field in httpResponse
            isReceived = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }
}
