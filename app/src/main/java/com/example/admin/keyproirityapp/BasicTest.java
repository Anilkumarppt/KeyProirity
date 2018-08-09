package com.example.admin.keyproirityapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.keyproirityapp.adapter.ListMessageAdapter;
import com.example.admin.keyproirityapp.database.SharedPreferenceHelper;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.Consersation;
import com.example.admin.keyproirityapp.model.Message;
import com.example.admin.keyproirityapp.model.User;
import com.example.admin.keyproirityapp.util.EndlessRecyclerOnScrollListener;
import com.example.admin.keyproirityapp.util.ImageUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicTest extends AppCompatActivity {
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private static final int TOTAL_ITEMS = 10;
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;
    private static final int PAGE_START = 0;
    public CircleImageView groupIcon;
    public CircleImageView profilepic;
    TextView usernametitle;
    List<String > idlist=new ArrayList<>();
    int visibleItemCount;
    int currentPage=0;
    TextView userLastseen;
    DatabaseReference groupDB;
    String frindid, chat_Type, nameFriend;
    String toolbartype;
    String groupId;
    String baseAvata;
    private int mTotalItemCount = 0;
    private int mLastVisibleItemPosition;
    private boolean mIsLoading = false;
    private int mPostsPerPage = 10;

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private DatabaseReference messageDatabaseRef;
    private RecyclerView recyclerChat;
    private ListMessageAdapter adapter;
    private String roomId;
    private ArrayList<CharSequence> idFriend;
    private Consersation consersation;
    private EditText editWriteMessage;
    SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int PICK_IMAGE = 1;
    private StorageReference mStorage;
    private DatabaseReference rootRef;

    private static  int start=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intentData = getIntent();
        chat_Type = intentData.getStringExtra(StaticConfig.PERSONAL_CHAT);
        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
        frindid = intentData.getStringExtra("FriendId");
        groupId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        groupDB = FirebaseDatabase.getInstance().getReference().child("group" + "/" + groupId);

        progressDialog = new ProgressDialog(this);

        messageDatabaseRef = FirebaseDatabase.getInstance().getReference().child("message" + "/" + roomId);
        messageDatabaseRef.keepSynced(true);

        consersation = new Consersation();
        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
            bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            bitmapAvataUser = null;
        }

        editWriteMessage = (EditText) findViewById(R.id.input_message);
        if (idFriend != null && nameFriend != null) {
            if (idFriend.size() >= 2) {
                toolbartype = "group";
                groupActionBar(toolbar);
                setgorupImage();
            } else {
                toolbartype = "personal";
                actionbarType(toolbar);
                showLastSeen(idFriend, nameFriend);
            }
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerChat = (RecyclerView) findViewById(R.id.message_list_users);

           refreshLayout=findViewById(R.id.swipeRefreshLayoutchat);
           //refreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)BasicTest.this);
            recyclerChat.setLayoutManager(linearLayoutManager);
            adapter = new ListMessageAdapter(this, consersation, bitmapAvataFriend, bitmapAvataUser);
            recyclerChat.setAdapter(adapter);
            loadData();
        }
    }

    private void loadData() {
        Query query;
        query=messageDatabaseRef.limitToFirst(TOTAL_ITEMS)
                .startAt(start).orderByChild("timestamp");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChildren()){
                            currentPage--;
                        }
                        for(DataSnapshot dsp:dataSnapshot.getChildren()){
                            Message message=dsp.getValue(Message.class);
                            consersation.getListMessageData().add(message);
                            adapter.notifyDataSetChanged();
                        }
                        start+=(TOTAL_ITEMS+1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void loadMoreData(){
        currentPage++;
        Toast.makeText(this, "Page No"+String.valueOf(currentPage), Toast.LENGTH_SHORT).show();
        loadData();
    }
    private String getLastId(){
        String lastId=null;
        int size=idlist.size();
        lastId=idlist.get(size-1);

        Log.d("last Id",lastId);
        return lastId;

    }
    private void getMessages(String nodeId) {
        Query query;
        if (nodeId == null) {
            query = messageDatabaseRef.orderByKey().
                    limitToFirst(mPostsPerPage);
        } else
            query = messageDatabaseRef.orderByKey().
                    startAt(nodeId).
                    limitToFirst(mPostsPerPage);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message message;
                List<Message> messagesList=new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    message = dsp.getValue(Message.class);
                    idlist.add(dsp.getKey());
                    consersation.getListMessageData().add(message);
                    messagesList.add(dsp.getValue(Message.class));
                }

                linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                mIsLoading = false;
                adapter.addAll(messagesList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            mIsLoading=false;
            }
        });
        //adapter.notifyDataSetChanged();
    }

    private void loadMessages() {
        DatabaseReference reference = messageDatabaseRef;
        Query messagequery = reference.limitToLast(TOTAL_ITEMS).orderByKey();
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
                    Message newMessage = new Message(idsender, idreciver, message, timestamp, type);
                    consersation.getListMessageData().add(newMessage);
                    adapter.notifyDataSetChanged();
                    linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);

                    //
                    // swipeRefreshLayout.setRefreshing(false);
                    Log.d("adapter size", String.valueOf(consersation.getListMessageData().size()));

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
        recyclerChat.setAdapter(adapter);

    }

    private void groupActionBar(Toolbar toolbar) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbar_view = layoutInflater.inflate(R.layout.group_chat_toolbar, null);
        actionBar.setCustomView(actionbar_view);
        TextView groupName = findViewById(R.id.display_groupname);
        groupIcon = findViewById(R.id.display_groupicon);

        groupName.setText(nameFriend);
        ///setgorupImage();
    }

    private void actionbarType(Toolbar toolbar) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbar_view = layoutInflater.inflate(R.layout.chat_custom_actionbar, null);
        actionBar.setCustomView(actionbar_view);
        usernametitle = findViewById(R.id.display_username);
        userLastseen = findViewById(R.id.display_lastseen);
        profilepic = findViewById(R.id.display_profile);
    }

    private void setgorupImage() {
        groupDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    HashMap mapGroup = (HashMap) dataSnapshot.getValue();
                    ArrayList<String> member = (ArrayList<String>) mapGroup.get("member");
                    HashMap mapGroupInfo = (HashMap) mapGroup.get("groupInfo");
                    String name = (String) mapGroupInfo.get("name");
                    String avatar = (String) mapGroupInfo.get("groupIcon");
                    setImageAvatar(getApplicationContext(), avatar);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setImageAvatar(Context context, String imgBase64) {
        try {
            Resources res = getResources();
            Bitmap src;
            if (imgBase64.equals("default")) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            groupIcon.setImageDrawable(ImageUtils.roundedImage(context, src));
        } catch (Exception e) {
        }
    }

    private void showLastSeen(ArrayList<CharSequence> idFriend, final String nameFriend) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("user");
        usernametitle.setText(nameFriend);
        reference.child(frindid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String userStatus = String.valueOf(user.status.isOnline);
                String online = String.valueOf(user.status.timestamp);
                String avata = String.valueOf(user.avata);
                if (avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                    profilepic.setImageResource(R.drawable.default_avata);
                } else {
                    byte[] decodingString = Base64.decode(avata, Base64.DEFAULT);
                    Bitmap src = BitmapFactory.decodeByteArray(decodingString, 0, decodingString.length);
                    profilepic.setImageBitmap(src);
                }
                if (userStatus.equals("true")) {
                    userLastseen.setText("Online");
                } else {
                    LastSeenStatus getTime = new LastSeenStatus();
                    long last_seen = Long.parseLong(online);
                    String lastSeendisplay = getTime.getTimeAgo(last_seen, getApplicationContext()).toString();
                    userLastseen.setText(lastSeendisplay);
                    //  getSupportActionBar().setTitle(lastSeendisplay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent result = new Intent();
            result.putExtra("idFriend", idFriend.get(0));
            setResult(RESULT_OK, result);
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("idFriend", idFriend.get(0));
        setResult(RESULT_OK, result);
        this.finish();
    }

    public void sendMessage(View view) {
        String content = editWriteMessage.getText().toString().trim();
        if (content.length() > 0) {
            editWriteMessage.setText("");
            Message newMessage = new Message(StaticConfig.UID, roomId, content, System.currentTimeMillis(), "text");
            Log.d("room id",roomId);
            messageDatabaseRef.keepSynced(true);
            messageDatabaseRef.push().setValue(newMessage);
        }
    }


    public void sendImage(View view) {
        progressBar = new ProgressBar(this);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mStorage = FirebaseStorage.getInstance().getReference();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            progressDialog.setTitle("Sending Image");
            progressDialog.setMessage("Please Wait ,while your chat message is sending.....");
            progressDialog.show();
            Uri uri = data.getData();
            final String imageLocation = "Photos" + "/" + roomId;
            final String imageLocationId = imageLocation + "/" + uri.getLastPathSegment();
            final String uniqueId = UUID.randomUUID().toString();
            DatabaseReference userMessage_key = messageDatabaseRef.push();
            String message_pushId = userMessage_key.getKey();
            final StorageReference filePathMessage = mStorage.child("MessageImages").child(message_pushId + ".jpg");
            final String downloadURl = filePathMessage.getPath();
            filePathMessage.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        final String downLoadURL = task.getResult().getDownloadUrl().toString();

                        Message imageMessage = new Message(StaticConfig.UID, roomId, downLoadURL, System.currentTimeMillis(), "media", downLoadURL);

                        Map messageBodyDetails = new HashMap();
                        DatabaseReference userMessage_key = messageDatabaseRef.push();
                        String message_pushId = userMessage_key.getKey();

                        messageBodyDetails.put(message_pushId, imageMessage);
                        FirebaseDatabase.getInstance().getReference().child("message" + "/" + roomId).updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(BasicTest.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }

                                editWriteMessage.setText("");
                                progressDialog.dismiss();
                            }

                        });

                        Toast.makeText(BasicTest.this, "Picture sent successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(BasicTest.this, "Picture not Sent.Please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public void viewGroupInfo(View view) {
        FirebaseDatabase.getInstance().getReference("group").child(groupId + "groupinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    baseAvata = dsp.child("groupIcon").getValue(String.class);
                    Toast.makeText(BasicTest.this, baseAvata, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent groupInfo = new Intent(this, GroupInfo.class);
        groupInfo.putExtra("GroupId", groupId);
        groupInfo.putExtra("groupIcon", baseAvata);
        if (nameFriend != null) {
            groupInfo.putExtra("GroupName", nameFriend);
        } else {
            nameFriend = "default";
            groupInfo.putExtra("GroupName", nameFriend);
        }

        startActivity(groupInfo);
    }


}


