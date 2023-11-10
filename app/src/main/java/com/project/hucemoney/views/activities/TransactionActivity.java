package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.TransactionGroupAdapter;
import com.project.hucemoney.databinding.ActivityTransactionBinding;
import com.project.hucemoney.entities.pojo.TransactionGroup;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;
import com.project.hucemoney.viewmodels.TransactionViewModel;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionActivity extends AppCompatActivity {

    private ActivityTransactionBinding binding;
    private List<TransactionGroup> transactionGroups = new ArrayList<>();
    private TransactionViewModel transactionViewModel;
    private TransactionGroupAdapter transactionGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction);
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
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionGroupAdapter = new TransactionGroupAdapter(this, transactionGroups);
        transactionViewModel.loadTransactions();
        binding.setLifecycleOwner(this);
        binding.setTransactionViewModel(transactionViewModel);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvTransactionGroup;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(transactionGroupAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
    }

    private void observer() {
        transactionViewModel.getTransactions().observe(this, transactionWithCategoryAndAccounts -> {
            if (transactionWithCategoryAndAccounts == null || transactionWithCategoryAndAccounts.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
                transactionGroupAdapter.setData(new ArrayList<>());
            } else {
                long totalIncome = transactionWithCategoryAndAccounts.stream()
                        .filter(transaction -> transaction.transaction.getType())
                        .mapToLong(transaction -> transaction.transaction.getAmount())
                        .sum();

                long totalExpense = transactionWithCategoryAndAccounts.stream()
                        .filter(transaction -> !transaction.transaction.getType())
                        .mapToLong(transaction -> transaction.transaction.getAmount())
                        .sum();

                Map<LocalDate, List<TransactionWithCategoryAndAccount>> groupedTransactions = transactionWithCategoryAndAccounts.stream()
                        .collect(Collectors.groupingBy(transaction -> transaction.transaction.getDate()));

                List<TransactionGroup> transactionGroups = groupedTransactions.entrySet().stream()
                        .map(entry -> new TransactionGroup(entry.getKey(), entry.getValue()))
                        .sorted((group1, group2) -> group2.date.compareTo(group1.date))
                        .collect(Collectors.toList());
                transactionGroupAdapter.setData(transactionGroups);

                NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
                binding.tvTotalIncome.setText(String.format("%s %s", format.format(totalIncome), getString(R.string.vi_currency)));
                binding.tvTotalExpense.setText(String.format("%s %s", format.format(totalExpense), getString(R.string.vi_currency)));
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });

    }
}