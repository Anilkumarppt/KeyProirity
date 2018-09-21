package com.example.admin.keyproirityapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.keyproirityapp.adapter.ChatAdapter;
import com.example.admin.keyproirityapp.database.SharedPreferenceHelper;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.ChatModel;
import com.example.admin.keyproirityapp.model.Conversation;
import com.example.admin.keyproirityapp.model.Message;
import com.example.admin.keyproirityapp.model.User;
import com.example.admin.keyproirityapp.service.MessagingService;
import com.example.admin.keyproirityapp.util.ImageUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicTest extends AppCompatActivity {
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private static final int TOTAL_ITEMS = 10;
    private static final int PAGE_START = 0;
    private static final String TAG = "BasicTesst";
    public static HashMap<String, String> bitmapAvataFriend = new HashMap<>();
    private static int start = 0;
    private final List<ChatModel> mChatList = new ArrayList<>();
    public String bitmapAvataUser;
    public CircleImageView groupIcon;
    ImageView profilepic;
    TextView usernametitle, userLastseen;
    List<String> idlist = new ArrayList<>();
    DatabaseReference groupDB;
    String frindid, chat_Type, nameFriend;
    String toolbartype;
    String groupId;
    String baseAvata;
    DatabaseReference notificationReference;
    SwipeRefreshLayout refreshLayout;
    private int mTotalItemCount = 0;
    private int mLastVisibleItemPosition;
    private boolean mIsLoading = false;
    private int mPostsPerPage = 10;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private DatabaseReference messageDatabaseRef;
    private RecyclerView recyclerChat;
    //    private ListMessageAdapter adapter;
    private String roomId;
    private ArrayList<CharSequence> idFriend;
    private Conversation conversation;
    private EditText editWriteMessage;
    private LinearLayoutManager linearLayoutManager;
    private int PICK_IMAGE = 1;
    private StorageReference mStorage;
    private DatabaseReference rootRef;
    private ChatAdapter mChatAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setVisibility(View.GONE);
        } else {
            Log.d("imageUrl", "loadImage: " + imageUrl);
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_email)
//                .thumbnail(thumbnailRequest)
                    .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intentData = getIntent();
        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
        frindid = intentData.getStringExtra(MessagingService.FCM_SENDER_ID);
        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            roomId = bundle.getString(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
            nameFriend = bundle.getString(StaticConfig.INTENT_KEY_CHAT_FRIEND);
            frindid = bundle.getString(StaticConfig.INTENT_KEY_CHAT_ID);
            actionbarType(toolbar);
            showLastSeen(frindid, nameFriend);
            //  Log.d("Friend Id",idFriend.toString());
            /*actionbarType(toolbar);
            showLastSeen(frindid, nameFriend);*/

            //showLastSeen(idFriend,nameFriend);

        } else {
            roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
            nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
            idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        }
        //idFriend.add(frindid);

        /*groupId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        groupDB = FirebaseDatabase.getInstance().getReference().child("group" + "/" + groupId);
        */
        progressDialog = new ProgressDialog(this);
        messageDatabaseRef = FirebaseDatabase.getInstance().getReference().child("message" + "/" + roomId);
        messageDatabaseRef.keepSynced(true);
        conversation = new Conversation();
        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        bitmapAvataUser = base64AvataUser;
        editWriteMessage = (EditText) findViewById(R.id.input_message);
        if (idFriend != null && nameFriend != null) {
            if (idFriend.size() >= 2) {
                groupActionBar(toolbar);
                //setgorupImage();
            } else {
                actionbarType(toolbar);
                showLastSeen(frindid, nameFriend);
            }
        }
        initRecyclerview();
    }

    private void initRecyclerview() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerChat = (RecyclerView) findViewById(R.id.message_list_users);
        //refreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)BasicTest.this);
        recyclerChat.setLayoutManager(linearLayoutManager);
//        adapter = new ListMessageAdapter(this, conversation, bitmapAvataFriend, bitmapAvataUser);
//        recyclerChat.setAdapter(adapter);
        //loadMessages();

        mChatAdapter = new ChatAdapter(getApplicationContext(), mChatList);
        recyclerChat.setAdapter(mChatAdapter);
        getChatMessages();
    }

    public void getChatMessages() {
        messageDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotList) {
                mChatList.clear();
                for (DataSnapshot dataSnapshot : dataSnapshotList.getChildren()) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        String idsender = (String) mapMessage.get("idSender");
                        String idreciver = (String) mapMessage.get("idReceiver");
                        String text = (String) mapMessage.get("text");
                        Long timestamp = (long) mapMessage.get("timestamp");
                        String type = (String) mapMessage.get("contentType");
                        String contentLocation = (String) mapMessage.get("contentLocation");
                        String imageUrl = type.equals("text") ? "" : contentLocation;
                        String message = type.equals("text") ? text : "";
                        String currentUserId = SharedPreferenceHelper.getInstance(getApplicationContext()).getUID();
                        boolean isSender = currentUserId.equals(idsender);
                        String name = (String) mapMessage.get("name");
                        ChatModel chatModel = new ChatModel(String.valueOf(timestamp), message, imageUrl,
                                null, null, isSender, name);
                        mChatList.add(chatModel);
                    }
                }
                mChatAdapter.notifyDataSetChanged();
                recyclerChat.scrollToPosition(mChatAdapter.getItemCount() - 1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void sendMessage(View view) {
        String content = editWriteMessage.getText().toString().trim();
        if (content.length() > 0) {
            notificationReference = FirebaseDatabase.getInstance().getReference("Notifications").child(frindid).push();
            Map<String, Object> notificationMessage = new HashMap<>();
            notificationMessage.put("message", content);
            notificationMessage.put("from", StaticConfig.UID);
            notificationMessage.put("roomId", roomId);
            notificationReference.setValue(notificationMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(BasicTest.this, "NotificationSent", Toast.LENGTH_SHORT).show();
                }
            });
            editWriteMessage.setText("");
            String username = SharedPreferenceHelper.getInstance(getApplicationContext()).getUserInfo().name;
            Message newMessage = new Message(StaticConfig.UID, roomId, content, System.currentTimeMillis(), "text", username, "");

            Log.d("room id", roomId);
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

    private void actionbarType(Toolbar toolbar) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View actionbar_view;
            actionbar_view = layoutInflater != null ? layoutInflater.inflate(R.layout.chat_custom_actionbar, null) : null;
            actionBar.setCustomView(actionbar_view);
            profilepic = actionbar_view != null ? (ImageView) actionbar_view.findViewById(R.id.display_profile_picture) : null;
            usernametitle = actionbar_view.findViewById(R.id.display_username);
            userLastseen = actionbar_view.findViewById(R.id.display_lastseen);
            profilepic.setVisibility(actionbar_view.VISIBLE);
            profilepic.setImageResource(R.drawable.default_avata);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        //   mAuth.addAuthStateListener(mAuthListener);

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

    private void showLastSeen(String idFriend, final String friendName) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("user");
        if (friendName != null) {
            setTitle(friendName);
        } else {
            setTitle(nameFriend);
        }
        reference.child(idFriend).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String userStatus = String.valueOf(user.status.isOnline);
                String online = String.valueOf(user.status.timestamp);
                String avata = String.valueOf(user.avata);
                if (avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                    //       profilepic.setImageResource(R.drawable.default_avata);
                } else {
                    //    loadImage(getApplicationContext(),avata,profilepic);
                   /* byte[] decodingString = Base64.decode(avata, Base64.DEFAULT);
                    Bitmap src = BitmapFactory.decodeByteArray(decodingString, 0, decodingString.length);
                    Log.d("Bitmap Src", String.valueOf(src));
                    profilepic.setImageBitmap(src);
                */
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
        if (idFriend != null && idFriend.size() > 0) {
            Intent result = new Intent();
            result.putExtra("idFriend", idFriend.get(0));
            setResult(RESULT_OK, result);
        }
        finish();
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
            DatabaseReference userMessage_key = messageDatabaseRef.push();
            String message_pushId = userMessage_key.getKey();
            final StorageReference filePathMessage = mStorage.child("MessageImages").child(message_pushId + ".jpg");
            if (uri != null) {
                filePathMessage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePathMessage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "download Url" + uri);
                                final String downLoadUrl = uri.toString();
                                Message multiMediaMessage = new Message(StaticConfig.UID, roomId, System.currentTimeMillis(), "Image", downLoadUrl, "Image");
                                HashMap messageBodyDetails = new HashMap();
                                DatabaseReference userMessage_key = messageDatabaseRef.push();
                                String message_pushId = userMessage_key.getKey();
                                messageBodyDetails.put(message_pushId, multiMediaMessage);
                                FirebaseDatabase.getInstance().getReference().child("message" + "/" + roomId).updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(BasicTest.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        editWriteMessage.setText("");
                                        progressDialog.dismiss();
                                        Toast.makeText(BasicTest.this, "Picture sent successfully", Toast.LENGTH_SHORT).show();
                                    }

                                });

                            }
                        });

                    }
                });
            }
        }
    }

    public void viewGroupInfo(View view) {
        FirebaseDatabase.getInstance().getReference("group").child(groupId + "groupinfo").addValueEventListener(new ValueEventListener() {
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


