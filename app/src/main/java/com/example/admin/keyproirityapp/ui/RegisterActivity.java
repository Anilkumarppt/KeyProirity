package com.example.admin.keyproirityapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.keyproirityapp.MainActivity;
import com.example.admin.keyproirityapp.R;
import com.example.admin.keyproirityapp.database.StaticConfig;
import com.example.admin.keyproirityapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public static String STR_EXTRA_ACTION_REGISTER = "register";
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    FirebaseAuth mAuth;
    EditText mUsername, mUseremail, mUserMobile, mUserPassword, mUserAge;
    CheckBox mTermsandCondition;
    String username, email, password, mobile, age;
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mUsername = (EditText) findViewById(R.id.signup_user);
        mUserMobile = (EditText) findViewById(R.id.signup_phone);
        mUseremail = (EditText) findViewById(R.id.signup_email);
        mUserPassword = (EditText) findViewById(R.id.signup_password);
        mUserAge = (EditText) findViewById(R.id.signup_age);
        mTermsandCondition = (CheckBox) findViewById(R.id.terms);
        mDatabase = FirebaseDatabase.getInstance().getReference(StaticConfig.FIREBASE_USERROOT);

    }


    public void create_account(View view) {
        username = mUsername.getText().toString();
        email = mUseremail.getText().toString();
        password = mUserPassword.getText().toString();
        mobile = mUserMobile.getText().toString();
        age = mUserAge.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mUsername.setError("Email is required");
            mUsername.requestFocus();
        }
        if (TextUtils.isEmpty(username)) {
            mUseremail.setError("Email is required");
            mUseremail.requestFocus();
        }
        if (TextUtils.isEmpty(mobile)) {
            mUserMobile.setError("Email is required");
            mUserMobile.requestFocus();

        }
        if (TextUtils.isEmpty(password)) {
            mUserPassword.setError("Email is required");
            mUserPassword.requestFocus();

        }
        if (TextUtils.isEmpty(age)) {
            mUserAge.setError("Email is required");
            mUserAge.requestFocus();

        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        saveUserInfo(username, email, password, mobile, age);
                        //   startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Helo Error" + task.getException().getMessage());
                        }

                    }

                }
            });
        }
    }

    private void saveUserInfo(String username, String email, String password, String mobile, String age) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        String onlineUser_id = mAuth.getCurrentUser().getUid();
        String device_token = FirebaseInstanceId.getInstance().getToken();
        mDatabase = mFirebaseDatabase.getReference("user");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        User newUser = new User();
        newUser.email = currentUser.getEmail();
        newUser.name = username;
        newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
        newUser.mobile = mobile;
        newUser.deviceToken = device_token;
        /// /   UserInfo userInfo=new UserInfo(username,email,password,mobile,age,"deafult_image","Hey I am Using ChatApp");
        mDatabase.child(id).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
    }

    void initNewUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        User newUser = new User();
        newUser.email = user.getEmail();
        newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
        FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
    }

    private boolean validate(String emailStr, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return password.length() > 0 && matcher.find();
    }

    public void login(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
