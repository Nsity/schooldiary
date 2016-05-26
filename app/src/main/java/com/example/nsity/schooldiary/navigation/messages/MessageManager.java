package com.example.nsity.schooldiary.navigation.messages;

import android.app.DownloadManager;
import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ErrorTracker;
import com.example.nsity.schooldiary.system.network.ResponseObject;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 27.04.16.
 */
public class MessageManager {

    public static void createMessage(final Context context, final Message message, final int teacherId, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_create_message) + Preferences.get(Preferences.PUPILID, context) + "/" + teacherId;

        RequestParams requestParams = new RequestParams();
        requestParams.add(context.getString(R.string.message_text), message.getMessage());
        requestParams.add(context.getString(R.string.message_date), message.getCreatedAt());

        new AsyncHttpResponse(context, url, requestParams, AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                JSONObject response = (JSONObject)object.getResponse();
                try {
                    response = response.getJSONObject(context.getString(R.string.result));


                    new MessageDBInterface(context).addMessage(response);

                    callBack.onSuccess(CommonFunctions.getFieldInt(response, context.getString(R.string.id)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(context.getString(R.string.error_response));
                }
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, String.valueOf(object.getResponse())));
            }
        });
    }


    public static void getMessages(final Context context, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_messages) + Preferences.get(Preferences.PUPILID, context);

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

                    JSONArray jsonArray = (JSONArray) result.get(context.getString(R.string.messages));
                    MessageDBInterface db = new MessageDBInterface(context);
                    db.save(jsonArray, true);

                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(context.getString(R.string.error_response));
                }
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, String.valueOf(object.getResponse())));
            }
        });
    }

/*
    public static void getLastMessages(final Context context, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_last_messages) + Preferences.get(Preferences.PUPILID, context);

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

                    JSONArray jsonArray = (JSONArray) result.get(context.getString(R.string.last_messages));
                    MessageDBInterface db = new MessageDBInterface(context);
                    db.save(jsonArray, true);

                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(context.getString(R.string.error_response));
                }
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, String.valueOf(object.getResponse())));
            }
        });
    }*/

    public static void getMessagesInConversation(final int teacherId, final Context context, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_get_chat_messages) + Preferences.get(Preferences.PUPILID, context) + "/"
                + String.valueOf(teacherId);

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

                    JSONArray jsonArray = (JSONArray) result.get(context.getString(R.string.messages));
                    MessageDBInterface db = new MessageDBInterface(context);
                    db.deleteConversation(teacherId);
                    db.save(jsonArray, false);

                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(context.getString(R.string.error_response));
                }
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, String.valueOf(object.getResponse())));
            }
        });
    }


    public static void readConversation(final int teacherId, final Context context, final CallBack callBack) {
        String url = context.getString(R.string.base_url) +
                context.getString(R.string.call_method_api_read_chat) + Preferences.get(Preferences.PUPILID, context) + "/"
                + String.valueOf(teacherId);

        new AsyncHttpResponse(context, url, null, AsyncHttpResponse.CALL_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>(){
            @Override
            public void onSuccess(ResponseObject object){
                if (!(object.getResponse() instanceof JSONObject)) {
                    callBack.onFail(context.getString(R.string.error_response));
                    return;
                }

                MessageDBInterface db = new MessageDBInterface(context);
                db.readConversation(teacherId);

                callBack.onSuccess();
            }

            @Override
            public void onFailure(ResponseObject object){
                callBack.onFail(ErrorTracker.getErrorDescription(context, String.valueOf(object.getResponse())));
            }
        });
    }

}
