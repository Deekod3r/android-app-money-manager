package com.project.hucemoney.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import com.project.hucemoney.databinding.ActivityEditBudgetBinding;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.BudgetViewModel;

import java.time.LocalDate;

public class EditBudgetActivity extends AppCompatActivity {

    private ActivityEditBudgetBinding binding;
    private BudgetViewModel budgetViewModel;
    private int position;
    private Category category;
    private ActivityResultLauncher<Intent> mLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_budget);
        init();
        controlAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        Budget budget = intent.getParcelableExtra("budget");
        category = intent.getParcelableExtra("category");
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        assert budget != null;
        budgetViewModel.getBudgetEditRequest().setUUID(budget.getUUID());
        budgetViewModel.getBudgetEditRequest().setName(budget.getName());
        budgetViewModel.getBudgetEditRequest().setCategory(budget.getCategory());
        budgetViewModel.getBudgetEditRequest().setInitialLimit(budget.getInitialLimit());
        budgetViewModel.getBudgetEditRequest().setCurrentBalance(budget.getCurrentBalance());
        budgetViewModel.getBudgetEditRequest().setStartDate(budget.getStartDate());
        budgetViewModel.getBudgetEditRequest().setEndDate(budget.getEndDate());
        budgetViewModel.getBudgetEditRequest().setNote(budget.getNote());
        binding.edtCategory.setText(category.getName());
        binding.edtInitialLimit.setText(String.valueOf(budget.getInitialLimit()));
        binding.edtCurrentBalance.setText(String.valueOf(budget.getCurrentBalance()));
        binding.edtStartDate.setText(budget.getStartDate().format(Constants.DATE_FORMATTER));
        binding.edtEndDate.setText(budget.getEndDate().format(Constants.DATE_FORMATTER));
        binding.setBudgetViewModel(budgetViewModel);
        binding.setLifecycleOwner(this);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            category = data.getParcelableExtra("categorySelected");
                            budgetViewModel.getBudgetEditRequest().setCategory(category.getUUID());
                            binding.edtCategory.setText(category.getName());
                        }
                    } catch (Exception e) {
                        Log.e("EditBudgetActivity", "mLauncher: " + e.getMessage());
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
                        budgetViewModel.getBudgetEditRequest().setInitialLimit(Long.parseLong(s.toString()));
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
                budgetViewModel.getBudgetEditRequest().setStartDate(localDate);
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
                budgetViewModel.getBudgetEditRequest().setEndDate(localDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            budgetViewModel.editBudget();
            budgetViewModel.getResultEditBudget().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("budgetEdited", response.getData());
                    data.putExtra("category", category);
                    data.putExtra("position", position);
                    data.putExtra("action", Constants.ACTION_EDIT);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                budgetViewModel.getResultEditBudget().removeObservers(this);
            });
        });

        binding.btnDelete.setOnClickListener(v -> {
            FunctionUtils.showDialogConfirm(this, "", "Bạn có chắc chắn muốn xóa hạn mức này không?", DialogType.WARNING,
                    (dialog, which) -> {
                        budgetViewModel.deleteBudget();
                        budgetViewModel.getResultDeleteBudget().observe(this, response -> {
                            if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                                FunctionUtils.hideKeyboard(this,v);
                                Intent data = new Intent();
                                data.putExtra("position", position);
                                data.putExtra("action", Constants.ACTION_DELETE);
                                setResult(Activity.RESULT_OK, data);
                                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                            }
                            budgetViewModel.getResultDeleteBudget().removeObservers(this);
                        });
                    }, (dialog, which) -> {
                    });
        });

        binding.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra("budgetUUID", budgetViewModel.getBudgetEditRequest().getUUID());
            intent.putExtra("budgetName", budgetViewModel.getBudgetEditRequest().getName());
            startActivity(intent);
        });
    }
}