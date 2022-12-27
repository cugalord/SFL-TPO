package com.example.sfl_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sfl_mobile.databinding.ActivityJobsBinding;
import com.example.sfl_mobile.databinding.ActivityManagerBinding;
import com.example.sfl_mobile.ui.login.LoginActivity;

public class ManagerActivity extends AppCompatActivity {

    // Header
    ImageView logoImageView;
    TextView userTextView;

    // Body
    ImageView totalImageView;
    TextView totalTextView;
    String total = "";

    ImageView pendingImageView;
    TextView pendingTextView;
    String pending = "";

    ImageView completedImageView;
    TextView completedTextView;
    String completed = "";

    ImageView driversImageView;
    TextView driversTextView;
    String drivers = "";

    ImageView agentsImageView;
    TextView agentsTextView;
    String agents = "";

    // Footer
    Button signOutButton;

    ActivityManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logoImageView = binding.logoImageView;
        userTextView = binding.userTextView;
        totalImageView = binding.parcelsImageView;
        totalTextView = binding.totalTextView;
        pendingImageView = binding.pendingImageView;
        pendingTextView = binding.pendingTextView;
        completedImageView = binding.completedImageView;
        completedTextView = binding.completedTextView;
        driversImageView = binding.driversImageView;
        driversTextView = binding.driversTextView;
        agentsImageView = binding.agentsImageView;
        agentsTextView = binding.agentsTextView;
        signOutButton = binding.signOutButton;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String res = "\n" + extras.getString("displayName");
            userTextView.setText(res);
            userTextView.setTextSize(17.5f);
            userTextView.setTypeface(null, Typeface.BOLD);
        }

        populateTestData();

        totalTextView.setText(total);
        pendingTextView.setText(pending);
        completedTextView.setText(completed);
        driversTextView.setText(drivers);
        agentsTextView.setText(agents);

        driversImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement
            }
        });

        agentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManagerActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void populateTestData() {
        total = "145";
        pending = "73";
        completed = Integer.toString(145 - 73);
        drivers = "10";
        agents = "12";
    }

}