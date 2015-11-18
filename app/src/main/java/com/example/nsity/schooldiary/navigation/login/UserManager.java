package com.example.nsity.schooldiary.navigation.login;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.ErrorTracker;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.ADBWorker;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by nsity on 15.11.15.
 */
public class UserManager {

    public static void login(final Context context, final String login, final String password, final CallBack callBack) {
        String encodedLogin, encodedPassword;
        try {
            encodedLogin = URLEncoder.encode(login, "utf-8");
            encodedPassword = URLEncoder.encode(password, "utf-8");
        } catch (UnsupportedEncodingException e) {
            callBack.onFail(context.getString(R.string.error_encode));
            return;
        }

        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_login) + encodedLogin + "/" + encodedPassword;

        new AsyncHttpResponse(url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                HashMap<String, String> result = new HashMap<>();

                try {
                    JSONObject response = (JSONObject)object.getResponse();

                    Preferences.set(Preferences.PUPILID, response.getString(context.getString(R.string.id)), context);
                    Preferences.set(Preferences.CLASSID, response.getString(context.getString(R.string.class_id)), context);
                    Preferences.set(Preferences.FIO, response.getString(context.getString(R.string.fio)), context);
                    Preferences.set(Preferences.CLASSNAME, response.getString(context.getString(R.string.class_name)), context);

                } catch (JSONException e) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }
                callBack.onSuccess();
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, object.getResponse().toString()));
            }
        });
    }


    public static void logoff(Context context) {
        ADBWorker.deleteAllTables(context);//remove all tables in database
        Preferences.clear(context);
    }
}
