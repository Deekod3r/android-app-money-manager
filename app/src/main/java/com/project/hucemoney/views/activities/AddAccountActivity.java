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
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityAddAccountBinding;
import com.project.hucemoney.utils.FunctionUtils;
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
        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtAmount.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtAmount.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        accountViewModel.getAccountAddRequest().setAmount(Long.parseLong(s.toString()));
                    }
                }
                binding.edtAmount.setSelection(binding.edtAmount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            accountViewModel.addAccount();
            accountViewModel.getResultAddAccount().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("accountAdded", response.getData());
                    data.putExtra("action", Constants.ACTION_ADD);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                } else {
                    FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                }
            });
        });
    }

}