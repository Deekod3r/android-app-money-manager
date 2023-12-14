package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.CategoryStatisticAdapter;
import com.project.hucemoney.databinding.FragmentCategoryStatisticBinding;
import com.project.hucemoney.entities.pojo.CategoryStatistic;
import com.project.hucemoney.viewmodels.TransactionViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CategoryStatisticFragment extends Fragment {

    private FragmentCategoryStatisticBinding binding;
    private TransactionViewModel transactionViewModel;
    private CategoryStatisticAdapter categoryStatisticAdapter;
    private RecyclerView recyclerView;
    private List<CategoryStatistic> categoryStatistics = new ArrayList<>();
    List<PieEntry> entries = new ArrayList<>();
    private boolean type;

    public CategoryStatisticFragment() {
        // Required empty public constructor
    }

    public CategoryStatisticFragment(boolean type) {
        this.type = type;
    }

    public static CategoryStatisticFragment newInstance(String param1, String param2) {
        CategoryStatisticFragment fragment = new CategoryStatisticFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentCategoryStatisticBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }

    private void init() {
        Intent intent = getActivity().getIntent();
        String time = intent.getStringExtra("time");
        int type = intent.getIntExtra("type", -1);
        if (this.type) {
            binding.tvTitle.setText("Tổng thu");
        } else {
            binding.tvTitle.setText("Tổng chi");
        }
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryStatisticAdapter = new CategoryStatisticAdapter(getContext(), categoryStatistics);
        binding.setLifecycleOwner(this);
        binding.setTransactionViewModel(transactionViewModel);
        transactionViewModel.transactionsWithCategory(time, type);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvCategoryStatistic;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(categoryStatisticAdapter);
    }

    private void controlAction() {
    }

    private void observer() {
        transactionViewModel.getTransactionsWithCategory().observe(getActivity(), categoryStatistics -> {
            if (categoryStatistics != null && categoryStatistics.size() > 0) {
                entries.clear();
                NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
                List<CategoryStatistic> income = new ArrayList<>();
                List<CategoryStatistic> expense = new ArrayList<>();
                long sumIncome = 0;
                long sumExpense = 0;
                for (CategoryStatistic categoryStatistic : categoryStatistics) {
                    if (categoryStatistic.isType()) {
                        income.add(categoryStatistic);
                        sumIncome += categoryStatistic.getAmount();
                    } else {
                        expense.add(categoryStatistic);
                        sumExpense += categoryStatistic.getAmount();
                    }
                    if (this.type == categoryStatistic.isType()) {
                        entries.add(new PieEntry(categoryStatistic.getAmount(), categoryStatistic.getName()));
                    }
                }
                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                dataSet.setValueTextColor(android.R.color.white);
                dataSet.setValueTextSize(12f);
                PieData pieData = new PieData(dataSet);
                binding.pieChart.setData(pieData);
                binding.pieChart.getDescription().setEnabled(false);
                binding.pieChart.setDrawEntryLabels(false);
                binding.pieChart.setHoleRadius(15f);
                binding.pieChart.setTransparentCircleRadius(10f);
                binding.pieChart.invalidate();
                if (type) {
                    binding.tvFinanceAmount.setText(String.format("%s %s", format.format(sumIncome), getString(R.string.vi_currency)));
                    categoryStatisticAdapter.setData(income);
                } else {
                    binding.tvFinanceAmount.setText(String.format("%s %s", format.format(sumExpense), getString(R.string.vi_currency)));
                    categoryStatisticAdapter.setData(expense);
                }
                if (entries.size() == 0) {
                    binding.pieChart.clear();
                }
            } else {
                binding.pieChart.clear();
            }
        });
    }

}