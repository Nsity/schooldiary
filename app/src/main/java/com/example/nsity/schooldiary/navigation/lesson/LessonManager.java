package com.example.nsity.schooldiary.navigation.lesson;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.ErrorTracker;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.database.tables.MarkDBInterface;
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

    public static void getLesson(final Context context, final int lessonId, final String pupilId, final String subjectId, final String day,
                                 final String timeId, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_lesson) + pupilId + "/"
                + subjectId + "/" + day + "/" + timeId;

        LessonDBInterface db = new LessonDBInterface(context);
        db.deleteLesson(lessonId);

        db.closeDB();

        new AsyncHttpResponse(url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                JSONObject response = (JSONObject)object.getResponse();
                try {

                    JSONArray result = response.getJSONArray(context.getString(R.string.result));

                    LessonDBInterface dbLesson = new LessonDBInterface(context);
                    dbLesson.save(result, false);
                    dbLesson.closeDB();

                    MarkDBInterface dbMark = new MarkDBInterface(context);

                    result = result.getJSONObject(0).getJSONArray(context.getString(R.string.marks));
                    int marks = dbMark.save(result, false);
                    dbMark.closeDB();

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
