package com.example.nsity.schooldiary.system;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.database.tables.PeriodDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 06.12.15.
 */
public class SyncManager {

    public static void sync(final Context context, final String classId, final CallBack callBack) {

        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_timetable) + classId;

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
                    TimeDBInterface dbTime = new TimeDBInterface(context);
                    dbTime.save(timesArray, true);
                    dbTime.closeDB();

                    JSONArray subjectsArray = (JSONArray) result.get(context.getString(R.string.subjects));
                    SubjectsClassDBInterface dbSubjects = new SubjectsClassDBInterface(context);
                    dbSubjects.save(subjectsArray, true);
                    dbSubjects.closeDB();

                    JSONArray timetableArray = (JSONArray) result.get(context.getString(R.string.timetable));
                    TimetableDBInterface dbTimetable = new TimetableDBInterface(context);
                    dbTimetable.save(timetableArray, true);
                    dbTimetable.closeDB();

                    JSONArray periodsArray = (JSONArray) result.get(context.getString(R.string.periods));
                    PeriodDBInterface dbPeriods = new PeriodDBInterface(context);
                    dbPeriods.save(periodsArray, true);
                    dbPeriods.closeDB();

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