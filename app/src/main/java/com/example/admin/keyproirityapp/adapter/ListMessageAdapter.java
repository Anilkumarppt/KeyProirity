package com.example.admin.keyproirityapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.keyproirityapp.BasicTest;
import com.example.admin.keyproirityapp.LastSeenStatus;
import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.Consersation;
import com.example.admin.keyproirityapp.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

public class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Consersation> messageList;
    private Context context;
    private Consersation consersation;
    private HashMap<String, Bitmap> bitmapAvata;
    private HashMap<String, DatabaseReference> bitmapAvataDB;
    private Bitmap bitmapAvataUser;

    public ListMessageAdapter(Context context, Consersation consersation, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
        this.context = context;
        this.consersation = consersation;
        this.bitmapAvata = bitmapAvata;
        this.bitmapAvataUser = bitmapAvataUser;
        bitmapAvataDB = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BasicTest.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == BasicTest.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        String contentType = consersation.getListMessageData().get(position).getContentType();
        final long time = consersation.getListMessageData().get(position).getTimestamp();
        LastSeenStatus getTime = new LastSeenStatus();
        long last_seen = time;
        //String lastSeendisplay = getTime.getTimeAgo(last_seen, context).toString();
        if (contentType.equals("text")) {
            if (holder instanceof ItemMessageFriendHolder) {
                String msg = consersation.getListMessageData().get(position).text;
                //  ((ItemMessageFriendHolder) holder).txtTimeFriend.setText(lastSeendisplay);
                ((ItemMessageFriendHolder) holder).txtContent.setText(msg + "," + position);
                Bitmap currentAvata = bitmapAvata.get(consersation.getListMessageData().get(position).idSender);
                if (currentAvata != null) {
                    ((ItemMessageFriendHolder) holder).avata.setImageBitmap(currentAvata);
                } else {
                    final String id = consersation.getListMessageData().get(position).idSender;
                    if (bitmapAvataDB.get(id) == null) {
                        bitmapAvataDB.put(id, FirebaseDatabase.getInstance().getReference().child("user/" + id + "/avata"));
                        bitmapAvataDB.get(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String avataStr = (String) dataSnapshot.getValue();
                                    if (!avataStr.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                                        byte[] decodedString = Base64.decode(avataStr, Base64.DEFAULT);
                                        BasicTest.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                    } else {
                                        BasicTest.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata));
                                    }
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            } else if (holder instanceof ItemMessageUserHolder) {
                Log.v("msg", consersation.getListMessageData().get(position).text);
                ((ItemMessageUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text + "," + position);
                //((ItemMessageUserHolder) holder).txtTimeuser.setText(lastSeendisplay);

                if (bitmapAvataUser != null) {
                    ((ItemMessageUserHolder) holder).avata.setImageBitmap(bitmapAvataUser);
                }
            }

        } else {
            /*image message*/
            final String msg = consersation.getListMessageData().get(position).text;
            final long timeImage = consersation.getListMessageData().get(position).getTimestamp();
            if (holder instanceof ItemMessageFriendHolder) {
                ((ItemMessageFriendHolder) holder).txtContent.setVisibility(View.INVISIBLE);
                ((ItemMessageFriendHolder) holder).txtContent.setPadding(0, 0, 0, 0);
                /*    ((ItemMessageFriendHolder) holder).txtTimeFriend.setGravity(Gravity.CENTER);
                 ((ItemMessageFriendHolder) holder).txtTimeFriend.setText(lastSeendisplay);
*/
                ((ItemMessageFriendHolder) holder).imageMessageFrnd.setVisibility(VISIBLE);
                ((ItemMessageFriendHolder) holder).txtContent.setVisibility(View.INVISIBLE);
                ((ItemMessageFriendHolder) holder).txtContent.setPadding(0, 0, 0, 0);
                ((ItemMessageFriendHolder) holder).imageMessageFrnd.setVisibility(View.VISIBLE);
  /*              ((ItemMessageFriendHolder) holder).txtTimeFriend.setText(lastSeendisplay);
  */

                /*DrawableRequestBuilder<String> thumbnailRequest = Glide
                        .with( context )
                        .load( msg );

                Glide.with(context)
                        .load(msg)
                        .thumbnail(thumbnailRequest)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ItemMessageFriendHolder) holder).imageMessageFrnd);
                */// ((ItemMessageFriendHolder) holder).imageMessageFrnd.setImageResource(R.drawable.default_avat
                StorageReference reference = FirebaseStorage.getInstance()
                        .getReference().child(msg);
                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(context).
                                    load(msg)
                                    .into(((ItemMessageFriendHolder) holder).imageMessageFrnd);

                        }
                    }
                });

                Bitmap currentAvata = bitmapAvata.get(consersation.getListMessageData().get(position).idSender);
                if (currentAvata != null) {
                    ((ItemMessageFriendHolder) holder).avata.setImageBitmap(currentAvata);
                } else {
                    final String id = consersation.getListMessageData().get(position).idSender;
                    if (bitmapAvataDB.get(id) == null) {
                        bitmapAvataDB.put(id, FirebaseDatabase.getInstance().getReference().child("user/" + id + "/avata"));
                        bitmapAvataDB.get(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String avataStr = (String) dataSnapshot.getValue();
                                    if (!avataStr.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                                        byte[] decodedString = Base64.decode(avataStr, Base64.DEFAULT);
                                        BasicTest.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                    } else {
                                        BasicTest.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata));
                                    }
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            } else if (holder instanceof ItemMessageUserHolder) {
                ((ItemMessageUserHolder) holder).txtContent.setVisibility(View.INVISIBLE);
                ((ItemMessageUserHolder) holder).txtContent.setPadding(0, 0, 0, 0);
                ((ItemMessageUserHolder) holder).imageMessage.setVisibility(VISIBLE);
                StorageReference reference = FirebaseStorage.getInstance()
                        .getReference().child(msg);
                Glide.with(context).
                        load(msg)
                        .into(((ItemMessageUserHolder) holder).imageMessage);
 /* DrawableRequestBuilder<String> thumbnailRequest = Glide
                        .with( context )
                        .load( msg );

                Glide.with(context)
                        .load(msg)
                        .thumbnail(thumbnailRequest)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ItemMessageUserHolder) holder).imageMessage);
*/
               /* Picasso.with(((ItemMessageUserHolder) holder).imageMessage.getContext()).load(consersation.getListMessageData().get(position).text)
                        .into(((ItemMessageUserHolder) holder).imageMessage);
                if (bitmapAvataUser != null) {
                    ((ItemMessageUserHolder) holder).avata.setImageBitmap(bitmapAvataUser);
                }*/
            }

        }

    }


    @Override
    public int getItemViewType(int position) {
        return consersation.getListMessageData().get(position).idSender.equals(StaticConfig.UID) ? BasicTest.VIEW_TYPE_USER_MESSAGE : BasicTest.VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return consersation.getListMessageData().size();
    }

    public void addAll(List<Message> newUsers) {
        int initialSize = consersation.getListMessageData().size();
        for (Message message : newUsers) {
            consersation.getListMessageData().add(message);
        }

        notifyItemRangeInserted(initialSize, newUsers.size());
    }

    public String getLastItemId() {
        String idRoom, senderId, reciverId;
        // senderId=consersation.getListMessageData().get()
        int size = consersation.getListMessageData().size();
        return String.valueOf(size - 1);
        //idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();

    }

}

class ItemMessageUserHolder extends RecyclerView.ViewHolder {
    public TextView txtContent, txtTimeuser;
    public CircleImageView avata;
    public ImageView imageMessage;


    public ItemMessageUserHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentUser);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView2);
        txtTimeuser = itemView.findViewById(R.id.timeuser);
        imageMessage = (ImageView) itemView.findViewById(R.id.imageMessageUser);
    }
}

class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
    public TextView txtContent, txtTimeFriend;
    public CircleImageView avata;
    public ImageView imageMessageFrnd;

    public ItemMessageFriendHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentFriend);
        txtTimeFriend = itemView.findViewById(R.id.timefriend);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView3);
        imageMessageFrnd = itemView.findViewById(R.id.imageMessage);
    }
}
