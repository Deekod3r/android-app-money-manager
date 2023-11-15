package com.project.hucemoney.views.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.FragmentTransactionBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.TransactionViewModel;
import com.project.hucemoney.views.activities.ListAccountActivity;
import com.project.hucemoney.views.activities.ListCategoryActivity;
import com.project.hucemoney.views.activities.TransactionActivity;

import java.time.LocalDate;

public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private TransactionViewModel transactionViewModel;
    private boolean type = Constants.TYPE_EXPENSE;
    private ActivityResultLauncher<Intent> mLauncher;
    private Category category;
    private Account account;

    public TransactionFragment() {
    }

    public static TransactionFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();
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
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.item_spinner_transaction, getResources().getTextArray(R.array.spinner_transaction_items)) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText(String.format("%s", getItem(position)));
                if (position == binding.spinnerTransactionType.getSelectedItemPosition()) {
                    textView.setTypeface(null, Typeface.BOLD);
                }
                return view;
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTransactionType.setAdapter(adapter);
        init();
        controlAction();
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
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            if (data.getBooleanExtra("isCategory",false)) {
                                category = data.getParcelableExtra("categorySelected");
                                assert category != null;
                                binding.edtCategory.setText(category.getName());
                                transactionViewModel.getTransactionAddRequest().setCategory(category.getUUID());
                            } else if (data.getBooleanExtra("isAccount",false)) {
                                account = data.getParcelableExtra("accountSelected");
                                assert account != null;
                                binding.edtAccount.setText(account.getName());
                                transactionViewModel.getTransactionAddRequest().setAccount(account.getUUID());
                            }
                        }
                    } catch (Exception e) {
                        Log.e("EditTransactionActivity", "mLauncher: " + e.getMessage());
                    }
                }
        );
    }

    private void controlAction() {

        binding.btnHistory.setOnClickListener(v -> {
            binding.btnHistory.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            Intent intent = new Intent(getContext(), TransactionActivity.class);
            startActivity(intent);
        });

        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtAmount.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtAmount.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        transactionViewModel.getTransactionAddRequest().setAmount(Long.parseLong(s.toString()));
                    }
                }
                binding.edtAmount.setSelection(binding.edtAmount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.edtCategory.setText(null);
                switch (position) {
                    case 0:
                        type = Constants.TYPE_EXPENSE;
                        binding.edtName.setHint("Tên khoản chi");
                        binding.edtGoal.setEnabled(false);
                        binding.edtGoal.setVisibility(View.GONE);
                        binding.edtGoal.setTag(null);
                        binding.edtGoal.setText(null);
                        binding.tvCurrency.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        transactionViewModel.getTransactionAddRequest().setCategory("");
                        break;
                    case 1:
                        type = Constants.TYPE_INCOME;
                        binding.edtName.setHint("Tên khoản thu");
                        binding.edtGoal.setEnabled(true);
                        binding.edtGoal.setVisibility(View.VISIBLE);
                        binding.tvCurrency.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                        transactionViewModel.getTransactionAddRequest().setCategory("");
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        binding.edtCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListCategoryActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("categorySelected", category);
            mLauncher.launch(intent);
        });
        
        binding.edtAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListAccountActivity.class);
            intent.putExtra("accountSelected", account);
            mLauncher.launch(intent);
        });
        
        binding.edtDate.setInputType(InputType.TYPE_NULL);
        binding.edtDate.setOnClickListener(v -> {
            FunctionUtils.showDialogDate(getContext(), binding.edtDate);
        });
        binding.edtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
                    transactionViewModel.getTransactionAddRequest().setDate(localDate);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            transactionViewModel.getTransactionAddRequest().setType(type);
            transactionViewModel.addTransaction();
            transactionViewModel.getResultAddTransaction().observe(getViewLifecycleOwner(), response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(getContext(),v);
                    binding.btnHistory.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    category = null;
                    binding.edtCategory.setText("");
                    binding.edtAccount.setText("");
                    binding.edtAmount.setText("0");
                    binding.edtDate.setText("");
                    binding.edtName.setText("");
                    binding.edtNote.setText("");
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    FunctionUtils.showDialogNotify(getContext(), "", response.getMessage(), DialogType.ERROR);
                }
                transactionViewModel.getResultAddTransaction().removeObservers(this);
            });
        });
    }

}