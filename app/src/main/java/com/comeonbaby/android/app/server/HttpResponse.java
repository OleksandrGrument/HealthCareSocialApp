package com.comeonbaby.android.app.server;

/**
 * Created by olegs on 27.12.2016.
 */

public class HttpResponse {
    private int responseCode;
    private String receivedDataText;
    private HttpRequest httpRequest;

    public HttpResponse(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public int getResponseCode() {return responseCode;}
    public void setResponseCode(int responseCode) {this.responseCode = responseCode;}
    public HttpRequest getHttpRequest() {return httpRequest;}
    public void setHttpRequest(HttpRequest httpRequest) {this.httpRequest = httpRequest;}
    public String getReceivedDataText() {return receivedDataText;}
    public void setReceivedDataText(String receivedDataText) {this.receivedDataText = receivedDataText;}

    @Override
    public String toString() {
        return "HttpResponse: responseCode=" + responseCode + " receivedDataText='" + receivedDataText;
    }
}
