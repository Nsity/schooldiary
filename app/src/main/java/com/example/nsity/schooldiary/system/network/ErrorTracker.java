package com.example.nsity.schooldiary.system.network;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 15.11.15.
 */
public class ErrorTracker {
    private static final String EXCEPTION_TITLE = "Exception";
    private static final String EXCEPTION_MESSAGE = "Message";
    private static final String EXCEPTION_STACK = "Stack";

    public static String getErrorDescription(Context context, String response) {

        if (CommonFunctions.StringIsNullOrEmpty(response)) {
            return context.getString(R.string.error_tracker_empty_content);
        }

        if (response.equalsIgnoreCase("{}")) {
            return context.getString(R.string.error_tracker_empty_json);
        }

        JSONObject jsonMessage;
        try {
            jsonMessage = new JSONObject(response);
            jsonMessage = jsonMessage.getJSONObject(EXCEPTION_TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
            return context.getString(R.string.error_tracker_exception_not_found);
        }
        String message = CommonFunctions.getFieldString(jsonMessage, EXCEPTION_MESSAGE);

        if (message.equals(AsyncHttpResponse.MESSAGE)){
            return context.getString(R.string.error_tracker_timeout_error);
        }

        return (CommonFunctions.StringIsNullOrEmpty(message)) ? context.getString(R.string.error_response) : message;
    }
}
