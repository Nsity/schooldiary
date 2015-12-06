package com.example.nsity.schooldiary.navigation.marks.progress;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.ErrorTracker;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressManager {

    public static void getProgress(final Context context, final CallBack callBack) {

        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_progress) + Preferences.get(Preferences.PUPILID, context) + "/" +
                Preferences.get(Preferences.CLASSID, context);

        new AsyncHttpResponse(url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                JSONObject response = (JSONObject) object.getResponse();
                try {
                    JSONObject result = response.getJSONObject(context.getString(R.string.result));

                    ProgressDBInterface db = new ProgressDBInterface(context);
                    db.save((JSONArray) result.get(context.getString(R.string.progress_marks)), true);
                    db.closeDB();

                } catch (JSONException e) {
                    e.printStackTrace();
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

}
