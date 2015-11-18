package com.example.nsity.schooldiary.system.network;

import com.example.nsity.schooldiary.system.CommonFunctions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nsity on 15.11.15.
 */
public class AsyncHttpResponse {
    private CallBack<ResponseObject> callBack;

    public static final int CALL_JSON_HTTP_RESPONSE = 0;
    public static final int CALL_POST_JSON_HTTP_RESPONSE = 1;

    private static final String EXCEPTION_TAG = "Exception";
    private static final String MESSAGE_TAG = "Message";
    public static final String MESSAGE = "timeout";

    public AsyncHttpResponse(){}

    public AsyncHttpResponse(String url, RequestParams params, int callMethod, CallBack<ResponseObject> callBack){
        this.callBack = callBack;

        switch (callMethod) {
            case CALL_JSON_HTTP_RESPONSE:
                callJsonHttpResponse(url, Server.getHttpClient());
                break;
            case CALL_POST_JSON_HTTP_RESPONSE:
                callPostJsonHttpResponse(url, params, Server.getHttpClient());
                break;
        }
    }


    public void callJsonHttpResponse(String url, AsyncHttpClient client){
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                response = (response == null) ? new JSONObject() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                response = (response == null) ? new JSONArray() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                response = (CommonFunctions.StringIsNullOrEmpty(response)) ? "" : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONObject() : errorResponse;

                if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
                    JSONObject error = new JSONObject();
                    try {
                        JSONObject exception = new JSONObject();
                        exception.put(MESSAGE_TAG, MESSAGE);
                        error.put(EXCEPTION_TAG, exception);
                    } catch (JSONException ignored) {
                    }
                    errorResponse = error;
                }

                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONArray() : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {
                errorResponse = (CommonFunctions.StringIsNullOrEmpty(errorResponse)) ? "" : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }
        });
    }


    public void callPostJsonHttpResponse(String url, RequestParams params, AsyncHttpClient client){
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                response = (response == null) ? new JSONObject() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                response = (response == null) ? new JSONArray() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                response = (CommonFunctions.StringIsNullOrEmpty(response)) ? "" : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONObject() : errorResponse;

                if (throwable instanceof SocketTimeoutException) {
                    JSONObject error = new JSONObject();
                    try {
                        JSONObject exception = new JSONObject();
                        exception.put(MESSAGE_TAG, MESSAGE);
                        error.put(EXCEPTION_TAG, exception);
                    } catch (JSONException ignored) {}
                    errorResponse = error;
                }

                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONArray() : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {
                errorResponse = (CommonFunctions.StringIsNullOrEmpty(errorResponse)) ? "" : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }
        });
    }
}