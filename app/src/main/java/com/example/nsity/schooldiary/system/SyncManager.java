package com.example.nsity.schooldiary.system;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.database.tables.PeriodDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ErrorTracker;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 06.12.15.
 */
public class SyncManager {

    public static void sync(final Context context, final CallBack callBack) {

        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_timetable) + Preferences.get(Preferences.CLASSID, context) + "/" + Preferences.get(Preferences.PUPILID, context);

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
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
                    TimeDBInterface dbTime = new TimeDBInterface(context);
                    dbTime.save(timesArray, true);

                    JSONArray subjectsArray = (JSONArray) result.get(context.getString(R.string.subjects));
                    SubjectsClassDBInterface dbSubjects = new SubjectsClassDBInterface(context);
                    dbSubjects.save(subjectsArray, true);

                    JSONArray timetableArray = (JSONArray) result.get(context.getString(R.string.timetable));
                    TimetableDBInterface dbTimetable = new TimetableDBInterface(context);
                    dbTimetable.save(timetableArray, true);

                    JSONArray periodsArray = (JSONArray) result.get(context.getString(R.string.periods));
                    PeriodDBInterface dbPeriods = new PeriodDBInterface(context);
                    dbPeriods.save(periodsArray, true);

                    /*JSONArray progressArray = (JSONArray) result.get(context.getString(R.string.progress_marks));
                    ProgressDBInterface dbProgress = new ProgressDBInterface(context);
                    dbProgress.save(progressArray, true);*/

                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(context.getString(R.string.error_response));
                }
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, object.getResponse().toString()));
            }
        });
    }
}
