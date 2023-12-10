package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.AccountAdapter;
import com.project.hucemoney.databinding.ActivityAnalystFinanceBinding;
import com.project.hucemoney.databinding.ActivityCurrentFinanceBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.viewmodels.AccountViewModel;

import java.util.ArrayList;
import java.util.List;

public class CurrentFinanceActivity extends AppCompatActivity {

    private ActivityCurrentFinanceBinding binding;
    private List<Account> accounts = new ArrayList<>();
    private AccountAdapter accountAdapter;
    private RecyclerView recyclerView;
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_finance);
        setContentView(binding.getRoot());
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
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountAdapter = new AccountAdapter(this, accounts, false, R.layout.item_account_current_finance);
        accountViewModel.loadAccounts();
        binding.setAccountViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvAccount;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(accountAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
    }

    private void observer() {
        accountViewModel.getAccounts().observe(this, accounts -> {
            accountAdapter.setData(accounts);
            if (accounts == null || accounts.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
        accountViewModel.getAmountTotal().observe(this, s -> {
            binding.tvSumOwn.setText(String.format("%s", s));
            binding.tvFinanceAmount.setText(String.format("%s", s));
        });
    }
}