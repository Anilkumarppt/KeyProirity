package com.example.admin.keyproirityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.keyproirityapp.database.StaticConfig;

public class NotificationActivity extends AppCompatActivity {

    TextView notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
       /* Intent intent=this.getIntent();
        String roomid=intent.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        notify=findViewById(R.id.textView);
        notify.setText(roomid+" ," +
                ""+intent.getStringExtra("message"));*/

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Toast.makeText(this, extras.getString("Value"), Toast.LENGTH_SHORT).show();
        }
    }
}
