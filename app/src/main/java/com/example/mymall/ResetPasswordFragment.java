package com.example.mymall;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private EditText registeredEmail;
    private Button resetPasswordBtn;
    private TextView goBack;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    private FrameLayout parentFrameLayout;

    public ResetPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail = view.findViewById(R.id.forgot_password_email);
        resetPasswordBtn = view.findViewById(R.id.reset_password_btn);
        goBack = view.findViewById(R.id.tv_reset_password_go_back);
        parentFrameLayout = requireActivity().findViewById(R.id.register_frameLayout);

        emailIconContainer = view.findViewById(R.id.forgot_password_email_icon_container);
        emailIcon = view.findViewById(R.id.forgot_password_email_icon);
        emailIconText = view.findViewById(R.id.forgot_password_icon_text);
        progressBar = view.findViewById(R.id.forgot_password_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registeredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetPasswordBtn.setOnClickListener(v -> {

            TransitionManager.beginDelayedTransition(emailIconContainer);
            emailIconText.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(emailIconContainer);
            emailIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            resetPasswordBtn.setEnabled(false);
            resetPasswordBtn.setTextColor(Color.argb(50, 255, 255, 255));

            firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, emailIcon.getWidth() / 2, emailIcon.getHeight() / 2);
                            scaleAnimation.setDuration(100);
                            scaleAnimation.setInterpolator(new AccelerateInterpolator());
                            scaleAnimation.setRepeatMode(Animation.REVERSE);
                            scaleAnimation.setRepeatCount(1);

                            scaleAnimation.setAnimationListener(new MyAnimationListener());
                            emailIcon.startAnimation(scaleAnimation);
                        } else {
                            String error = task.getException().getMessage();

                            resetPasswordBtn.setEnabled(true);
                            resetPasswordBtn.setTextColor(Color.rgb(255, 255, 255));

                            emailIconText.setText(error);
                            emailIconText.setTextColor(getResources().getColor(R.color.colorPrimary, requireContext().getTheme()));
                            TransitionManager.beginDelayedTransition(emailIconContainer);
                            emailIconText.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                    });
        });

        goBack.setOnClickListener(v -> setFragment(new SignInFragment()));
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registeredEmail.getText())) {
            resetPasswordBtn.setEnabled(false);
            resetPasswordBtn.setTextColor(Color.argb(50, 255, 255, 255));
        } else {
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private class MyAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onAnimationEnd(Animation animation) {
            emailIconText.setText("Recovery email sent successfully ! check your inbox");
            emailIconText.setTextColor(getResources().getColor(R.color.successGreen, requireActivity().getTheme()));

            TransitionManager.beginDelayedTransition(emailIconContainer);
            emailIconText.setVisibility(View.VISIBLE);
            emailIcon.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            emailIcon.setImageResource(R.mipmap.green_mail);
        }
    }
}