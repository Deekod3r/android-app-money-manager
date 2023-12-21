package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.project.hucemoney.R;
import com.project.hucemoney.adapters.custom.TimeTransactionAdapter;
import com.project.hucemoney.databinding.FragmentYearTransactionBinding;
import com.project.hucemoney.entities.pojo.TimeSummary;
import com.project.hucemoney.viewmodels.TransactionViewModel;
import com.project.hucemoney.views.activities.TimeStatisticActivity;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class YearTransactionFragment extends Fragment {

    private FragmentYearTransactionBinding binding;
    private TransactionViewModel transactionViewModel;
    private TimeTransactionAdapter timeTransactionAdapter;
    private RecyclerView recyclerView;
    private List<TimeSummary> timeSummaries = new ArrayList<>();
    private NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
    private ArrayList<BarEntry> values = new ArrayList<>();

    public YearTransactionFragment() {
    }

    public static YearTransactionFragment newInstance(String param1, String param2) {
        YearTransactionFragment fragment = new YearTransactionFragment();
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
        binding = FragmentYearTransactionBinding.inflate(inflater, container, false);
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
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        timeTransactionAdapter = new TimeTransactionAdapter(getContext(), timeSummaries);
        binding.setTransactionViewModel(transactionViewModel);
        binding.setLifecycleOwner(this);
        transactionViewModel.setDate(LocalDate.now());
        transactionViewModel.summaryYears();
    }


    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvYearTransaction;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(timeTransactionAdapter);
    }

    private void controlAction() {
        timeTransactionAdapter.setOnItemClickListener(timeSummary -> {
            Intent intent = new Intent(getContext(), TimeStatisticActivity.class);
            intent.putExtra("time", timeSummary.getTime());
            intent.putExtra("type", 1);
            startActivity(intent);
        });
    }

    private void observer() {
        transactionViewModel.getSummaryYears().observe(getViewLifecycleOwner(), timeSummaries -> {
            timeTransactionAdapter.setData(timeSummaries);
            if (timeSummaries != null && !timeSummaries.isEmpty()) {
                binding.tvYearToYear.setText(String.format("%s - %s", timeSummaries.get(timeSummaries.size() - 1).getTime(), timeSummaries.get(0).getTime()));
                values.clear();
                for (int i = 0; i < timeSummaries.size(); i++) {
                    values.add(new BarEntry(Integer.parseInt(timeSummaries.get(i).getTime()), new float[]{timeSummaries.get(i).getIncome(), timeSummaries.get(i).getExpense()}));
                }
                BarDataSet barDataSet = new BarDataSet(values, "");
                barDataSet.setColors(Color.rgb(104, 241, 175), Color.rgb(164, 228, 251));
                barDataSet.setStackLabels(new String[]{"Thu", "Chi"});
                barDataSet.setDrawValues(false);
                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.4f);
                binding.barChartYearTransaction.getAxisRight().setDrawLabels(false);
                binding.barChartYearTransaction.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int roundedValue = Math.round(value);
                        if (Math.ceil(value) == value) {
                            return String.valueOf((int) value);
                        } else {
                            return "";
                        }
                    }
                });

                binding.barChartYearTransaction.getXAxis().setGranularityEnabled(false);
                binding.barChartYearTransaction.getXAxis().setGranularity(1f);
                binding.barChartYearTransaction.setData(barData);
                binding.barChartYearTransaction.getDescription().setText("(Đơn vị: đồng)");
                binding.barChartYearTransaction.getDescription().setYOffset(-10f);
                binding.barChartYearTransaction.setFitBars(true);
                binding.barChartYearTransaction.invalidate();
            } else {
                binding.barChartYearTransaction.clear();
            }
        });
    }
}