package com.comeonbaby.android.app.server;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by olegs on 27.12.2016.
 */

public class TestServer {
    public static void main(String[] args) {
        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            URL url = new URL(ServerConsts.SERVER_PROTOCOL, ServerConsts.SERVER_HOST, ServerConsts.SERVER_PORT, ServerConsts.REQ_USER_PROOFILE);
            HttpRequest request = new HttpRequest(url, ServerConsts.TIMEOUT, HttpRequest.METHOD_GET, HttpRequest.CONTENT_JSON);
            request.addRequestProperty("Test_Prop01", "Test_Value_01");
            request.addRequestProperty("Test_Prop02", "Test_Value_02");
            HttpResponse response = request.start();
            System.out.println(response);

            url = new URL(ServerConsts.SERVER_PROTOCOL, ServerConsts.SERVER_HOST, ServerConsts.SERVER_PORT, ServerConsts.REQ_LIST_CITY);
            request = new HttpRequest(url, ServerConsts.TIMEOUT, HttpRequest.METHOD_GET, HttpRequest.CONTENT_JSON);
            request.addRequestProperty("Test_Prop01", "Test_Value_01");
            response = request.start();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
