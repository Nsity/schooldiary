package com.example.nsity.schooldiary.navigation.marks.subjects;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.ErrorTracker;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class SubjectManager {
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
