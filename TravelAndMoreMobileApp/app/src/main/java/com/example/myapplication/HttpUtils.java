package com.example.myapplication;

import android.content.Context;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;

public class HttpUtils {
    private static final String BASE_URL = "<API URL GOES HERE>";


    public static String accessToken = "";
    public static String refreshToken = "";
    public static String stateToken = "";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity entity, String contentType, boolean authorize, ResponseHandlerInterface responseHandler) {
        client.removeHeader("Authorization");
        if (authorize) {
          if (HttpUtils.accessToken != "") {
            client.addHeader("Authorization", "Bearer " + HttpUtils.accessToken);
          }
          if (HttpUtils.stateToken != "") {
            client.addHeader("StateToken", HttpUtils.stateToken);
          }
        }
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public RequestHandle post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        return client.post(context, getAbsoluteUrl(url), headers, entity, contentType, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
