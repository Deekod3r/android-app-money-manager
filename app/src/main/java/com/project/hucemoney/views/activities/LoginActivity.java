package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityLoginBinding;
import com.project.hucemoney.entities.User;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.NetworkUtils;
import com.project.hucemoney.utils.SessionManager;
import com.project.hucemoney.viewmodels.UserViewModel;

import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        init();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        userViewModel.getLoginResult().removeObservers(this);
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

        binding.btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        binding.btnLoginFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        binding.btnLoginGoogle.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogin.setOnClickListener(v -> {
                if (NetworkUtils.isNetworkAvailable(this)) {
                    userViewModel.login();
                } else {
                    FunctionUtils.showDialogNotify(this, "", "Vui lòng kết nối mạng để thực hiện", DialogType.WARNING);
                }
            }
        );

        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void observer() {
        Observer<Response<Map<String, String>>> loginObserver = response -> {
            if (Objects.equals(response.getStatus(), ResponseCode.SUCCESS)) {
                Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                SessionManager sessionManager = new SessionManager(LoginActivity.this);
                sessionManager.setLoggedIn();
                sessionManager.setUUID(response.getData().get("UUID"));
                sessionManager.setUsername(response.getData().get("username"));
                sessionManager.setEmail(response.getData().get("email"));
                Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            } else {
                FunctionUtils.showDialogNotify(LoginActivity.this, "", response.getMessage(), DialogType.ERROR);
            }
        };
        userViewModel.getLoginResult().observe(this, loginObserver);
    }

}