package com.example.admin.keyproirityapp.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.admin.keyproirityapp.BasicTest;
import com.example.admin.keyproirityapp.NotificationActivity;
import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Dell on 8/23/2018.
 */

public class MessagingService extends FirebaseMessagingService {
    String from_senderId, idRoom;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        /*String roomId="0";
        String message="0";
        String from_senderId="0";
        String clickAction="0";
        if(remoteMessage.getData().size()>0){
            Log.d("Message Payload:",remoteMessage.getData().toString());
            roomId=remoteMessage.getData().get("roomId");

            message=remoteMessage.getData().get("body");
            from_senderId=remoteMessage.getData().get("from_senderId");
            clickAction=remoteMessage.getData().get("click_action");
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getClickAction(),from_senderId);
        }*/
        sendNotification("Title", "Body", "Action", "senderId");
    }

    private void sendNotification(String notificationTitle, String notificationBody, String clickAction, String from_senderId) {
        // Intent intent=new Intent(clickAction);

//        intent.putExtra("from_senderId",from_senderId);
        int requestId = (int) System.currentTimeMillis();
        String channelId = getString(R.string.default_notification_channel_id);
        /*intent.putExtra("body",notificationBody);
        intent.putExtra("from_senderId",from_senderId);
        intent.putExtra("message",notificationBody);
        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID,idRoom);
   */ //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.setAction(Long.toString(System.currentTimeMillis()));

        Intent testIntent = new Intent(this, NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Value", "Krishna");
        testIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, testIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSmallIcon(R.drawable.default_avata)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager mNotify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(notificationBody);
            mBuilder.setChannelId(channelId);

//            mNotify.createNotificationChannel(channel);
        }
        int mNotificationId = (int) System.currentTimeMillis();
        mNotify.notify(mNotificationId, mBuilder.build());
    }
}
