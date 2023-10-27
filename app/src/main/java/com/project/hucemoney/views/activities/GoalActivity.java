package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.GoalAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal);
        init();
        initRecyclerView();
        controlAction();
        observe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goals.add(new Goal("1","1","1","Goal 1", Long.parseLong("5000000000"), Long.parseLong("1000000000"), LocalDate.now(),LocalDate.now(),""));
        goals.add(new Goal("2","2","2","Goal 2", Long.parseLong("4000000000"), Long.parseLong("1000000000"), LocalDate.now(),LocalDate.now(),""));
        goals.add(new Goal("3","3","3","Goal 3", Long.parseLong("3000000000"), Long.parseLong("1000000000"), LocalDate.now(),LocalDate.now(),""));
        goals.add(new Goal("4","4","4","Goal 4", Long.parseLong("2000000000"), Long.parseLong("1000000000"), LocalDate.now(),LocalDate.now(),""));
        goals.add(new Goal("5","5","5","Goal 5", Long.parseLong("1000000000"), Long.parseLong("1000000000"), LocalDate.now(),LocalDate.now(),""));
        goalAdapter = new GoalAdapter(this, goals);
        binding.setGoalViewModel(goalViewModel);
        binding.setLifecycleOwner(this);
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
    }

    private void observe() {
    }
}