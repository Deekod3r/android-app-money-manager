package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.databinding.ActivityExpenseBinding;
import com.project.hucemoney.databinding.ActivityIncomeBinding;

public class IncomeActivity extends AppCompatActivity {

    private ActivityIncomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIncomeBinding.inflate(getLayoutInflater());
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
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
    }

}