package com.example.admin.keyproirityapp.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.admin.keyproirityapp.BasicTest;
import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    public static final String FCM_PARAM = "roomId";
    public static final String FCM_SENDER_NAME = "sender_name";
    public static final String FCM_SENDER_IMAGE = "sender_image";
    public static final String FCM_MESSAGE = "body";
    public static final String FCM_SENDER_ID = "from_senderId";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Log.d("FROM_sender ", remoteMessage.getFrom());
        sendNotification(data);
    }

    private void sendNotification(Map<String, String> data) {
        int requestId = (int) System.currentTimeMillis();
        String channelId = getString(R.string.default_notification_channel_id);
        Bundle bundle = new Bundle();
        bundle.putString(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, data.get(FCM_PARAM));//roomid
        bundle.putString(StaticConfig.INTENT_KEY_CHAT_FRIEND, data.get(FCM_SENDER_NAME));//sender name
        bundle.putString(StaticConfig.INTENT_KEY_CHAT_IMAAGE, data.get(FCM_SENDER_IMAGE));//senderImage
        bundle.putString(StaticConfig.INTENT_KEY_CHAT_ID, data.get(FCM_SENDER_ID));//senderID
        Intent intent = new Intent(this, BasicTest.class);
        intent.putExtras(bundle);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(data.get(FCM_SENDER_NAME))
                .setContentText(data.get(FCM_MESSAGE))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_add_friend);

       /* try {
            String picture = data.get(FCM_PARAM);
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText("c")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.default_notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }
}