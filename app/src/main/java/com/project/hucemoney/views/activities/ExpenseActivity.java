package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayoutMediator;
import com.project.hucemoney.adapters.custom.ViewPagerExpenseStatisticAdapter;
import com.project.hucemoney.adapters.custom.ViewPagerTransactionStatisticAdapter;
import com.project.hucemoney.databinding.ActivityExpenseBinding;

public class ExpenseActivity extends AppCompatActivity {

    private ActivityExpenseBinding binding;
    private ViewPagerExpenseStatisticAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
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
        adapter = new ViewPagerExpenseStatisticAdapter(this);
        binding.viewPagerExpenseStatistic.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPagerExpenseStatistic, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText("Tháng");
                    break;
                case 2:
                    tab.setText("Năm");
                    break;
                case 0:
                default:
                    tab.setText("Ngày");
                    break;
            }
        }).attach();
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
    }

}