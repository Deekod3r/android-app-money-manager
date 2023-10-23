package com.project.hucemoney.views.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.databinding.ActivityAddAccountBinding;
import com.project.hucemoney.viewmodels.AccountViewModel;

public class AddAccountActivity extends AppCompatActivity {

    private ActivityAddAccountBinding binding;
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        init();
        controlAction();
        observe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setAccountViewModel(accountViewModel);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> accountViewModel.insertAccount());
    }

    private void observe() {
        accountViewModel.getResultAddAccount().observe(this, response -> {
            if (response != null) {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    finish();
                }
            }
        });
    }
}