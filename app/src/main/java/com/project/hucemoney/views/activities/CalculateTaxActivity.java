package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.ActivityCalculateTaxBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class CalculateTaxActivity extends AppCompatActivity {

    private ActivityCalculateTaxBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculateTaxBinding.inflate(getLayoutInflater());
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
        Intent intent = getIntent();
        NumberFormat formatter = NumberFormat.getInstance(Locale.GERMANY);
        long income = intent.getLongExtra("income", 0);
        long insurance = intent.getLongExtra("insurance", 0);
        int dp = intent.getIntExtra("dependentPeople", 0);
        binding.tvIncome.setText(String.format("%s %s", formatter.format(income), this.getString(R.string.vi_currency)));
        binding.tvTaxPercent.setText(String.format("%s %s", insurance * 10.5 / 100, this.getString(R.string.vi_currency)));
        binding.tvBHXHPercent.setText(String.format("%s %s", Constants.BHXH * insurance, this.getString(R.string.vi_currency)));
        binding.tvBHYTPercent.setText(String.format("%s %s", Constants.BHYT * insurance, this.getString(R.string.vi_currency)));
        binding.tvBHTNPercent.setText(String.format("%s %s", Constants.BHTN * insurance, this.getString(R.string.vi_currency)));
        binding.tvDP.setText(String.format("%s %s", formatter.format(dp * 4_400_000L), this.getString(R.string.vi_currency)));
        binding.tvMine.setText(String.format("%s %s", formatter.format(11_000_000L), this.getString(R.string.vi_currency)));
        double v = income - insurance * 10.5 / 100 - dp * 4_400_000L - 11_000_000L;
        if (v < 0) {
            v = 0;
        }
        binding.tvIncomeTax.setText(String.format("%s %s", formatter.format(v), this.getString(R.string.vi_currency)));
        double tax = 0;
        if (income <= 5000000) {
            tax = income * 0.05;
        } else if (income <= 10000000) {
            tax = 5000000 * 0.05 + (income - 5000000) * 0.1;
        } else if (income <= 18000000) {
            tax = 5000000 * 0.05 + 5000000 * 0.1 + (income - 10000000) * 0.15;
        } else if (income <= 32000000) {
            tax = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + (income - 18000000) * 0.2;
        } else if (income <= 52000000) {
            tax = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + 14000000 * 0.2 + (income - 32000000) * 0.25;
        } else if (income <= 80000000) {
            tax = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + 14000000 * 0.2 + 20000000 * 0.25 + (income - 52000000) * 0.3;
        } else {
            tax = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + 14000000 * 0.2 + 20000000 * 0.25 + 28000000 * 0.3 + (income - 80000000) * 0.35;
        }
        binding.tvTax.setText(String.format("%s %s", formatter.format(tax), this.getString(R.string.vi_currency)));
        binding.tvFinalIncome.setText(String.format("%s %s", formatter.format(income - tax), this.getString(R.string.vi_currency)));
    }

    private void controlAction() {

        binding.btnClose.setOnClickListener(v -> {
            finish();
        });

    }
}