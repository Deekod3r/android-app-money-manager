package com.project.hucemoney.views.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.adapters.entities.AccountAdapter;
import com.project.hucemoney.common.ResponseCode;
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

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Account account = data.getParcelableExtra("accountAdded");
                            accountViewModel.addAccountLiveData(account);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
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
        observer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }

    private void init() {
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        accountAdapter = new AccountAdapter(getContext(), accounts, true);
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
                Toast.makeText(getContext(), "Số tài khoản đã đạt giới hạn", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), AddAccountActivity.class);
                mLauncher.launch(intent);
            }
        });

        accountAdapter.setOnItemClickListener((account, position) -> {
            BottomSheetAccountFragment fragment = BottomSheetAccountFragment.newInstance(account, position);
            fragment.show(getParentFragmentManager(), fragment.getTag());
        });
    }

    private void observer() {
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            accountAdapter.setData(accounts);
            if (accounts == null || accounts.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
        accountViewModel.getAmountTotal().observe(getViewLifecycleOwner(), s -> binding.tvTotalAmount.setText(String.format("Tổng tiền: %s", s)));
    }

}