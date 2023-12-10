package com.project.hucemoney.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.databinding.FragmentCurrentTransactionBinding;
import com.project.hucemoney.entities.pojo.TimeSummary;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.TransactionViewModel;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class CurrentTransactionFragment extends Fragment {
    
    private FragmentCurrentTransactionBinding binding;
    private TransactionViewModel transactionViewModel;
    private NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

    public CurrentTransactionFragment() {
    }
    
    public static CurrentTransactionFragment newInstance(String param1, String param2) {
        CurrentTransactionFragment fragment = new CurrentTransactionFragment();
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
        binding= FragmentCurrentTransactionBinding.inflate(inflater, container, false);
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
        binding.setTransactionViewModel(transactionViewModel);
        binding.setLifecycleOwner(this);
        binding.edtYear.setText(String.valueOf(LocalDate.now().getYear()));
        transactionViewModel.setDate(LocalDate.now());
        transactionViewModel.summaryDate();
        transactionViewModel.summaryMonth();
        transactionViewModel.summaryYear();
    }
    
    private void initRecyclerView() {
        
    }
    
    private void controlAction() {

    }
    
    private void observer() {
        transactionViewModel.getSummaryDate().observe(getViewLifecycleOwner(), response -> {
            if (response != null && Objects.equals(response.getStatus(), ResponseCode.SUCCESS)) {
                TimeSummary timeSummary = response.getData();
                binding.tvToday.setText(String.format("Hôm nay (%s)", timeSummary.getTime()));
                binding.tvSumIncomeToday.setText(String.format("%s %s", format.format(timeSummary.getIncome()), getContext().getString(R.string.vi_currency)));
                binding.tvSumExpenseToday.setText(String.format("%s %s", format.format(timeSummary.getExpense()), getContext().getString(R.string.vi_currency)));
                long sum = timeSummary.getIncome() - timeSummary.getExpense();
                binding.tvSumToday.setText(String.format("%s %s", format.format(sum), getContext().getString(R.string.vi_currency)));
                if (sum < 0) {
                    binding.tvSumToday.setTextColor(getResources().getColor(R.color.red));
                } else {
                    binding.tvSumToday.setTextColor(getResources().getColor(R.color.green));
                }
            }
            transactionViewModel.getSummaryDate().removeObservers(getViewLifecycleOwner());
        });

        transactionViewModel.getSummaryMonth().observe(getViewLifecycleOwner(), response -> {
            if (response != null && Objects.equals(response.getStatus(), ResponseCode.SUCCESS)) {
                TimeSummary timeSummary = response.getData();
                binding.tvThisMonth.setText(String.format("Tháng này (%s)", timeSummary.getTime()));
                binding.tvSumIncomeThisMonth.setText(String.format("%s %s", format.format(timeSummary.getIncome()), getContext().getString(R.string.vi_currency)));
                binding.tvSumExpenseThisMonth.setText(String.format("%s %s", format.format(timeSummary.getExpense()), getContext().getString(R.string.vi_currency)));
                long sum = timeSummary.getIncome() - timeSummary.getExpense();
                binding.tvSumThisMonth.setText(String.format("%s %s", format.format(sum), getContext().getString(R.string.vi_currency)));
                if (sum < 0) {
                    binding.tvSumThisMonth.setTextColor(getResources().getColor(R.color.red));
                } else {
                    binding.tvSumThisMonth.setTextColor(getResources().getColor(R.color.green));
                }
            }
            transactionViewModel.getSummaryMonth().removeObservers(getViewLifecycleOwner());
        });

        transactionViewModel.getSummaryYear().observe(getViewLifecycleOwner(), response -> {
            if (response != null && Objects.equals(response.getStatus(), ResponseCode.SUCCESS)) {
                TimeSummary timeSummary = response.getData();
                binding.tvThisYear.setText(String.format("Năm nay (%s)", timeSummary.getTime()));
                binding.tvSumIncomeThisYear.setText(String.format("%s %s", format.format(timeSummary.getIncome()), getContext().getString(R.string.vi_currency)));
                binding.tvSumExpenseThisYear.setText(String.format("%s %s", format.format(timeSummary.getExpense()), getContext().getString(R.string.vi_currency)));
                long sum = timeSummary.getIncome() - timeSummary.getExpense();
                binding.tvSumThisYear.setText(String.format("%s %s", format.format(sum), getContext().getString(R.string.vi_currency)));
                if (sum < 0) {
                    binding.tvSumThisYear.setTextColor(getResources().getColor(R.color.red));
                } else {
                    binding.tvSumThisYear.setTextColor(getResources().getColor(R.color.green));
                }
            }
            transactionViewModel.getSummaryYear().removeObservers(getViewLifecycleOwner());
        });
    }
}