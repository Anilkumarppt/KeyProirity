package com.example.admin.keyproirityapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.adapter.AllusersAdapter;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.AllUsers;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity {

    Query query;
    RecyclerView recyclerView;
    Toolbar allusersToolbar;
    private DatabaseReference allUsersDatabaseref;
    AllusersAdapter myAdapter;
    List<AllUsers> listUsers;
    List<String> usersId;
    private ArrayList<String> listFriendID;
    LovelyProgressDialog dialogWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersId=new ArrayList<>();
        listFriendID=new ArrayList<>();
        setContentView(R.layout.activity_all_users_list);
        allusersToolbar=findViewById(R.id.allusers_appbar);
        setSupportActionBar(allusersToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Users");
        recyclerView=findViewById(R.id.allusers_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
         //loadData();
    }

    private void loadData() {
        dialogWait=new LovelyProgressDialog(this);
        dialogWait.setCancelable(false)
                .setIcon(R.drawable.ic_add_friend)
                .setTitle("Finding friend....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        allUsersDatabaseref = FirebaseDatabase.getInstance().getReference();
        listUsers = new ArrayList<>();
        myAdapter = new AllusersAdapter(AllUsersActivity.this, listUsers);
         DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("user");
        reference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listUsers.clear();
                                showData( dataSnapshot);
                                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        recyclerView.setAdapter(myAdapter);
 }

    private void showData(final DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            for(DataSnapshot dsp:dataSnapshot.getChildren()){
                AllUsers usersList=new AllUsers();
                String key= dsp.getKey().toString();
                usersList.setUid(key);
                usersId.add(key);
                usersList.setName(dsp.child("name").getValue(String.class));
                usersList.setMobile(dsp.child("mobile").getValue(String.class));
                usersList.setAvata(dsp.child("avata").getValue(String.class));
                usersList.setEmail(dsp.child("email").getValue(String .class));
                    listUsers.add(usersList);
            }
            //removeDuplicate();
//            getFriendsId();

            recyclerView.setAdapter(myAdapter);
        }
            dialogWait.dismiss();
            myAdapter.notifyDataSetChanged();
    }
    private void getFriendsId(){

        FirebaseDatabase.getInstance().getReference().child("friend"+"/"+ StaticConfig.UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for(DataSnapshot dsp:dataSnapshot.getChildren()){
                        String frndid=dsp.getValue(String.class);
                        System.out.print("frined id"+frndid);
                        listFriendID.add(frndid);

                    }
                    //                  getAllFriendInfo(0);
                } else {

//                    dialogFindAllFriend.dismiss();
                }
                compareToList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void compareToList(){
        if(usersId.size()>0 && listFriendID.size()>0){
            for(int i=0;i<=usersId.size();i++){
                for(int j=0;j<listFriendID.size();j++){
                    if(listFriendID.get(i).contains(usersId.get(i))){
                        Toast.makeText(this, "Already a friend", Toast.LENGTH_SHORT).show();
                    }
                    else {

                    }
                }
            }
        }
    }
    private void removeDuplicate() {
       /* AllUsers allUsersModel=new AllUsers();
        HashSet<String> hs=new HashSet<String >();
        for(int i=0;i<listUsers.size();i++){
            hs.add(listUsers.get(i).getName()+","+listUsers.get(i).getUid()+","+listUsers.get(i).getAvata());
        }
        listUsers.clear();
        String allUsers[]=null;
        for(String s: hs){
            allUsers=s.split(",");
            allUsersModel.setUsername(allUsers[0]);
            allUsersModel.setUid(allUsers[1]);
            allUsersModel.setUserprofilepic(allUsers[2]);
            Toast.makeText(this, allUsersModel.getUsername(), Toast.LENGTH_SHORT).show();
            listUsers.add(allUsersModel);
        }
*/    }


    @Override
    protected void onStop() {
        super.onStop();
         }
    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        }

    }
