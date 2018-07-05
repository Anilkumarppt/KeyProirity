package com.example.admin.keyproirityapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ChatActivity2 extends AppCompatActivity {

    TextView usernameTitle,lastseen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
       /* Toolbar toolbar=findViewById(R.id.chatbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbar_view=layoutInflater.inflate(R.layout.chat_custom_actionbar,null);
        actionBar.setCustomView(actionbar_view);
        usernameTitle=findViewById(R.id.display_username);
        lastseen=findViewById(R.id.display_lastseen);
*/
    }

}
