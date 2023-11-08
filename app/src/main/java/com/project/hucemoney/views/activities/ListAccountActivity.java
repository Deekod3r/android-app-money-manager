package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.AccountAdapter;
import com.project.hucemoney.adapters.entities.CategoryAdapter;
import com.project.hucemoney.databinding.ActivityListAccountBinding;
import com.project.hucemoney.databinding.ActivityListCategoryBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.viewmodels.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListAccountActivity extends AppCompatActivity {

    private ActivityListAccountBinding binding;
    private AccountViewModel accountViewModel;
    private AccountAdapter accountAdapter;
    private RecyclerView recyclerView;
    private List<Account> accounts = new ArrayList<>();
    private Account accountSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_account);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        accountViewModel.getAccounts().removeObservers(this);
    }

    private void init() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountAdapter = new AccountAdapter(this, accounts, false);
        accountViewModel.loadAccounts();
        binding.setAccountViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initRecyclerView() {
        recyclerView = binding.rvListAccount;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(accountAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        accountAdapter.setOnItemClickListener((account, position) -> {
            Intent intent = new Intent();
            intent.putExtra("accountSelected", account);
            intent.putExtra("position", position);
            intent.putExtra("isAccount", true);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void observer() {
        accountViewModel.getAccounts().observe(this, accounts -> {
            accountAdapter.setData(accounts);
            if (accounts == null || accounts.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                Intent intent = getIntent();
                if (intent != null) {
                    accountSelected = intent.getParcelableExtra("accountSelected");
                    if (accountSelected != null) {
                        for (Account account : accounts) {
                            if (account.getUUID().equals(accountSelected.getUUID())) {
                                accountAdapter.setPositionSelected(accounts.indexOf(account));
                                break;
                            }
                        }
                    }
                }
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
    }

}