package com.example.admin.keyproirityapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.keyproirityapp.GroupInfo;
import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.adapter.GroupMessagesAdapter;
import com.example.admin.keyproirityapp.database.SharedPreferenceHelper;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.Conversation;
import com.example.admin.keyproirityapp.model.GroupMessage;
import com.example.admin.keyproirityapp.widgets.EmptyStateRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    List<GroupMessage> groupMessageList;
    LinearLayoutManager linearLayoutManager;
    GroupMessagesAdapter adapter;
    TextInputEditText editTextMessage;
    private ArrayList<CharSequence> idFriend;
    private Conversation conversation;
    private String baseAvata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        sendFab = findViewById(R.id.activity_thread_send_fab);
        toolbar = findViewById(R.id.activity_thread_toolbar);
        progress = findViewById(R.id.activity_thread_progress);
        editTextMessage = findViewById(R.id.activity_thread_input_edit_text);
        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        groupMessageList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            roomId = intent.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
            groupname = intent.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
            toolbar.setTitle(groupname);

            idFriend = intent.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        }
        //conversation=new Conversation();
        initializeMessagesRecycler();

    }

    public void sendGroupmessage(View view) {
        String msg = editTextMessage.getText().toString().trim();
        long timestamp = new Date().getTime();
        final long dayTimestamp = getDayTimestamp(timestamp);
        String senderName = SharedPreferenceHelper.getInstance(this).getUserInfo().name;
        GroupMessage groupMessage = new GroupMessage(StaticConfig.UID, roomId, msg, dayTimestamp, false, "text", senderName);
        DatabaseReference groupMessageref = FirebaseDatabase.getInstance().getReference();
        editTextMessage.setText("");
        groupMessageref.child("groupMessages").child(roomId).push().setValue(groupMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("deliveryStatus", "Message Delivered");
                editTextMessage.setText("");
            }

        });

    }

    private long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    private void initializeMessagesRecycler() {
        groupDatabase = FirebaseDatabase.getInstance().getReference().child("groupMessages" + "/" + roomId);
        groupDatabase.keepSynced(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        EmptyStateRecyclerView recyclerView = findViewById(R.id.groupmessage_recylcer);
        TextView emptyView = findViewById(R.id.activity_thread_empty_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(emptyView);
        adapter = new GroupMessagesAdapter(this, groupMessageList, bitmapAvataFriend, bitmapAvataUser);
        recyclerView.setAdapter(adapter);
        loadMessages();
    }

    private void loadMessages() {
        DatabaseReference reference = groupDatabase;
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                    String idsender = (String) mapMessage.get("idSender");
                    String idreciver = (String) mapMessage.get("idReceiver");
                    String message = (String) mapMessage.get("text");
                    Long timestamp = (long) mapMessage.get("timestamp");
                    String type = (String) mapMessage.get("contentType");
                    Boolean multimedia = (Boolean) mapMessage.get("multimedia");
                    String senderName = (String) mapMessage.get("senderName");
                    GroupMessage newMessage = new GroupMessage(idsender, idreciver, message, timestamp, multimedia, type, senderName);
                    groupMessageList.add(newMessage);
                    newMessage.setGroupMessageList(groupMessageList);
                    //conversation.getGroupMessageArrayList().add(newMessage);
                    adapter.notifyDataSetChanged();
                    linearLayoutManager.scrollToPosition(groupMessageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void groupInfo(View view) {
        FirebaseDatabase.getInstance().getReference("group").child(roomId + "groupinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    baseAvata = dsp.child("groupIcon").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent groupInfo = new Intent(this, GroupInfo.class);
        groupInfo.putExtra("GroupId", roomId);
        groupInfo.putExtra("groupIcon", baseAvata);
        if (groupname != null) {
            groupInfo.putExtra("GroupName", groupname);
        } else {
            groupname = "default";
            groupInfo.putExtra("GroupName", groupname);
        }

        startActivity(groupInfo);

    }
}
