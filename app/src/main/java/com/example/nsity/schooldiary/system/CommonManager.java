package com.example.nsity.schooldiary.system;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.subjects.SubjectMark;
import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 08.12.15.
 */
public class CommonManager {
    public static void getLesson(final Context context, final int lessonId, final int subjectId, final String day,
                                 final int timeId, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_lesson) + Preferences.get(Preferences.PUPILID, context) + "/"
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

                    response = response.getJSONObject(context.getString(R.string.result));

                    JSONArray result = new JSONArray();
                    result.put(response);

                    LessonDBInterface dbLesson = new LessonDBInterface(context);
                    dbLesson.save(result, false);
                    dbLesson.closeDB();

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

    public static void getMarks(final Context context, final int subjectId, final int periodId, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_subject_marks) + Preferences.get(Preferences.PUPILID, context) + "/" +
                subjectId + "/" + periodId;

        new AsyncHttpResponse(url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                JSONObject response = (JSONObject)object.getResponse();

                JSONObject result;
                try {
                    result = response.getJSONObject(context.getString(R.string.result));

                    JSONArray marks = (JSONArray) result.get(context.getString(R.string.marks));

                    if (CommonFunctions.StringIsNullOrEmpty(marks.toString())) {
                        callBack.onFail(context.getString(R.string.error_response));
                        return;
                    }

                    ArrayList<SubjectMark> marksArrayList = new ArrayList<>();

                    for (int i = 0; i < marks.length(); i++) {
                        JSONObject mark = marks.getJSONObject(i);

                        SubjectMark subjectMark = new SubjectMark();

                        subjectMark.setId(CommonFunctions.getFieldInt(mark, context.getString(R.string.id)));
                        subjectMark.setValue(CommonFunctions.getFieldInt(mark, context.getString(R.string.mark)));
                        subjectMark.setDate(CommonFunctions.getFieldString(mark, context.getString(R.string.date)));
                        subjectMark.setType(CommonFunctions.getFieldString(mark, context.getString(R.string.type)));
                        subjectMark.setLessonId(CommonFunctions.getFieldInt(mark, context.getString(R.string.lessonId)));

                        marksArrayList.add(subjectMark);
                    }

                    callBack.onSuccess(marksArrayList);
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
