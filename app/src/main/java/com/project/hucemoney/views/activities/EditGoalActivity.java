package com.project.hucemoney.views.activities;

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
import com.project.hucemoney.common.enums.CategoryType;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityEditCategoryBinding;
import com.project.hucemoney.databinding.ActivityEditGoalBinding;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.CategoryViewModel;
import com.project.hucemoney.viewmodels.GoalViewModel;

import java.time.LocalDate;

public class EditGoalActivity extends AppCompatActivity {

    private ActivityEditGoalBinding binding;
    private GoalViewModel goalViewModel;
    private int position;
    private Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_goal);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        if (position == -1) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
            finish();
        }
        goal = intent.getParcelableExtra("goal");
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalViewModel.getGoalEditRequest().setUUID(goal.getUUID());
        goalViewModel.getGoalEditRequest().setName(goal.getName());
        goalViewModel.getGoalEditRequest().setNote(goal.getNote());
        goalViewModel.getGoalEditRequest().setCurrentAmount(goal.getCurrentAmount());
        goalViewModel.getGoalEditRequest().setTargetAmount(goal.getTargetAmount());
        goalViewModel.getGoalEditRequest().setStartDate(goal.getStartDate());
        goalViewModel.getGoalEditRequest().setEndDate(goal.getEndDate());
        binding.edtTargetAmount.setText(String.valueOf(goal.getTargetAmount()));
        binding.edtCurrentAmount.setText(String.valueOf(goal.getCurrentAmount()));
        binding.edtStartDate.setText(goal.getStartDate().format(Constants.DATE_FORMATTER));
        binding.edtEndDate.setText(goal.getEndDate().format(Constants.DATE_FORMATTER));
        binding.setGoalViewModel(goalViewModel);
        binding.setLifecycleOwner(this);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
        binding.edtCurrentAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtCurrentAmount.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtCurrentAmount.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        goalViewModel.getGoalAddRequest().setCurrentAmount(Long.parseLong(s.toString()));
                    }
                }
                binding.edtCurrentAmount.setSelection(binding.edtCurrentAmount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.edtTargetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtTargetAmount.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtTargetAmount.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        goalViewModel.getGoalAddRequest().setTargetAmount(Long.parseLong(s.toString()));
                    }
                }
                binding.edtTargetAmount.setSelection(binding.edtTargetAmount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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
                String startDate = binding.edtStartDate.getTag().toString();
                LocalDate localDate = LocalDate.parse(startDate, Constants.DATE_FORMATTER);
                goalViewModel.getGoalAddRequest().setStartDate(localDate);
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
                String endDate = binding.edtEndDate.getTag().toString();
                LocalDate localDate = LocalDate.parse(endDate, Constants.DATE_FORMATTER);
                goalViewModel.getGoalAddRequest().setEndDate(localDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.btnSave.setOnClickListener(v -> {
            goalViewModel.editGoal();
            goalViewModel.getResultEditGoal().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("goalEdited", response.getData());
                    data.putExtra("position", position);
                    data.putExtra("action", Constants.ACTION_EDIT);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
        binding.btnDelete.setOnClickListener(v -> {
            FunctionUtils.showDialogConfirm(this, "", "Bạn có chắc chắn muốn xóa mục tiêu này không?", DialogType.WARNING,
                    (dialog, which) -> {
                        goalViewModel.deleteGoal();
                        goalViewModel.getResultDeleteGoal().observe(this, response -> {
                            if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                                FunctionUtils.hideKeyboard(this,v);
                                Intent data = new Intent();
                                data.putExtra("position", position);
                                data.putExtra("action", Constants.ACTION_DELETE);
                                setResult(Activity.RESULT_OK, data);
                                finish();
                            }
                            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }, (dialog, which) -> {
                    });
        });
    }
}