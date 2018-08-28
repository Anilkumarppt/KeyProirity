package com.example.admin.keyproirityapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.widgets.EmptyStateRecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatActivity extends Activity implements TextWatcher {
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    final String TAG = "GroupChatActivity";
    public Bitmap bitmapAvataUser;
    String friednId, roomId, groupname;
    EmptyStateRecyclerView messagesRecycler;
    TextView emptyView;
    Toolbar toolbar;
    boolean emptyInput;
    FloatingActionButton sendFab;
    ProgressBar progress;
    boolean isLoading;
    DatabaseReference groupDatabase;
    private ArrayList<CharSequence> idFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        sendFab = findViewById(R.id.activity_thread_send_fab);
        toolbar = findViewById(R.id.activity_thread_toolbar);
        progress = findViewById(R.id.activity_thread_progress);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            roomId = intent.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
            groupname = intent.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
            toolbar.setTitle(groupname);
            idFriend = intent.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
            Log.d(TAG, roomId);
            Log.d(TAG, idFriend.toString());
        }
        initializeMessagesRecycler();
    }

    private void initializeMessagesRecycler() {
        groupDatabase = FirebaseDatabase.getInstance().getReference().child("message" + "/" + roomId);
        groupDatabase.keepSynced(true);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        emptyInput = s.toString().trim().isEmpty();
        displayInputState();

    }

    private void displayInputState() {
        //inputEditText.setEnabled(!isLoading);
        sendFab.setEnabled(!emptyInput && !isLoading);
        //sendFab.setImageResource(isLoading ? R.color.colorTransparent : R.drawable.ic_send);

    }
}
