package com.example.nsity.schooldiary.system.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by nsity on 21.02.16.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMIntentService.class);
        startService(intent);
    }
}
