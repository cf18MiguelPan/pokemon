package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvUserEmail;
    ImageView userImageView;
    Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

            tvUserName = findViewById(R.id.userName);
            tvUserEmail = findViewById(R.id.userEmail);
            userImageView = findViewById(R.id.userImage);
            btnSignOut = findViewById(R.id.btnLogout);

            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

            String userName = preferences.getString("username", "");
            String userEmail = preferences.getString("useremail", "");
            String userPhotoUrl = preferences.getString("userphoto", "");

            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);

        Glide.with(this).load(userPhotoUrl).into(userImageView);

    }



}