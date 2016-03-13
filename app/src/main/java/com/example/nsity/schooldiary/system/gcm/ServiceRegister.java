package com.example.nsity.schooldiary.system.gcm;

import android.content.Context;
import android.util.Log;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;
import com.example.nsity.schooldiary.system.network.Server;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by nsity on 21.02.16.
 */
public class ServiceRegister {

    public static void unregister(final Context context, String token) {
        if (!Server.isOnline(context) || token == null || token.equals(""))
            return;

        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_push_unregister) + Preferences.get(Preferences.PUPILID, context) + "/" + token;

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_SYNCHRONIZATION_RESPONSE, new CallBack<ResponseObject>());

    }
    public static void register(final Context context, String token) {
        if (!Server.isOnline(context) || token == null || token.equals(""))
            return;

        String url =  context.getString(R.string.base_url) +
                context.getString(R.string.call_method_push_register) + Preferences.get(Preferences.PUPILID, context) + "/" + token;

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_SYNCHRONIZATION_RESPONSE, new CallBack<ResponseObject>());
    }


    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

}

