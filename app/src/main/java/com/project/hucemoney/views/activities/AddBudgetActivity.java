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
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityAddBudgetBinding;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.pojo.BudgetWithCategory;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.BudgetViewModel;

import java.time.LocalDate;

public class AddBudgetActivity extends AppCompatActivity {

    private ActivityAddBudgetBinding binding;
    private BudgetViewModel budgetViewModel;
    private ActivityResultLauncher<Intent> mLauncher;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_budget);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        binding.setBudgetViewModel(budgetViewModel);
        binding.setLifecycleOwner(this);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            category = data.getParcelableExtra("categorySelected");
                            budgetViewModel.getBudgetAddRequest().setCategory(category.getUUID());
                            binding.edtCategory.setText(category.getName());
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.edtInitialLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtInitialLimit.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtInitialLimit.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        budgetViewModel.getBudgetAddRequest().setInitialLimit(Long.parseLong(s.toString()));
                    }
                }
                binding.edtInitialLimit.setSelection(binding.edtInitialLimit.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListCategoryActivity.class);
            intent.putExtra("type", Constants.TYPE_EXPENSE);
            intent.putExtra("categorySelected", category);
            mLauncher.launch(intent);
        });

        binding.edtStartDate.setInputType(InputType.TYPE_NULL);
        binding.edtStartDate.setOnClickListener(v -> {
            FunctionUtils.showDialogDate(this, binding.edtStartDate);
        });
        binding.edtStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
                budgetViewModel.getBudgetAddRequest().setStartDate(localDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.edtEndDate.setInputType(InputType.TYPE_NULL);
        binding.edtEndDate.setOnClickListener(v -> {
            FunctionUtils.showDialogDate(this, binding.edtEndDate);
        });
        binding.edtEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
                budgetViewModel.getBudgetAddRequest().setEndDate(localDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            budgetViewModel.addBudget();
            budgetViewModel.getResultAddBudget().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("budgetAdded", response.getData());
                    data.putExtra("category", category);
                    data.putExtra("action", Constants.ACTION_ADD);
                    setResult(Activity.RESULT_OK, data);
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                }
                budgetViewModel.getResultAddBudget().removeObservers(this);
            });
        });
    }
}