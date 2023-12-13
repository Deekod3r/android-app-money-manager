package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.project.hucemoney.R;
import com.project.hucemoney.adapters.custom.ViewPagerCategoryStatisticAdapter;
import com.project.hucemoney.adapters.custom.ViewPagerTransactionStatisticAdapter;
import com.project.hucemoney.databinding.ActivityTimeStatisticBinding;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.CategoryStatistic;
import com.project.hucemoney.entities.pojo.TransactionWithCategory;
import com.project.hucemoney.viewmodels.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;

public class TimeStatisticActivity extends AppCompatActivity {

    private ActivityTimeStatisticBinding binding;
    private ViewPagerCategoryStatisticAdapter adapter;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_statistic);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        binding.tvTime.setText(time);
        int type = intent.getIntExtra("type", -1);
        adapter = new ViewPagerCategoryStatisticAdapter(this);
        binding.viewPagerStatistic.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPagerStatistic, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText("Thu");
                    break;
                case 0:
                default:
                    tab.setText("Chi");
                    break;
            }
        }).attach();
    }

    private void initRecyclerView() {
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
    }

    private void observer() {
    }
}