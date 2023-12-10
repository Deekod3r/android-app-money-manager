package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayoutMediator;
import com.project.hucemoney.adapters.custom.ViewPagerTransactionStatisticAdapter;
import com.project.hucemoney.databinding.ActivityTransactionStatisticBinding;

public class TransactionStatisticActivity extends AppCompatActivity {

    private ActivityTransactionStatisticBinding binding;
    private ViewPagerTransactionStatisticAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionStatisticBinding.inflate(getLayoutInflater());
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
        adapter = new ViewPagerTransactionStatisticAdapter(this);
        binding.viewPagerTransactionStatistic.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPagerTransactionStatistic, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText("Tháng");
                    break;
                case 2:
                    tab.setText("Năm");
                    break;
                case 0:
                default:
                    tab.setText("Hiện tại");
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