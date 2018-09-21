package com.example.admin.keyproirityapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.FriendDB;
import com.example.admin.keyproirityapp.database.SharedPreferenceHelper;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.databinding.ActivityAddGroupBinding;
import com.example.admin.keyproirityapp.model.Friend;
import com.example.admin.keyproirityapp.model.GroupModel;
import com.example.admin.keyproirityapp.model.RoomModel;
import com.example.admin.keyproirityapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getSimpleName();
    public DatabaseReference groupDB;
    DatabaseReference notificationReference;
    private CreateGroupAdapter adapter;
    private GroupModel groupModel;
    private LovelyProgressDialog dialogWait;
    private boolean isEditGroup;
    private ActivityAddGroupBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_group);
        groupModel = new GroupModel();
        groupModel.setListFriend(FriendDB.getInstance(this).getListFriend());
        User userInfo = SharedPreferenceHelper.getInstance(getApplicationContext()).getUserInfo();
        groupModel.setAdmin(userInfo);

        Intent intentData = getIntent();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        groupDB = FirebaseDatabase.getInstance().getReference("group");

        dialogWait = new LovelyProgressDialog(this).setCancelable(false);
        mBinding.editGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    mBinding.iconGroup.setText((charSequence.charAt(0) + "").toUpperCase());
                } else {
                    mBinding.iconGroup.setText("R");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelectedFriendList().size() < 1) {
                    Toast.makeText(CreateGroupActivity.this, "Add at lease two people to create group", Toast.LENGTH_SHORT).show();
                } else {
                    if (mBinding.editGroupName.getText().length() == 0) {
                        Toast.makeText(CreateGroupActivity.this, "Enter group name", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isEditGroup) {
                            //            editGroup();
                        } else {
                            createGroup();
                        }
                    }
                }
            }
        });

        /*if (intentData.getStringExtra("groupId") != null) {
            isEditGroup = true;
            String idGroup = intentData.getStringExtra("groupId");
            mBinding.txtActionName.setText("Save");
            mBinding.btnAddGroup.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            groupModel = GroupDB.getInstance(this).getGroup(idGroup);
            mBinding.editGroupName.setText(groupEdit.groupInfo.get("name"));
        } else {
            isEditGroup = false;
        }*/

        mBinding.recycleListFriend.setLayoutManager(linearLayoutManager);
        adapter = new CreateGroupAdapter(this, groupModel);
        mBinding.recycleListFriend.setAdapter(adapter);
    }

  /*  private void editGroup() {
        //Show dialog wait
        dialogWait.setIcon(R.drawable.ic_add_group_dialog)
                .setTitle("Editing....")
                .setTopColorRes(R.color.colorPrimary)
                .show();
        //Delete group
        final String idGroup = groupModel.getId();
        Room room = new Room();
        for (String id : listIDChoose) {
            room.member.add(id);
        }
        //listIdFriend.addAll(listIDChoose);
        Log.d("Room members", room.member.toString());
        room.groupInfo.put("name", mBinding.editGroupName.getText().toString());
        room.groupInfo.put("admin", StaticConfig.UID);
        room.groupInfo.put("groupIcon", StaticConfig.STR_DEFAULT_BASE64);
        //getDeviceTokes(0);
        FirebaseDatabase.getInstance().getReference().child("group/" + idGroup).setValue(room)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        addRoomForUser(idGroup, 0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialogWait.dismiss();
                        new LovelyInfoDialog(CreateGroupActivity.this) {
                            @Override
                            public LovelyInfoDialog setConfirmButtonText(String text) {
                                findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dismiss();
                                    }
                                });
                                return super.setConfirmButtonText(text);
                            }
                        }
                                .setTopColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.ic_add_group_dialog)
                                .setTitle("False")
                                .setMessage("Cannot connect database")
                                .setCancelable(false)
                                .setConfirmButtonText("Ok")
                                .show();
                    }
                })
        ;
    }
*/

    private void createGroup() {
        //Show dialog wait
        dialogWait.setIcon(R.drawable.ic_add_group_dialog)
                .setTitle("Registering....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        final String idGroup = (StaticConfig.UID + System.currentTimeMillis()).hashCode() + "";
        groupModel.setId(idGroup);
        SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(getApplicationContext());
        helper.setGroupId(idGroup);

        /*  Group Information object */
        final RoomModel roomModel = new RoomModel();
        RoomModel.GroupInfo groupInfo = new RoomModel.GroupInfo();
        groupInfo.id = idGroup;
        groupInfo.name = mBinding.editGroupName.getText().toString();
        groupInfo.avtar = StaticConfig.STR_DEFAULT_BASE64;
        roomModel.groupInfo = groupInfo;

        List<RoomModel.GroupMember> groupMembers = new ArrayList<>();
        RoomModel.GroupMember groupCreateMember = new RoomModel.GroupMember();
        groupCreateMember.isAdmin = true;
        groupCreateMember.token = helper.getUserInfo().deviceToken;
        groupCreateMember.id = helper.getUID();
        groupMembers.add(groupCreateMember);

        List<Friend> groupFriends = adapter.getSelectedFriendList();
        for (Friend groupFriend : groupFriends) {
            RoomModel.GroupMember groupMember = new RoomModel.GroupMember();
            groupMember.id = groupFriend.id;
            groupMember.token = groupFriend.deviceToken;
            groupMember.isAdmin = false;
            groupMembers.add(groupMember);
        }
        roomModel.groupMembers = groupMembers;
        FirebaseDatabase.getInstance().getReference().child("userGroup/" + idGroup).setValue(roomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addGroupToUsers(roomModel);
                dialogWait.dismiss();
            }
        });

    }

    public void addGroupToUsers(RoomModel roomModel) {
        List<RoomModel.GroupMember> groupMembers = roomModel.groupMembers;
        for (RoomModel.GroupMember friend : groupMembers) {
            FirebaseDatabase.getInstance().getReference().child("user/" + friend.id + "/group/" + roomModel.groupInfo.id)
                    .setValue(roomModel.groupInfo.id);
        }
        /*addMessage("Sample Test").addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("Task",task.getResult());
                }

            }
        });*/
    }

    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        String userIds = "[\"cf4B3RL6n44:APA91bELzjEncAM3cAgZtg8niicW3iMVhXXICKr-wKzLOuxwkpuEjW_AQ7kHZM_obwtVSXJ9G8kHSePrbcEBaUB2vbtXVGa4kJdqpKckCZByTYaUNnFUyRlrQAWGXH_36L6OkMo9yAoy\",\n" +
                "   \"fPS7wyMkgeU:APA91bFlkaSaLiw7m5S3K_ekoJmnEp6c9ezDmcjJLbdPdQDzA7BV\"]";
        data.put("text", text);
        data.put("groupIds", userIds);

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("addMessage")
                .call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {

                    }
                })
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.d(TAG, "then: addMessage Response");
                        String result = task.getResult().getData().toString();
                        Log.d("Result", result);
                        return result;
                    }
                });
    }


    /**
     private void deleteRoomForUser(final String roomId, final int userIndex) {
     if (userIndex == listIDRemove.size()) {
     dialogWait.dismiss();
     Toast.makeText(this, "Edit group success", Toast.LENGTH_SHORT).show();
     setResult(RESULT_OK, null);
     CreateGroupActivity.this.finish();
     } else {
     FirebaseDatabase.getInstance().getReference().child("user/" + listIDRemove.toArray()[userIndex] + "/group/" + roomId).removeValue()
     .addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override public void onComplete(@NonNull Task<Void> task) {
    deleteRoomForUser(roomId, userIndex + 1);
    }
    })
     .addOnFailureListener(new OnFailureListener() {
    @Override public void onFailure(@NonNull Exception e) {
    dialogWait.dismiss();
    new LovelyInfoDialog(CreateGroupActivity.this) {
    @Override public LovelyInfoDialog setConfirmButtonText(String text) {
    findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View view) {
    dismiss();
    }
    });
    return super.setConfirmButtonText(text);
    }
    }
    .setTopColorRes(R.color.colorAccent)
    .setIcon(R.drawable.ic_add_group_dialog)
    .setTitle("False")
    .setMessage("Cannot connect database")
    .setCancelable(false)
    .setConfirmButtonText("Ok")
    .show();
    }
    });
     }
     }
     **/

    /**
     public void sendNotification(final String groupId) {
     notificationReference = FirebaseDatabase.getInstance().getReference("Notifications" + "/" + "GroupNotifications/").child(groupId).push();
     Map<String, Object> notificationMessage = new HashMap<>();
     notificationMessage.put("message", "New Group Created");
     notificationMessage.put("from", StaticConfig.UID);
     notificationMessage.put("roomId", groupId);
     notificationMessage.put("groupname", groupname);
     notificationReference.setValue(notificationMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override public void onSuccess(Void aVoid) {
    Toast.makeText(getApplicationContext(), "NotificationSent", Toast.LENGTH_SHORT).show();
    }
    });

     }

     private void addRoomForUser(final String roomId, final int userIndex) {
     if (userIndex == listIDChoose.size()) {
     if (!isEditGroup) {
     sendNotification(roomId);
     dialogWait.dismiss();
     Toast.makeText(this, "Create group success", Toast.LENGTH_SHORT).show();
     setResult(RESULT_OK, null);
     CreateGroupActivity.this.finish();
     } else {
     deleteRoomForUser(roomId, 0);
     }
     } else {
     final Group group = new Group();

     String memberId = String.valueOf(listIDChoose.toArray()[userIndex]);
     Log.d(TAG, "member id" + memberId);
     FirebaseDatabase.getInstance().getReference().child("user/" + memberId)
     .addListenerForSingleValueEvent(new ValueEventListener() {
    @Override public void onDataChange(DataSnapshot dataSnapshot) {
    if (dataSnapshot.getValue() != null && dataSnapshot.hasChild("deviceToken")) {
    Room room = new Room();
    String groupId = SharedPreferenceHelper.getInstance(getApplicationContext()).getGroupId();
    String deviceToken = dataSnapshot.child("deviceToken").getValue(String.class);
    String name = dataSnapshot.child("name").getValue(String.class);
    group.memberTokens.add(deviceToken);
    FirebaseDatabase.getInstance().getReference("group" + "/" + groupId).child("membersTokens")
    .setValue(group.memberTokens);

    tokenMap.put("deviceToken", deviceToken);
    room.memberTokens.add(deviceToken);
    Log.d(TAG, "Device Tokens Map" + tokenMap.toString());
    }
    }

    @Override public void onCancelled(DatabaseError databaseError) {

    }
    });

     FirebaseDatabase.getInstance().getReference().child("user/" + listIDChoose.toArray()[userIndex] + "/group/" + roomId).setValue(roomId).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override public void onComplete(@NonNull Task<Void> task) {
    //getDeviceTokes(memberId);
    addRoomForUser(roomId, userIndex + 1);


    }
    }).addOnFailureListener(new OnFailureListener() {
    @Override public void onFailure(@NonNull Exception e) {
    dialogWait.dismiss();
    new LovelyInfoDialog(CreateGroupActivity.this) {
    @Override public LovelyInfoDialog setConfirmButtonText(String text) {
    findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View view) {
    dismiss();
    }
    });
    return super.setConfirmButtonText(text);
    }
    }
    .setTopColorRes(R.color.colorAccent)
    .setIcon(R.drawable.ic_add_group_dialog)
    .setTitle("False")
    .setMessage("Create group false")
    .setCancelable(false)
    .setConfirmButtonText("Ok")
    .show();
    }
    });
     }
     }
     * @param roomModel
     */

}

