package com.example.admin.keyproirityapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.AllUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllusersAdapter extends RecyclerView.Adapter<AllusersAdapter.MyViewHolder> {
   private List<AllUsers> allUsersList;
    public List<String> allusersString;
    Context ctx;
    Activity activity;
    View view;
    Dialog dialog;

    public AllusersAdapter(List<String> allusersString) {
        this.allusersString = allusersString;
    }

    public AllusersAdapter(Activity context, List<AllUsers> allUsersList) {
        this.activity = context;
        this.allUsersList = allUsersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allusersitem, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_friend);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView username = dialog.findViewById(R.id.dialog_name);
                TextView email = dialog.findViewById(R.id.dialog_email);
                CircleImageView profileImg = dialog.findViewById(R.id.dialog_userImg);
                String name = allUsersList.get(myViewHolder.getAdapterPosition()).getName();
                final Button addFriend = dialog.findViewById(R.id.dialog__addfriend);

                username.setText(name);
                email.setText(allUsersList.get(myViewHolder.getAdapterPosition()).getEmail());
                if (allUsersList.get(myViewHolder.getAdapterPosition()).getAvata() != null) {
                    if (allUsersList.get(myViewHolder.getAdapterPosition()).getAvata().equals("default")) {
                        profileImg.setImageResource(R.drawable.default_avata);

                    } else {
                        byte[] decodedString = Base64.decode(allUsersList.get(myViewHolder.getAdapterPosition()).avata, Base64.DEFAULT);
                        Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profileImg.setImageBitmap(src);

                    }
                } else {
                    profileImg.setImageResource(R.drawable.default_avata);
                }

                addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String friendid = allUsersList.get(myViewHolder.getAdapterPosition()).getUid();
                        final String friendName = allUsersList.get(myViewHolder.getAdapterPosition()).getName();
                        if (friendid.equals(StaticConfig.UID)) {
                            Toast.makeText(view.getContext(), "this is your profile", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean alreadyFrnd = checkBeforeAdd(friendid);

                        }

                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });

        //dialog.setContentView(R.layout.);

        return myViewHolder;
    }

    private boolean checkBeforeAdd(String id) {
        final String friendId = id;
        FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int count = 0;
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        String key = dsp.getValue().toString();
                        if (key.equals(friendId)) {
                            count++;
                            break;
                        }
                    }
                    if (count >= 1) {
                        Toast.makeText(activity, "already a friend", Toast.LENGTH_SHORT).show();
                    } else {
                        addFriendToDB(friendId);
                    }
                } else {
                    addFriendToDB(friendId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return true;
    }

    private void addFriendToDB(String friendId) {
        FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).push().setValue(friendId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Sucessfully Added to FriendList", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "failed to add", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (allUsersList.size() > 0) {
            holder.username.setText(allUsersList.get(position).getName());
            if (allUsersList.get(position).getAvata() != null) {
                if (allUsersList.get(position).getAvata().equals(StaticConfig.STR_DEFAULT_BASE64)) {
                    holder.profileImage.setImageResource(R.drawable.default_avata);
                } else {
                    byte[] decodedString = Base64.decode(allUsersList.get(position).avata, Base64.DEFAULT);
                    Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    (holder).profileImage.setImageBitmap(src);

                }
            } else {
                holder.profileImage.setImageResource(R.drawable.default_avata);
            }
            if (allUsersList.get(position).status.isOnline==true) {
                holder.userStatus.setText("Online");
                holder.userStatus.setTextColor(Color.GREEN);
            } else {
                boolean status = allUsersList.get(position).status.isOnline;
                if (status == true) {
                    Toast.makeText(activity, String.valueOf(allUsersList.get(position).status.isOnline), Toast.LENGTH_SHORT).show();
                    holder.userStatus.setText("Online");
                    holder.userStatus.setTextColor(Color.GREEN);
                } else {
                    holder.userStatus.setText("Offline");
                    holder.userStatus.setTextColor(Color.RED);
                }
            }
        }


        //         holder.userStatus.setText("Hi!!!! I am Using Chat App");
        /* holder.setUserProfilePic(ctx,"userprofilepic");
         holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent=new Intent(ctx, BasicTest.class);
                chatIntent.putExtra("Visit_userid",allUsersList.get(holder.getAdapterPosition()).getUid());
                chatIntent.putExtra("user_name",allUsersList.get(holder.getAdapterPosition()).getName());
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(chatIntent);
            }
        });
        *///  Picasso.with(ctx).load("userprofilepic").placeholder(R.drawable.default_avata).into(holder.profileImage);

        //holder.profileImage.setImageResource(R.drawable.default_avata);
    }

    @Override
    public int getItemCount() {
        return allUsersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final CircleImageView profileImage;
        View mView;
        TextView username, userStatus;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            username = itemView.findViewById(R.id.txt_contactName);
            profileImage = itemView.findViewById(R.id.profile_pic);
            userStatus = itemView.findViewById(R.id.txt_contacStatus);
            layout = itemView.findViewById(R.id.layoutAudio);
        }
    }
}
