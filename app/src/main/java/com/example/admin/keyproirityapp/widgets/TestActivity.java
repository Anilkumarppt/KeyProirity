package com.example.admin.keyproirityapp.widgets;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.admin.keyproirityapp.NotificationActivity;
import com.example.admin.keyproirityapp.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification("Title", "Body", "Action", "senderId");
            }
        });
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
