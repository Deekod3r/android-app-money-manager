package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.ActivityTaxBinding;
import com.project.hucemoney.utils.FunctionUtils;

public class TaxActivity extends AppCompatActivity {

    private ActivityTaxBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        binding.tvTaxPercent.setText(String.format("%s %%", Constants.BHXH * 100 + Constants.BHYT * 100 + Constants.BHTN * 100));
        binding.tvBHXHPercent.setText(String.format("%s %%", Constants.BHXH * 100));
        binding.tvBHYTPercent.setText(String.format("%s %%", Constants.BHYT * 100));
        binding.tvBHTNPercent.setText(String.format("%s %%", Constants.BHTN * 100));
    }

    private void controlAction() {

        binding.edtArea.setOnClickListener(v -> {
            FunctionUtils.showDialogChoose(this, binding.edtArea, new String[]{"Vùng 1", "Vùng 2", "Vùng 3", "Vùng 4"});
        });

        binding.btnChoiseArea.setOnClickListener(v -> {
            FunctionUtils.showDialogChoose(this, binding.edtArea, new String[]{"Vùng 1", "Vùng 2", "Vùng 3", "Vùng 4"});
        });

        binding.edtIncome.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtIncome.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtIncome.setText(s.subSequence(1, s.length()));
                    }
                }
                binding.edtIncome.setSelection(binding.edtIncome.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtInsurance.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtInsurance.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtInsurance.setText(s.subSequence(1, s.length()));
                    }
                }
                binding.edtInsurance.setSelection(binding.edtInsurance.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtDependentPeople.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtDependentPeople.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtDependentPeople.setText(s.subSequence(1, s.length()));
                    }
                }
                binding.edtDependentPeople.setSelection(binding.edtDependentPeople.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnClose.setOnClickListener(v -> finish());

        binding.btnCalculate.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalculateTaxActivity.class);
            intent.putExtra("income", Integer.parseInt(binding.edtIncome.getText().toString()));
            intent.putExtra("insurance", Integer.parseInt(binding.edtInsurance.getText().toString()));
            intent.putExtra("dependentPeople", Integer.parseInt(binding.edtDependentPeople.getText().toString()));
            intent.putExtra("area", binding.edtArea.getText().toString());
            startActivity(intent);
        });
    }
}