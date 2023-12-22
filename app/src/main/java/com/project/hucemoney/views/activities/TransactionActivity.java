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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.TransactionGroupAdapter;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.ActivityTransactionBinding;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.TransactionGroup;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.TransactionViewModel;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionActivity extends AppCompatActivity implements TransactionGroupAdapter.OnTransactionItemClickListener {

    private ActivityTransactionBinding binding;
    private List<TransactionGroup> transactionGroups = new ArrayList<>();
    private List<TransactionWithCategoryAndAccount> transactionWithCategoryAndAccounts = new ArrayList<>();
    private TransactionViewModel transactionViewModel;
    private TransactionGroupAdapter transactionGroupAdapter;
    private ActivityResultLauncher<Intent> mLauncher;
    private String key = "";
    private LocalDate startDate;
    private LocalDate endDate;

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
        Intent intent = getIntent();
        if (intent.hasExtra("categoryUUID")) {
            key = intent.getStringExtra("categoryUUID");
            binding.tvKeySearch.setText("Lọc theo danh mục: " + intent.getStringExtra("categoryName"));
        }
        if (intent.hasExtra("accountUUID")) {
            key = intent.getStringExtra("accountUUID");
            binding.tvKeySearch.setText("Lọc theo tài khoản: " + intent.getStringExtra("accountName"));
        }
        if (intent.hasExtra("budgetUUID")) {
            key = intent.getStringExtra("budgetUUID");
            binding.tvKeySearch.setText("Lọc theo hạn mức: " + intent.getStringExtra("budgetName"));
        }
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionGroupAdapter = new TransactionGroupAdapter(this, transactionGroups, this);
        transactionViewModel.loadTransactions(key);
        binding.setLifecycleOwner(this);
        binding.setTransactionViewModel(transactionViewModel);
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
                            switch (data.getStringExtra("action")) {
                                case Constants.ACTION_EDIT:
                                case Constants.ACTION_DELETE:
                                    transactionViewModel.loadTransactions(key);
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("TransactionActivity", "mLauncher: " + e.getMessage());
                    }
                }
        );
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

        binding.edtStartDate.setInputType(InputType.TYPE_NULL);
        binding.edtStartDate.setOnClickListener(v -> {
            FunctionUtils.showDialogDate(this, binding.edtStartDate);
        });
        binding.edtStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.edtEndDate.setInputType(InputType.TYPE_NULL);
        binding.edtEndDate.setOnClickListener(v -> {
            FunctionUtils.showDialogDate(this, binding.edtEndDate);
        });
        binding.edtEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnFilter.setOnClickListener(v -> {
            if (startDate == null || endDate == null) {
                Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startDate.isAfter(endDate)) {
                Toast.makeText(this, "Ngày bắt đầu không được lớn hơn ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }
            List<TransactionWithCategoryAndAccount> transactionWithCategoryAndAccountsFilter = transactionWithCategoryAndAccounts.stream()
                    .filter(transaction -> (transaction.transaction.getDate().isAfter(startDate) || transaction.transaction.getDate().isEqual(startDate))
                            && (transaction.transaction.getDate().isBefore(endDate) || transaction.transaction.getDate().isEqual(endDate)))
                    .collect(Collectors.toList());
            loadData(transactionWithCategoryAndAccountsFilter);
        });
    }

    private void observer() {
        transactionViewModel.getTransactions().observe(this, transactionWithCategoryAndAccounts -> {
            this.transactionWithCategoryAndAccounts = transactionWithCategoryAndAccounts;
            loadData(transactionWithCategoryAndAccounts);
        });

    }

    private void loadData(List<TransactionWithCategoryAndAccount> transactionWithCategoryAndAccounts) {
        long totalIncome = 0;
        long totalExpense = 0;
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        if (transactionWithCategoryAndAccounts == null || transactionWithCategoryAndAccounts.size() == 0) {
            binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            transactionGroupAdapter.setData(new ArrayList<>());
        } else {
            totalIncome = transactionWithCategoryAndAccounts.stream()
                    .filter(transaction -> transaction.transaction.getType())
                    .mapToLong(transaction -> transaction.transaction.getAmount())
                    .sum();

            totalExpense = transactionWithCategoryAndAccounts.stream()
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
            binding.tvNotifyNoData.setVisibility(View.GONE);
        }
        binding.tvTotalIncome.setText(String.format("%s %s", format.format(totalIncome), getString(R.string.vi_currency)));
        binding.tvTotalExpense.setText(String.format("%s %s", format.format(totalExpense), getString(R.string.vi_currency)));
    }

    @Override
    public void onTransactionItemClick(TransactionWithCategoryAndAccount transactionWithCategoryAndAccount) {
        Intent intent = new Intent(this, EditTransactionActivity.class);
        intent.putExtra("transaction", transactionWithCategoryAndAccount.transaction);
        intent.putExtra("category", transactionWithCategoryAndAccount.category);
        intent.putExtra("account", transactionWithCategoryAndAccount.account);
        mLauncher.launch(intent);
    }
}