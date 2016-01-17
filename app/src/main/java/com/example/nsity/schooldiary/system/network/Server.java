package com.example.nsity.schooldiary.system.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by nsity on 15.11.15.
 */
public class Server {
    public static final int TIMEOUT = 30000; //30sec
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    public static AsyncHttpClient getHttpClient() {
        httpClient.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
        httpClient.setTimeout(TIMEOUT);
        return httpClient;
    }

    public static boolean isOnline(Context context) {
        if (context == null)
            return false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
