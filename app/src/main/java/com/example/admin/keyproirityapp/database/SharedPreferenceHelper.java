package com.example.admin.keyproirityapp.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.admin.keyproirityapp.model.User;


public class SharedPreferenceHelper {
    private static final String SHARE_KEY_ROOMID = "roomId";
    private static SharedPreferenceHelper instance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static String SHARE_USER_INFO = "userinfo";
    private static String SHARE_KEY_NAME = "name";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATA = "avata";
    private static String SHARE_KEY_UID = "uid";
    private static String GROUPICON = "groupIcon";
    private static String GROUPNAME = "name";
    private static String SHARE_KEY_DEVICE_TOKEN = "deviceToken";
    private static String SHARE_KEY_MOBILE = "mobile";

    private SharedPreferenceHelper() {
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceHelper();
            preferences = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }


    public void saveUserInfo(User user) {
        editor.putString(SHARE_KEY_NAME, user.name);
        editor.putString(SHARE_KEY_EMAIL, user.email);
        editor.putString(SHARE_KEY_AVATA, user.avata);
        editor.putString(SHARE_KEY_UID, StaticConfig.UID);
        editor.putString(SHARE_KEY_MOBILE, user.mobile);
        editor.putString(SHARE_KEY_DEVICE_TOKEN, user.deviceToken);
        editor.apply();
    }

    public String getGroupId() {
        String roomId = preferences.getString(SHARE_KEY_ROOMID, "");
        return roomId;
    }

    public void setGroupId(String groupId) {
        editor.putString(SHARE_KEY_ROOMID, groupId);
    }

    public User getUserInfo() {
        String userName = preferences.getString(SHARE_KEY_NAME, "");
        String email = preferences.getString(SHARE_KEY_EMAIL, "");
        String avatar = preferences.getString(SHARE_KEY_AVATA, "default");
        String mobile = preferences.getString(SHARE_KEY_MOBILE, "0");
        String deviceToken = preferences.getString(SHARE_KEY_DEVICE_TOKEN, "default");
        User user = new User(userName, email, avatar, mobile, deviceToken);
        return user;
    }

    public String getUID() {
        return preferences.getString(SHARE_KEY_UID, "");
    }

}
