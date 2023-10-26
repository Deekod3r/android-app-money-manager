package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.adapters.entities.AccountAdapter;
import com.project.hucemoney.databinding.FragmentAccountBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.views.activities.AddAccountActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private List<Account> accounts = new ArrayList<>();
    private AccountAdapter accountAdapter;
    private RecyclerView recyclerView;
    private AccountViewModel accountViewModel;
    private ActivityResultLauncher<Intent> mLauncher;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initRecyclerView();
        controlAction();
        observe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountAdapter = new AccountAdapter(getContext(), accounts);
        accountViewModel.loadAccounts();
        binding.setAccountViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvAccount;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(accountAdapter);
    }

    private void controlAction() {

        binding.btnAddAccount.setOnClickListener(v -> {
            if (accounts.size() > 4) {
                Toast.makeText(getContext(), "Số lượng tài khoản đã đạt giới hạn", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), AddAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fillData() {
    }

    private void observe() {
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            accountAdapter.setData(accounts);
            if (accounts == null || accounts.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
        accountViewModel.getResultEditAccount().observe(getViewLifecycleOwner(), response -> {
//            if (response != null) {
//                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
//            }
        });
        accountViewModel.getAmountTotal().observe(getViewLifecycleOwner(), s -> binding.tvTotalAmount.setText("Tổng tiền: " + s));
    }

}