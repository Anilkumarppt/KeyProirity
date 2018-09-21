package com.example.admin.keyproirityapp.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Dell on 8/27/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String refreshedToken;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("mylog", "Inside on token refresh");
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d("mylog", "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {


        ServiceUtils.updateDeviceToke(this, refreshedToken);
    }
}
