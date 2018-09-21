package com.example.admin.keyproirityapp.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.databinding.RcItemAddFriendBinding;
import com.example.admin.keyproirityapp.model.Friend;
import com.example.admin.keyproirityapp.model.GroupModel;
import com.example.admin.keyproirityapp.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.ItemHolder> {

    private Context context;
    private GroupModel mGroupModel;

    public CreateGroupAdapter(Context context, GroupModel groupModel) {
        this.context = context;
        this.mGroupModel = groupModel;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_add_friend, parent, false);
        return new ItemHolder(view);
    }

    private Friend getFriendAt(int position) {
        return mGroupModel.getListFriend().getListFriend().get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        Friend friend = getFriendAt(position);

        holder.itemBinding.txtName.setText(friend.name);
        holder.itemBinding.txtEmail.setText(friend.email);
        AppUtils.loadImage(context, friend.avata, holder.itemBinding.iconAvata);
        holder.itemBinding.checkAddPeople.setOnCheckedChangeListener(null);
        holder.itemBinding.checkAddPeople.setChecked(friend.isSelected);
        holder.itemBinding.checkAddPeople.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Friend friendAt = getFriendAt(holder.getAdapterPosition());
                friendAt.isSelected = isChecked;
            }
        });
    }

    public List<Friend> getSelectedFriendList() {
        List<Friend> selectedFriends = new ArrayList<>();
        for (Friend friend : mGroupModel.getListFriend().getListFriend()) {
            if (friend.isSelected) {
                selectedFriends.add(friend);
            }
        }
        return selectedFriends;
    }

    @Override
    public int getItemCount() {
        return mGroupModel.getListFriend().getListFriend().size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        private final RcItemAddFriendBinding itemBinding;

        public ItemHolder(View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
        }
    }
}