package com.project.hucemoney.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.BudgetAdapter;
import com.project.hucemoney.adapters.entities.GoalAdapter;
import com.project.hucemoney.databinding.ActivityBudgetBinding;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.viewmodels.BudgetViewModel;
import com.project.hucemoney.viewmodels.GoalViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private ActivityBudgetBinding binding;
    private BudgetViewModel budgetViewModel;
    private BudgetAdapter budgetAdapter;
    private RecyclerView recyclerView;
    private List<Budget> budgets = new ArrayList<>();
    private ActivityResultLauncher<Intent> mLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_budget);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        budgets.add(new Budget(
                "12345678-1234-1234-1234-123456789012",
                "budget123",
                "user123",
                "Monthly Expenses",
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2023, 11, 30),
                1000L,
                800L,
                "category123",
                "Monthly budget for expenses"
        ));
        budgets.add(new Budget(
                "12345678-1234-1234-1234-123456789012",
                "budget123",
                "user123",
                "Monthly Expenses",
                LocalDate.of(2022, 11, 1),
                LocalDate.of(2023, 11, 30),
                1000L,
                800L,
                "category123",
                "Monthly budget for expenses"
        ));
        budgets.add(new Budget(
                "12345678-1234-1234-1234-123456789012",
                "budget123",
                "user123",
                "Monthly Expenses",
                LocalDate.of(2021, 11, 1),
                LocalDate.of(2023, 10, 30),
                1000L,
                800L,
                "category123",
                "Monthly budget for expenses"
        ));
        budgets.add(new Budget(
                "12345678-1234-1234-1234-123456789012",
                "budget123",
                "user123",
                "Monthly Expenses",
                LocalDate.of(2020, 11, 1),
                LocalDate.of(2023, 9, 30),
                1000L,
                1100L,
                "category123",
                "Monthly budget for expenses"
        ));
        budgetAdapter = new BudgetAdapter(this, budgets);
        binding.setBudgetViewModel(budgetViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvBudget;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(budgetAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.btnAddBudget.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBudgetActivity.class);
            startActivity(intent);
        });
    }

    private void observer() {
    }
}