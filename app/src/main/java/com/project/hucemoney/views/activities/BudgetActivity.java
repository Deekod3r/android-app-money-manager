package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.databinding.ActivityBudgetBinding;

public class BudgetActivity extends AppCompatActivity {

    private ActivityBudgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_budget);
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
        binding.btnClose.setOnClickListener(v -> finish());
    }
}