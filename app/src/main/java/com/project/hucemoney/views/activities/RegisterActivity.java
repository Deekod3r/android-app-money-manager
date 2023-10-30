package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityRegisterBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.NetworkUtils;
import com.project.hucemoney.viewmodels.UserViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setUserViewModel(userViewModel);
    }

    private void controlAction() {

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnRegister.setOnClickListener(v -> {
            try {
                if (NetworkUtils.isNetworkAvailable(this)) {
                    userViewModel.register();
                    userViewModel.getRegisterResult().observe(this, response -> {
                        if (Objects.equals(response.getStatus(), ResponseCode.SUCCESS)) {
                            Intent intent = new Intent(this, VerifyActivity.class);
                            intent.putExtra("typeVerify", "register");
                            intent.putExtra("UUID", response.getData());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    FunctionUtils.showDialogNotify(this, "", "Vui lòng kết nối mạng để thực hiện", DialogType.WARNING);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

}