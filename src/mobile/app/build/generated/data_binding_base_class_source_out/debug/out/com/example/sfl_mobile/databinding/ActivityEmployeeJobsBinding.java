// Generated by view binder compiler. Do not edit!
package com.example.sfl_mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.sfl_mobile.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEmployeeJobsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button backButton;

  @NonNull
  public final LinearLayout jobsLayout;

  @NonNull
  public final ScrollView jobsView;

  @NonNull
  public final ImageView logoImageView;

  @NonNull
  public final TextView userTextView;

  private ActivityEmployeeJobsBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button backButton, @NonNull LinearLayout jobsLayout, @NonNull ScrollView jobsView,
      @NonNull ImageView logoImageView, @NonNull TextView userTextView) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.jobsLayout = jobsLayout;
    this.jobsView = jobsView;
    this.logoImageView = logoImageView;
    this.userTextView = userTextView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEmployeeJobsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEmployeeJobsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_employee_jobs, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEmployeeJobsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      Button backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.jobsLayout;
      LinearLayout jobsLayout = ViewBindings.findChildViewById(rootView, id);
      if (jobsLayout == null) {
        break missingId;
      }

      id = R.id.jobsView;
      ScrollView jobsView = ViewBindings.findChildViewById(rootView, id);
      if (jobsView == null) {
        break missingId;
      }

      id = R.id.logoImageView;
      ImageView logoImageView = ViewBindings.findChildViewById(rootView, id);
      if (logoImageView == null) {
        break missingId;
      }

      id = R.id.userTextView;
      TextView userTextView = ViewBindings.findChildViewById(rootView, id);
      if (userTextView == null) {
        break missingId;
      }

      return new ActivityEmployeeJobsBinding((ConstraintLayout) rootView, backButton, jobsLayout,
          jobsView, logoImageView, userTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
