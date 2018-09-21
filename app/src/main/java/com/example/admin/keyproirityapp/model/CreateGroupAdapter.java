package com.example.admin.keyproirityapp.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.FriendDB;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.ui.GroupChatActivity;
import com.example.admin.keyproirityapp.ui.GroupFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static ListFriend listFriend = null;
    private ArrayList<Group> listGroup;
    private Context context;

    public CreateGroupAdapter(Context context, ArrayList<Group> listGroup) {
        this.context = context;
        this.listGroup = listGroup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_group, parent, false);
        return new ItemGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String groupName = listGroup.get(position).groupInfo.get("name");
        if (groupName != null && groupName.length() > 0) {
            ((ItemGroupViewHolder) holder).txtGroupName.setText(groupName);
            ((ItemGroupViewHolder) holder).iconGroup.setText((groupName.charAt(0) + "").toUpperCase());
        }
        ((ItemGroupViewHolder) holder).btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(new Object[]{groupName, position});
                view.getParent().showContextMenuForChild(view);
            }
        });
        ((RelativeLayout) ((ItemGroupViewHolder) holder).txtGroupName.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listFriend == null) {
                    listFriend = FriendDB.getInstance(context).getListFriend();
                }
                //Intent intent = new Intent(context, BasicTest.class);
              /*  Intent intent = new Intent(context, BasicTest.class);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, groupName);
                ArrayList<CharSequence> idFriend = new ArrayList<>();
                BasicTest.bitmapAvataFriend = new HashMap<>();
                for (String id : listGroup.get(position).member) {
                    idFriend.add(id);
                    String avata = listFriend.getAvataById(id);
                    if (!avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                        byte[] decodedString = Base64.decode(avata, Base64.DEFAULT);
                        BasicTest.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                    } else if (avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                        BasicTest.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata));
                    } else {
                        BasicTest.bitmapAvataFriend.put(id, null);
                    }
                }
                intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, listGroup.get(position).id);
                intent.putExtra(StaticConfig.PERSONAL_CHAT, "group");
                context.startActivity(intent);
*/
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, groupName);
                ArrayList<CharSequence> idFriend = new ArrayList<>();
                GroupChatActivity.bitmapAvataFriend = new HashMap<>();
                for (String id : listGroup.get(position).member) {
                    idFriend.add(id);
                    String avata = listFriend.getAvataById(id);
                    if (!avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                        byte[] decodedString = Base64.decode(avata, Base64.DEFAULT);
                        GroupChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                    } else if (avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                        GroupChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata));
                    } else {
                        GroupChatActivity.bitmapAvataFriend.put(id, null);
                    }
                }
                intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, listGroup.get(position).id);
                intent.putExtra(StaticConfig.PERSONAL_CHAT, "group");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGroup.size();
    }

    public static class ItemGroupViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView iconGroup, txtGroupName;
        ImageButton btnMore;

        ItemGroupViewHolder(View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            iconGroup = (TextView) itemView.findViewById(R.id.icon_group);
            txtGroupName = (TextView) itemView.findViewById(R.id.txtName);
            btnMore = (ImageButton) itemView.findViewById(R.id.btnMoreAction);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle((String) ((Object[]) btnMore.getTag())[0]);
            Intent data = new Intent();
            data.putExtra(GroupFragment.CONTEXT_MENU_KEY_INTENT_DATA_POS, (Integer) ((Object[]) btnMore.getTag())[1]);
            menu.add(Menu.NONE, GroupFragment.CONTEXT_MENU_EDIT, Menu.NONE, "Edit group").setIntent(data);
            menu.add(Menu.NONE, GroupFragment.CONTEXT_MENU_DELETE, Menu.NONE, "Delete group").setIntent(data);
            menu.add(Menu.NONE, GroupFragment.CONTEXT_MENU_LEAVE, Menu.NONE, "Leave group").setIntent(data);
        }
    }
}