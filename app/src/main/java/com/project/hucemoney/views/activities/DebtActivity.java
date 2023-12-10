package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.databinding.ActivityCurrentFinanceBinding;
import com.project.hucemoney.databinding.ActivityDebtBinding;

public class DebtActivity extends AppCompatActivity {

    private ActivityDebtBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDebtBinding.inflate(getLayoutInflater());
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