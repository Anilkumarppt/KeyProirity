package com.example.admin.keyproirityapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.databinding.RowChatReceiverBinding;
import com.example.admin.keyproirityapp.databinding.RowChatSenderBinding;
import com.example.admin.keyproirityapp.model.ChatModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dell on 9/1/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = ChatAdapter.class.getSimpleName();
    private static final int SENDER = 1;
    private static final int RECEIVER = 2;
    private final LayoutInflater inflater;
    private final List<ChatModel> mChatList;

    public ChatAdapter(Context context, List<ChatModel> chatList) {
        inflater = LayoutInflater.from(context);
        this.mChatList = chatList;
    }

    public static String getTime(String milliseconds) {

        String dateFormat = "hh:mm:a";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliseconds));
        return formatter.format(calendar.getTime());
    }

    public static void loadImage(Context context, String imageUrl, ImageView imageView, TextView textView) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "loadImage: " + imageUrl);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    //.thumbnail(thumbnailRequest)
                    .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SENDER:
                View senderView = inflater.inflate(R.layout.row_chat_sender, parent, false);
                return new SenderHolder(senderView);
            case RECEIVER:
                View receiverView = inflater.inflate(R.layout.row_chat_receiver, parent, false);
                return new ReceiverHolder(receiverView);
            default:
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        int viewType = getItemViewType(position);
        ChatModel chatModel = getItemAtPosition(position);

        switch (viewType) {
            case SENDER:
                SenderHolder senderHolder = (SenderHolder) baseHolder;
                senderHolder.senderBinding.tvName.setText(chatModel.getUserName());
                senderHolder.senderBinding.tvTime.setText(getTime(chatModel.getTime()));
                senderHolder.senderBinding.tvMessage.setText(chatModel.getMessage());
                loadImage(inflater.getContext(), chatModel.getImageUrl(), senderHolder.senderBinding.ivImage, senderHolder.senderBinding.tvMessage);
                break;
            case RECEIVER:
                ReceiverHolder receiverHolder = (ReceiverHolder) baseHolder;
                receiverHolder.receiverBinding.tvName.setText(chatModel.getUserName());
                receiverHolder.receiverBinding.tvTime.setText(getTime(chatModel.getTime()));
                receiverHolder.receiverBinding.tvMessage.setText(chatModel.getMessage());
                loadImage(inflater.getContext(), chatModel.getImageUrl(), receiverHolder.receiverBinding.ivImage, receiverHolder.receiverBinding.tvMessage);
                break;
            default:
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItemAtPosition(position).isSender() ? SENDER : RECEIVER;
    }

    public ChatModel getItemAtPosition(int position) {
        return mChatList.get(position);


    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public static class SenderHolder extends RecyclerView.ViewHolder {
        RowChatSenderBinding senderBinding;

        public SenderHolder(View itemView) {
            super(itemView);
            senderBinding = DataBindingUtil.bind(itemView);
        }
    }

    public static class ReceiverHolder extends RecyclerView.ViewHolder {
        RowChatReceiverBinding receiverBinding;

        public ReceiverHolder(View itemView) {
            super(itemView);
            receiverBinding = DataBindingUtil.bind(itemView);
        }
    }
}


