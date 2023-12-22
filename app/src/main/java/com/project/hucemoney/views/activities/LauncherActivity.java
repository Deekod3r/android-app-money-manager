package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityLaucherBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.NotificationUtils;
import com.project.hucemoney.utils.SessionManager;

public class LauncherActivity extends AppCompatActivity {

    private ActivityLaucherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!NotificationUtils.isNotificationPermissionGranted(this)) {
            FunctionUtils.showDialogConfirm(this, "", "Bạn cần cấp quyền thông báo cho ứng dụng để có thể nhận được thông báo",
                    DialogType.WARNING,
                    (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        startActivity(intent);
                    }, (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    });
        } else {
            binding = ActivityLaucherBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            SessionManager sessionManager = new SessionManager(this);
            if (sessionManager.isLoggedIn()) {
                Intent intent = new Intent(this, AppActivity.class);
                startActivity(intent);
                finish();
            }
            controlAction();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void controlAction() {
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}