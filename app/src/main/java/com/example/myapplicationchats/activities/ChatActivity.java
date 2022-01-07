package com.example.myapplicationchats.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplicationchats.R;
import com.example.myapplicationchats.databinding.ActivityChatBinding;
import com.example.myapplicationchats.models.User;
import com.example.myapplicationchats.utilities.Constants;

public class ChatActivity extends AppCompatActivity {


    private ActivityChatBinding binding;


    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();


    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.TextName.setText(receiverUser.name);


    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v-> onBackPressed());



    }
}