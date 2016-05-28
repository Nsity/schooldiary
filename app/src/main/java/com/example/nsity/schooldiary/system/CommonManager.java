package com.example.nsity.schooldiary.system;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.navigation.marks.Teachers;
import com.example.nsity.schooldiary.navigation.news.News;
import com.example.nsity.schooldiary.navigation.statistics.Score;
import com.example.nsity.schooldiary.navigation.marks.subjects.SubjectMark;
import com.example.nsity.schooldiary.navigation.marks.subjects.Subjects;
import com.example.nsity.schooldiary.system.database.tables.LessonsDBInterface;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ErrorTracker;
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

        LessonsDBInterface db = new LessonsDBInterface(context);
        db.deleteLesson(lessonId);

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
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

                    LessonsDBInterface dbLesson = new LessonsDBInterface(context);
                    dbLesson.save(result, false);

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

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                JSONObject response = (JSONObject)object.getResponse();
                try {
                    JSONObject result = response.getJSONObject(context.getString(R.string.result));

                    ProgressDBInterface db = new ProgressDBInterface(context);
                    db.save((JSONArray)result.get(context.getString(R.string.progress_marks)), true);

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

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
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



   /* public static void getHomework(final Context context, final int offset, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_homework) + Preferences.get(Preferences.CLASSID, context) + "/" +
                offset;

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

                    JSONArray lessons = (JSONArray) result.get(context.getString(R.string.lessons));

                    if (CommonFunctions.StringIsNullOrEmpty(lessons.toString())) {
                        callBack.onFail(context.getString(R.string.error_response));
                        return;
                    }

                    ArrayList<HomeworkItem> homeworkArrayList = new ArrayList<>();

                    for (int i = 0; i < lessons.length(); i++) {
                        JSONObject lesson = lessons.getJSONObject(i);

                        HomeworkItem homework = new HomeworkItem();

                        homework.setDate(CommonFunctions.getFieldString(lesson, context.getString(R.string.date)));
                        homework.setTask(CommonFunctions.getFieldString(lesson, context.getString(R.string.homework)));
                        homework.setTheme(CommonFunctions.getFieldString(lesson, context.getString(R.string.theme)));

                        Subject subject = new Subjects(context).findSubjectById(CommonFunctions.getFieldInt(lesson, context.getString(R.string.subject_id)));
                        homework.setSubject(subject);

                        homeworkArrayList.add(homework);
                    }

                    callBack.onSuccess(homeworkArrayList);
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
    }*/

    public static void getHomework(final Context context, final String beginDate, final String endDate, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_homework) + Preferences.get(Preferences.CLASSID, context) + "/" + Preferences.get(Preferences.PUPILID, context)
                + "/" + beginDate + "/" + endDate;

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

                    LessonsDBInterface dbLesson = new LessonsDBInterface(context);
                    dbLesson.deleteHomeworkForWeek(beginDate, endDate);
                    dbLesson.save((JSONArray)result.get(context.getString(R.string.lessons)), false);

                    callBack.onSuccess(beginDate);
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

    public static void getStatistics(final Context context, int periodId, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_statistics) + Preferences.get(Preferences.CLASSID, context) + "/" +
                Preferences.get(Preferences.PUPILID, context) + "/" + periodId;

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
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

                    JSONArray jsonArray = (JSONArray) result.get(context.getString(R.string.statistics));

                    if (CommonFunctions.StringIsNullOrEmpty(jsonArray.toString())) {
                        callBack.onFail(context.getString(R.string.error_response));
                        return;
                    }

                    ArrayList<Score> arrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject statistics = jsonArray.getJSONObject(i);

                        Score score = new Score();

                        JSONArray values = statistics.getJSONArray(context.getResources().getString(R.string.values));
                        for (int j = 0; j < values.length(); j++) {
                            JSONObject value = values.getJSONObject(j);
                            score.setSubject(new Subjects(context).findSubjectById(
                                    CommonFunctions.getFieldInt(statistics, context.getString(R.string.subject_id))));
                            score.setAverageScore(CommonFunctions.getFieldDouble(value, context.getString(R.string.average)));
                            score.setMinAverageScore(CommonFunctions.getFieldDouble(value, context.getString(R.string.min_average)));
                            score.setMaxAverageScore(CommonFunctions.getFieldDouble(value, context.getString(R.string.max_average)));
                        }
                        arrayList.add(score);
                    }

                    callBack.onSuccess(arrayList);
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


    public static void getNews(final Context context, final int page, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_news) + page;

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
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

                    JSONArray newsArray = (JSONArray) result.get(context.getString(R.string.news));

                    if (CommonFunctions.StringIsNullOrEmpty(newsArray.toString())) {
                        callBack.onFail(context.getString(R.string.error_response));
                        return;
                    }

                    ArrayList<News> newsArrayList = new ArrayList<>();

                    for (int i = 0; i < newsArray.length(); i++) {
                        JSONObject newsObject = newsArray.getJSONObject(i);

                        News news = new News(CommonFunctions.getFieldInt(newsObject, context.getString(R.string.id)),
                                CommonFunctions.getFieldString(newsObject, context.getString(R.string.theme)),
                                CommonFunctions.getFieldString(newsObject, context.getString(R.string.text)),
                                CommonFunctions.getFieldString(newsObject, context.getString(R.string.date)));

                        news.setTeacher(new Teachers(context).
                                findTeacherById(CommonFunctions.getFieldInt(newsObject, context.getString(R.string.teacher_id))));

                        newsArrayList.add(news);
                    }

                    callBack.onSuccess(newsArrayList);
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
