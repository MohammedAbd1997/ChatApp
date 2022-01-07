package com.example.myapplicationchats.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationchats.adapter.UserAdapter;
import com.example.myapplicationchats.databinding.ActivityUsersBinding;
import com.example.myapplicationchats.listeners.UserListeners;
import com.example.myapplicationchats.models.User;
import com.example.myapplicationchats.utilities.Constants;
import com.example.myapplicationchats.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListeners {

    private ActivityUsersBinding binding;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUser();

    }

    private void setListeners() {

        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUser() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id =queryDocumentSnapshot.getId();
                            users.add(user);

                        }
                        if (users.size() > 0) {
                            UserAdapter userAdapter = new UserAdapter(users, this);
                            binding.userRecyclerView.setAdapter(userAdapter);
                            binding.userRecyclerView.setVisibility(View.VISIBLE);

                        } else {

                            showErrorMessage();

                        }
                    } else {

                        showErrorMessage();
                    }
                });

    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user Available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);


    }

    private void loading(Boolean isLoading) {

        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}