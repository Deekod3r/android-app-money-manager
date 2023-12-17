package com.project.hucemoney.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityEditAccountBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.models.requests.AccountEditRequest;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.AccountViewModel;

public class EditAccountActivity extends AppCompatActivity {

    private ActivityEditAccountBinding binding;
    private AccountViewModel accountViewModel;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        account = getIntent().getParcelableExtra("account");
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        AccountEditRequest accountEditRequest = AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote());
        accountViewModel.setAccountEditRequest(accountEditRequest);
        binding.edtAmount.setText(String.valueOf(account.getAmount()));
        binding.setLifecycleOwner(this);
        binding.setAccountViewModel(accountViewModel);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            Intent data = new Intent();
            setResult(Activity.RESULT_CANCELED, data);
            finish();
        });
        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 || (s.length() == 1 && s.toString().equals("-"))) {
                    binding.edtAmount.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtAmount.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        accountViewModel.getAccountEditRequest().setAmount(Long.parseLong(s.toString()));
                    }
                }
                binding.edtAmount.setSelection(binding.edtAmount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.btnSave.setOnClickListener(v -> {
            accountViewModel.editAccount();
            accountViewModel.getResultEditAccount().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("accountEdited", response.getData());
                    setResult(Activity.RESULT_OK, data);
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                }
                accountViewModel.getResultEditAccount().removeObservers(this);
            });
        });
    }

}