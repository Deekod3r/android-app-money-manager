package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityAddGoalBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.GoalViewModel;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

public class AddGoalActivity extends AppCompatActivity {

    private ActivityAddGoalBinding binding;
    private GoalViewModel goalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_goal);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
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
                LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
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
                LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
                goalViewModel.getGoalAddRequest().setEndDate(localDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.btnSave.setOnClickListener(v -> {
            goalViewModel.addGoal();
            goalViewModel.getResultAddGoal().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("goalAdded", response.getData());
                    data.putExtra("action", Constants.ACTION_ADD);
                    setResult(Activity.RESULT_OK, data);
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    FunctionUtils.showDialogNotify(this, "", response.getMessage(), DialogType.ERROR);
                }
                goalViewModel.getResultAddGoal().removeObservers(this);
            });
        });
    }


}