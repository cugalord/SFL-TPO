package com.example.sfl_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sfl_mobile.databinding.ActivityJobsBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class JobsActivity extends AppCompatActivity {
    TextView userData;
    ImageView logoView;

    Button pendingButton;
    Button completedButton;

    ScrollView jobsView;
    LinearLayout jobsLayout;

    ActivityJobsBinding binding;

    ArrayList<String> pendingJobsData = new ArrayList<>();
    ArrayList<String> completedJobsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        binding = ActivityJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData = binding.userTextView;
        logoView = binding.logoImageView;
        pendingButton = binding.buttonPending;
        completedButton = binding.buttonDone;
        jobsView = binding.jobsView;
        jobsLayout = binding.jobsLayout;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String res = "\n" + extras.getString("displayName");
            userData.setText(res);
            userData.setTextSize(17.5f);
            userData.setTypeface(null, Typeface.BOLD);
        }

        populateTestData();
        fillLayout(pendingJobsData);

        pendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillLayout(pendingJobsData);
            }
        });

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillLayout(completedJobsData);
            }
        });
    }

    private void fillLayout(ArrayList<String> data) {
        jobsLayout.removeAllViews();

        for (String jobData : data) {
            RelativeLayout relativeLayout = new RelativeLayout(JobsActivity.this);
            LinearLayout textLayout = new LinearLayout(JobsActivity.this);

            TextView jobIDView = new TextView(JobsActivity.this);
            TextView dimensionsView = new TextView(JobsActivity.this);
            TextView weightView = new TextView(JobsActivity.this);
            TextView statusView = new TextView(JobsActivity.this);

            Button button = new Button(JobsActivity.this);

            // Set text.
            String [] jobAttributes = jobData.split(" ");
            jobIDView.setText(jobAttributes[0]);
            dimensionsView.setText(jobAttributes[1]);
            weightView.setText(jobAttributes[2] + "g");
            statusView.setText(jobAttributes[3]);

            // Implement button functionality.
            button.setText("SCAN");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(JobsActivity.this, ScannedBarcodeActivity.class);
                    i.putExtra("parcelID", "XYZ123WH");
                    startActivity(i);
                    // TODO: Implement
                }
            });

            // Style text.
            jobIDView.setPadding(10, 15, 25, 0);
            jobIDView.setTextSize(17.5f);
            jobIDView.setTypeface(null, Typeface.BOLD);

            dimensionsView.setPadding(10, 15, 25, 0);
            dimensionsView.setTextSize(17.5f);
            dimensionsView.setTypeface(null, Typeface.BOLD);

            weightView.setPadding(10, 15, 25, 0);
            weightView.setTextSize(17.5f);
            weightView.setTypeface(null, Typeface.BOLD);

            statusView.setPadding(10, 15, 25, 0);
            statusView.setTextSize(17.5f);
            statusView.setTypeface(null, Typeface.BOLD);

            if (jobAttributes[3].toLowerCase(Locale.ROOT).equals("pending")) {
                statusView.setTextColor(Color.GREEN);
            }
            else {
                statusView.setTextColor(Color.RED);
            }

            // Add text views to text layout.
            textLayout.addView(jobIDView);
            textLayout.addView(dimensionsView);
            textLayout.addView(weightView);
            textLayout.addView(statusView);

            // Add text layout and button to relative layout.
            relativeLayout.addView(textLayout);
            // Only add button if job is pending.
            if (jobAttributes[3].toLowerCase(Locale.ROOT).equals("pending")) {
                relativeLayout.addView(button);
                // Align button to the right side of page.
                RelativeLayout.LayoutParams buttonParams =
                        (RelativeLayout.LayoutParams) button.getLayoutParams();
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }



            // Align text to the left side of page.
            RelativeLayout.LayoutParams textViewLayoutParams =
                    (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            jobsLayout.addView(relativeLayout);
        }
    }

    private void populateTestData() {
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");

        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
    }
}