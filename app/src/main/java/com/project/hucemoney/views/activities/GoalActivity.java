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
import android.view.View;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.GoalAdapter;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.ActivityGoalBinding;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.viewmodels.GoalViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalActivity extends AppCompatActivity {

    private ActivityGoalBinding binding;
    private GoalViewModel goalViewModel;
    private GoalAdapter goalAdapter;
    private RecyclerView recyclerView;
    private List<Goal> goals = new ArrayList<>();
    private ActivityResultLauncher<Intent> mLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        goalViewModel.getGoals().removeObservers(this);
    }

    private void init() {
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalAdapter = new GoalAdapter(this, goals);
        goalViewModel.loadGoals();
        binding.setGoalViewModel(goalViewModel);
        binding.setLifecycleOwner(this);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String action = data.getStringExtra("action");
                            if (action == null) {
                                Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            switch (action) {
                                case Constants.ACTION_ADD: {
                                    Goal goal = data.getParcelableExtra("goalAdded");
                                    goalViewModel.addGoalLiveData(goal);
                                    break;
                                }
                                case Constants.ACTION_EDIT: {
                                    Goal goal = data.getParcelableExtra("goalEdited");
                                    int position = data.getIntExtra("position", -1);
                                    goalViewModel.editGoalLiveData(goal, position);
                                    break;
                                }
                                case Constants.ACTION_DELETE: {
                                    int position = data.getIntExtra("position", -1);
                                    goalViewModel.deleteGoalLiveData(position);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvGoal;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(goalAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.btnAddGoal.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddGoalActivity.class);
            mLauncher.launch(intent);
        });
        goalAdapter.setOnItemClickListener((goal, position) -> {
            Intent intent = new Intent(this, EditGoalActivity.class);
            intent.putExtra("goal", goal);
            intent.putExtra("position", position);
            mLauncher.launch(intent);
        });
    }

    private void observer() {
        goalViewModel.getGoals().observe(this, goals -> {
            goalAdapter.setData(goals);
            int ongoingCount = 0;
            int completedCount = 0;
            int overdueCount = 0;
            if (goals == null || goals.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvNotifyNoData.setVisibility(View.GONE);
                for (Goal goal : goals) {
                    if (goal.getCurrentAmount() == goal.getTargetAmount()) {
                        completedCount++;
                    } else if (LocalDate.now().isAfter(goal.getEndDate())) {
                        overdueCount++;
                    } else {
                        ongoingCount++;
                    }
                }
            }
            binding.tvOngoingCount.setText(String.valueOf(ongoingCount));
            binding.tvCompletedCount.setText(String.valueOf(completedCount));
            binding.tvOverdueCount.setText(String.valueOf(overdueCount));
        });
    }

}