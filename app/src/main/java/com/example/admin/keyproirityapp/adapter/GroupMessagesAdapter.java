package com.example.admin.keyproirityapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.Conversation;
import com.example.admin.keyproirityapp.model.GroupMessage;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Dell on 8/30/2018.
 */

public class GroupMessagesAdapter extends RecyclerView.Adapter<GroupMessagesAdapter.MessagesHolder> {
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    public List<GroupMessage> conversationList;
    private Context context;
    private Conversation conversation;
    private HashMap<String, Bitmap> bitmapAvata;
    private HashMap<String, DatabaseReference> bitmapAvataDB;
    private Bitmap bitmapAvataUser;

    public GroupMessagesAdapter(Context context, List<GroupMessage> conversationList, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
        this.conversationList = conversationList;
        this.context = context;

        this.bitmapAvata = bitmapAvata;
        this.bitmapAvataDB = bitmapAvataDB;
        this.bitmapAvataUser = bitmapAvataUser;
    }


    @NonNull
    @Override
    public MessagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new MessagesHolder(view);
        } else if (viewType == VIEW_TYPE_FRIEND_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new MessagesHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesHolder holder, int position) {
        String time = String.valueOf((conversationList.get(position).timestamp));
        String msg = conversationList.get(position).text;
        Log.d("messageBody", String.valueOf(time));
        holder.setMessage(msg, String.valueOf(time));
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return conversationList.get(position).idSender.equals(StaticConfig.UID) ? VIEW_TYPE_USER_MESSAGE : VIEW_TYPE_FRIEND_MESSAGE;
    }

    public class MessagesHolder extends RecyclerView.ViewHolder {

        TextView messageBody, messageTime;

        public MessagesHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.item_message_body_text_view);
            messageTime = itemView.findViewById(R.id.item_message_date_text_view);
        }

        public void setMessage(String msg, String time) {
            int viewType = GroupMessagesAdapter.this.getItemViewType(getLayoutPosition());
            messageBody.setText(msg);
            messageTime.setText(time);
        }
    }
}
