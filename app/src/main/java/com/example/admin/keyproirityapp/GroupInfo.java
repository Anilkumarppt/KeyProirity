package com.example.admin.keyproirityapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.keyproirityapp.adapter.GroupMembersAdapter;
import com.example.admin.keyproirityapp.database.FriendDB;
import com.example.admin.keyproirityapp.database.GroupDB;
import com.example.admin.keyproirityapp.model.Group;
import com.example.admin.keyproirityapp.model.ListFriend;
import com.example.admin.keyproirityapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupInfo extends AppCompatActivity {
    CircleImageView groupIcon;
    TextView txtGroupname;
    RecyclerView membersList;
    private ListFriend dataListFriend = null;
    String mGroupId,mGroupName;
    private ListFriend listFriend;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference groupReference;
    private ArrayList<String> listFriendID=null;
    private ArrayList<Group> listGroup;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GroupMembersAdapter membersAdapter;
    List<User> list=new ArrayList<>();
    TextView groupName;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        Intent intent=getIntent();
        listFriend = FriendDB.getInstance(this).getListFriend();
        toolbar=findViewById(R.id.groupinfo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GroupInfo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        membersAdapter=new GroupMembersAdapter(list,GroupInfo.this);
        groupName=findViewById(R.id.txt_groupname);
        mGroupId=intent.getStringExtra("GroupId");
        mGroupName=intent.getStringExtra("GroupName");
        groupName.setText(mGroupName);

        listGroup = GroupDB.getInstance(getApplicationContext()).getListGroups();
        int size=GroupDB.getInstance(getApplicationContext()).getListGroups().size();
        groupIcon=findViewById(R.id.groupicon);
        txtGroupname=findViewById(R.id.txt_groupname);
        membersList=findViewById(R.id.members_recycler_view);
        membersList.setHasFixedSize(true);
        membersList.setLayoutManager(new LinearLayoutManager(this));
        membersList.setAdapter(membersAdapter);
        membersAdapter.notifyDataSetChanged();
        if(listFriendID==null){
            listFriendID=new ArrayList<>();
            fetchmembersList();
        }
        ///getListGroup();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fetchmembersList() {
            firebaseDatabase=FirebaseDatabase.getInstance();
            groupReference=firebaseDatabase.getReference().child("group");
            groupReference.keepSynced(true);
            groupReference.child(mGroupId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                            ArrayList<String> member = (ArrayList<String>) mapRecord.get("member");
                            Iterator listKey = member.iterator();
                            while (listKey.hasNext()){
                                listFriendID.add(listKey.next().toString());
                            }
                            getAllFriendInfo(0);

                        }

                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
    private void getAllFriendInfo(final int i) {
        if(i==listFriendID.size()){
                membersAdapter.notifyDataSetChanged();

            }
            else{
                final String id=listFriendID.get(i);
                FirebaseDatabase.getInstance().getReference().child("user"+"/"+id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null){
                            User user=new User();
                            HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();
                            user.name=(String )mapUserInfo.get("name");
                            user.avata=(String )mapUserInfo.get("avata");
                            user.email=(String)mapUserInfo.get("email");
                            list.add(user);
                        }
                        getAllFriendInfo(i+1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        }


    }


}
