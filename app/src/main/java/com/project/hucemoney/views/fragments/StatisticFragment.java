package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.hucemoney.databinding.FragmentStatisticBinding;
import com.project.hucemoney.views.activities.AnalystFinanceActivity;
import com.project.hucemoney.views.activities.CurrentFinanceActivity;
import com.project.hucemoney.views.activities.DebtActivity;
import com.project.hucemoney.views.activities.TransactionStatisticActivity;
import com.project.hucemoney.views.activities.ExpenseActivity;
import com.project.hucemoney.views.activities.IncomeActivity;

public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;

    public StatisticFragment() {
        // Required empty public constructor
    }

    public static StatisticFragment newInstance() {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding  = FragmentStatisticBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        controlAction();
    }

    private void init() {
    }

    private void controlAction() {
        binding.btnCurrentFinance.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CurrentFinanceActivity.class);
            startActivity(intent);
        });

        binding.btnExIn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TransactionStatisticActivity.class);
            startActivity(intent);
        });

        binding.btnExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExpenseActivity.class);
            startActivity(intent);
        });

        binding.btnIncome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), IncomeActivity.class);
            startActivity(intent);
        });

        binding.btnAnalystFinance.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnalystFinanceActivity.class);
            startActivity(intent);
        });

        binding.btnDebt.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DebtActivity.class);
            startActivity(intent);
        });
    }
}