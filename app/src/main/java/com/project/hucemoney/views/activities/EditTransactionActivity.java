package com.project.hucemoney.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityEditTransactionBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.TransactionViewModel;

import java.time.LocalDate;
import java.util.Objects;

public class EditTransactionActivity extends AppCompatActivity {

    private ActivityEditTransactionBinding binding;
    private TransactionViewModel transactionViewModel;
    private Transaction transaction;
    private Account account;
    private Category category;
    private ActivityResultLauncher<Intent> mLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_transaction);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        binding.setTransactionViewModel(transactionViewModel);
        binding.setLifecycleOwner(this);
        Intent intent = getIntent();
        if (intent != null) {
            transaction = intent.getParcelableExtra("transaction");
            account = intent.getParcelableExtra("account");
            category = intent.getParcelableExtra("category");
            transactionViewModel.getTransactionEditRequest().setUUID(transaction.getUUID());
            transactionViewModel.getTransactionEditRequest().setName(transaction.getName());
            transactionViewModel.getTransactionEditRequest().setAmount(transaction.getAmount());
            transactionViewModel.getTransactionEditRequest().setCategory(category.getUUID());
            transactionViewModel.getTransactionEditRequest().setAccount(account.getUUID());
            transactionViewModel.getTransactionEditRequest().setDate(transaction.getDate());
            transactionViewModel.getTransactionEditRequest().setNote(transaction.getNote());
            binding.edtCategory.setText(category.getName());
            binding.edtAmount.setText(String.valueOf(transaction.getAmount()));
            binding.edtDate.setText(transaction.getDate().format(Constants.DATE_FORMATTER));
            binding.edtNote.setText(transaction.getNote());
            binding.edtAccount.setText(account.getName());
        }
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            if (data.getBooleanExtra("isCategory",false)) {
                                category = data.getParcelableExtra("categorySelected");
                                assert category != null;
                                binding.edtCategory.setText(category.getName());
                                transactionViewModel.getTransactionEditRequest().setCategory(category.getUUID());
                            } else if (data.getBooleanExtra("isAccount",false)) {
                                account = data.getParcelableExtra("accountSelected");
                                assert account != null;
                                binding.edtAccount.setText(account.getName());
                                transactionViewModel.getTransactionEditRequest().setAccount(account.getUUID());
                            }
                        }
                    } catch (Exception e) {
                        Log.e("EditTransactionActivity", "mLauncher: " + e.getMessage());
                    }
                }
        );
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
                        transactionViewModel.getTransactionEditRequest().setAmount(Long.parseLong(s.toString()));
                    }
                }
                binding.edtAmount.setSelection(binding.edtAmount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.edtCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListCategoryActivity.class);
            intent.putExtra("type", transaction.getType());
            intent.putExtra("categorySelected", category);
            mLauncher.launch(intent);
        });

        binding.edtAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListAccountActivity.class);
            intent.putExtra("accountSelected", account);
            mLauncher.launch(intent);
        });

        binding.edtDate.setInputType(InputType.TYPE_NULL);
        binding.edtDate.setOnClickListener(v -> {
            FunctionUtils.showDialogDate(this, binding.edtDate);
        });
        binding.edtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
                    transactionViewModel.getTransactionEditRequest().setDate(localDate);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            transactionViewModel.editTransaction();
            transactionViewModel.getResultEditTransaction().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("action", Constants.ACTION_EDIT);
                    setResult(Activity.RESULT_OK, data);
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                }
                transactionViewModel.getResultEditTransaction().removeObservers(this);
            });
        });

        binding.btnDelete.setOnClickListener(v -> {
            FunctionUtils.showDialogConfirm(this, "", "Bạn có chắc chắn muốn xóa giao dịch này không?", DialogType.WARNING,
                    (dialog, which) -> {
                        transactionViewModel.deleteTransaction();
                        transactionViewModel.getResultDeleteTransaction().observe(this, response -> {
                            if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                                FunctionUtils.hideKeyboard(this,v);
                                Intent data = new Intent();
                                data.putExtra("action", Constants.ACTION_DELETE);
                                setResult(Activity.RESULT_OK, data);
                                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                            }
                            transactionViewModel.getResultDeleteTransaction().removeObservers(this);
                        });
                    }, (dialog, which) -> {
                    });
        });
    }
}