package com.example.nsity.schooldiary.system.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.network.AsyncHttpResponse;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.ResponseObject;
import com.example.nsity.schooldiary.system.network.Server;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by nsity on 21.02.16.
 */
public class GCMIntentService extends IntentService {


    public static final String API_KEY = "AIzaSyAM-A6ttQQmkqllswtVu5eHQc01QmVq9gs"; //api key
    public static final String SENDER_ID = "342244681055"; //project number
    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String REGISTER = "register";
    public static final String UNREGISTER = "unregister";

    private static final String TAG = "RegIntentService";

    public GCMIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(KEY);
        switch (key) {
            case REGISTER:
                registerGCM();
                break;
            case UNREGISTER:
                unregisterGCM();
                break;
        }
    }

    private void registerGCM() {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "GCM Registration Token: " + token);

            if(token != null)
                sendRegistrationToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unregisterGCM() {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            instanceID.deleteToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            ServiceRegister.unregister(this, Preferences.get(Preferences.GCM_TOKEN, this));
          //  Preferences.set(Preferences.GCM_TOKEN, null, this);
            Preferences.clear(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String token) {
        Preferences.set(Preferences.GCM_TOKEN, token, this);
        ServiceRegister.register(this, token);
    }

    private void RegistrationToServer(String token) {
        Preferences.set(Preferences.GCM_TOKEN, null, this);
        ServiceRegister.unregister(this, token);
    }

    private void subscribeToTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed to topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    private void unsubscribeFromTopic() {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.unsubscribe(token, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
