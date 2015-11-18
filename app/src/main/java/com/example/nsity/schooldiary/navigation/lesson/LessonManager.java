package com.example.nsity.schooldiary.navigation.lesson;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.ErrorTracker;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 18.11.15.
 */
public class LessonManager {

    public static void getLesson(final Context context, final String pupilId, final String subjectId, final String day, final String timeId, final CallBack callBack) {

        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_lesson) + pupilId + "/"
                + subjectId + "/" + day + "/" + timeId;

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
                    JSONArray timesArray = (JSONArray) result.get(context.getString(R.string.time));

                    /*TimeDBInterface dbTime = new TimeDBInterface(context);
                    dbTime.save(timesArray, true);*/


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
