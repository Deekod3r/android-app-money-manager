package com.project.hucemoney.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.BudgetAdapter;
import com.project.hucemoney.adapters.entities.GoalAdapter;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.databinding.ActivityBudgetBinding;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.entities.pojo.BudgetWithCategory;
import com.project.hucemoney.viewmodels.BudgetViewModel;
import com.project.hucemoney.viewmodels.GoalViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BudgetActivity extends AppCompatActivity {

    private ActivityBudgetBinding binding;
    private BudgetViewModel budgetViewModel;
    private BudgetAdapter budgetAdapter;
    private RecyclerView recyclerView;
    private List<BudgetWithCategory> budgets = new ArrayList<>();
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
        binding.unbind();
        budgetViewModel.getBudgets().removeObservers(this);
    }

    private void init() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetAdapter = new BudgetAdapter(this, budgets);
        budgetViewModel.loadBudgets();
        binding.setBudgetViewModel(budgetViewModel);
        binding.setLifecycleOwner(this);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            String action = data.getStringExtra("action");
                            switch (Objects.requireNonNull(action)) {
                                case Constants.ACTION_ADD: {
                                    Budget budget = data.getParcelableExtra("budgetAdded");
                                    Category category = data.getParcelableExtra("category");
                                    budgetViewModel.addBudgetLiveData(BudgetWithCategory.of(budget, category));
                                    break;
                                }
                                case Constants.ACTION_EDIT: {
                                    Budget budget = data.getParcelableExtra("budgetEdited");
                                    Category category = data.getParcelableExtra("category");
                                    int position = data.getIntExtra("position", -1);
                                    budgetViewModel.editBudgetLiveData(BudgetWithCategory.of(budget, category), position);
                                    break;
                                }
                                case Constants.ACTION_DELETE: {
                                    int position = data.getIntExtra("position", -1);
                                    budgetViewModel.deleteBudgetLiveData(position);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("BudgetActivity", "mLauncher: " + e.getMessage());
                    }
                }
        );
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
            mLauncher.launch(intent);
        });
        budgetAdapter.setOnItemClickListener((budgetWithCategory, position) -> {
            Intent intent = new Intent(this, EditBudgetActivity.class);
            intent.putExtra("budget", budgetWithCategory.budget);
            intent.putExtra("category", budgetWithCategory.category);
            intent.putExtra("position", position);
            mLauncher.launch(intent);
        });
    }

    private void observer() {
        budgetViewModel.getBudgets().observe(this, budgets -> {
            budgetAdapter.setData(budgets);
            if (budgets == null || budgets.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
    }
}