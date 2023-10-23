package com.project.hucemoney.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.databinding.ActivityEditAccountBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.models.requests.AccountEditRequest;
import com.project.hucemoney.viewmodels.AccountViewModel;

public class EditAccountActivity extends AppCompatActivity {

    private ActivityEditAccountBinding binding;
    private AccountViewModel accountViewModel;
    private Account account;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account);
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
        account = getIntent().getParcelableExtra("account");
        position = getIntent().getIntExtra("position", -1);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        AccountEditRequest accountEditRequest = AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote());
        accountViewModel.setAccountEditRequest(accountEditRequest);
        accountViewModel.setAmountAsString(String.valueOf(account.getAmount()));
        binding.setLifecycleOwner(this);
        binding.setAccountViewModel(accountViewModel);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> accountViewModel.updateAccount(position));
    }

    private void observe() {
        accountViewModel.getResultEditAccount().observe(this, response -> {
            if (response != null) {
                //Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    finish();
                }
            }
        });
    }
}