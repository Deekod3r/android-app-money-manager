package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.project.hucemoney.adapters.custom.TimeTransactionAdapter;
import com.project.hucemoney.databinding.FragmentMonthTransactionBinding;
import com.project.hucemoney.entities.pojo.TimeSummary;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.TransactionViewModel;
import com.project.hucemoney.views.activities.TimeStatisticActivity;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonthTransactionFragment extends Fragment {

    private FragmentMonthTransactionBinding binding;
    private TransactionViewModel transactionViewModel;
    private TimeTransactionAdapter timeTransactionAdapter;
    private RecyclerView recyclerView;
    private List<TimeSummary> timeSummaries = new ArrayList<>();
    private NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
    private ArrayList<BarEntry> values = new ArrayList<>();
    public MonthTransactionFragment() {
    }

    public static MonthTransactionFragment newInstance(String param1, String param2) {
        MonthTransactionFragment fragment = new MonthTransactionFragment();
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
        binding = FragmentMonthTransactionBinding.inflate(inflater, container, false);
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
        transactionViewModel.getSummaryMonths().removeObservers(this);
    }

    private void init() {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        timeTransactionAdapter = new TimeTransactionAdapter(getContext(), timeSummaries);
        binding.setTransactionViewModel(transactionViewModel);
        binding.setLifecycleOwner(this);
        binding.edtYear.setText(LocalDate.now().getYear() + "");
        transactionViewModel.setDate(LocalDate.now());
        transactionViewModel.summaryMonths();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvMonthTransaction;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(timeTransactionAdapter);
    }

    private void controlAction() {
        binding.edtYear.setOnClickListener(v -> {
            FunctionUtils.showDialogYear(getContext(), binding.edtYear);
        });
        binding.edtYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    LocalDate localDate = LocalDate.of(Integer.parseInt(s.toString()), 1, 1);
                    transactionViewModel.setDate(localDate);
                    transactionViewModel.summaryMonths();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        timeTransactionAdapter.setOnItemClickListener(timeSummary -> {
            Intent intent = new Intent(getContext(), TimeStatisticActivity.class);
            intent.putExtra("time", timeSummary.getTime());
            intent.putExtra("type", 2);
            startActivity(intent);
        });
    }

    private void observer() {
        transactionViewModel.getSummaryMonths().observe(getViewLifecycleOwner(), timeSummaries -> {
            timeTransactionAdapter.setData(timeSummaries);
            if (timeSummaries != null && timeSummaries.size() > 0) {
                values.clear();
                for (int i = 0; i < timeSummaries.size(); i++) {
                    values.add(new BarEntry(Integer.parseInt(timeSummaries.get(i).getTime().substring(5)), new float[]{timeSummaries.get(i).getIncome(), timeSummaries.get(i).getExpense()}));
                }
                BarDataSet barDataSet = new BarDataSet(values, "");
                barDataSet.setColors(Color.rgb(104, 241, 175), Color.rgb(164, 228, 251));
                barDataSet.setStackLabels(new String[]{"Thu", "Chi"});
                barDataSet.setDrawValues(false);
                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.4f);
                binding.barChartMonthTransaction.getAxisRight().setDrawLabels(false);
                binding.barChartMonthTransaction.getXAxis().setValueFormatter(new ValueFormatter() {
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

                binding.barChartMonthTransaction.getXAxis().setGranularityEnabled(false);
                binding.barChartMonthTransaction.getXAxis().setGranularity(1f);
                binding.barChartMonthTransaction.setData(barData);
                binding.barChartMonthTransaction.getDescription().setText("(Đơn vị: đồng)");
                binding.barChartMonthTransaction.getDescription().setYOffset(-10f);
                binding.barChartMonthTransaction.setFitBars(true);
                binding.barChartMonthTransaction.invalidate();
            } else {
                binding.barChartMonthTransaction.clear();
            }
        });
    }
}