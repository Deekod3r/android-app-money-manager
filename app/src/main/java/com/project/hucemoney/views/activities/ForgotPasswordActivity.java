package com.project.hucemoney.views.activities;

import static com.project.hucemoney.utils.ValidationUtils.isValidEmail;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityForgotPasswordBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.NetworkUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void controlAction() {
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        binding.btnSend.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            if (!isValidEmail(email)) {
                FunctionUtils.showDialogNotify(this, "", "Email không hợp lệ", DialogType.ERROR);
                return;
            }
            if (NetworkUtils.isNetworkAvailable(this)) {
                // request to server
                Intent intent = new Intent(this, VerifyActivity.class);
                startActivity(intent);
                finish();
            } else {
                FunctionUtils.showDialogNotify(this, "", "Vui lòng kết nối mạng để thực hiện", DialogType.WARNING);
            }
        });
    }
}