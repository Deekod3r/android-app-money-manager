package com.project.hucemoney.views.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.FragmentTransactionBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.views.activities.ListAccountActivity;
import com.project.hucemoney.views.activities.ListCategoryActivity;
import com.project.hucemoney.views.activities.TransactionActivity;

import java.time.LocalDate;

public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private boolean type = Constants.TYPE_EXPENSE;
    private ActivityResultLauncher<Intent> mLauncher;


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
        if (getArguments() != null) {
        }
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
        binding = null;
    }

    private void init() {
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
                            if (data.getBooleanExtra("isCategory",false)) {
                                Category category = data.getParcelableExtra("categorySelected");
                                binding.edtCategory.setText(category.getName());
                            } else if (data.getBooleanExtra("isAccount",false)) {
                                Account account = data.getParcelableExtra("accountSelected");
                                binding.edtAccount.setText(account.getName());
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void controlAction() {

        binding.btnHistory.setOnClickListener(v -> {
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
                        //accountViewModel.getAccountAddRequest().setAmount(Long.parseLong(s.toString()));
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
                switch (position) {
                    case 0:
                        type = Constants.TYPE_EXPENSE;
                        binding.edtName.setHint("Tên khoản chi");
                        binding.edtGoal.setEnabled(false);
                        binding.edtGoal.setVisibility(View.GONE);
                        binding.edtGoal.setTag(null);
                        binding.edtGoal.setText(null);
                        binding.tvCurrency.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        break;
                    case 1:
                        type = Constants.TYPE_INCOME;
                        binding.edtName.setHint("Tên khoản thu");
                        binding.edtGoal.setEnabled(true);
                        binding.edtGoal.setVisibility(View.VISIBLE);
                        binding.tvCurrency.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    default:
                        break;
                }
                //binding.edtCategory.setText(null);
                //binding.edtCategory.setTag(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        binding.edtCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListCategoryActivity.class);
            intent.putExtra("type", type);
            mLauncher.launch(intent);
        });
        
        binding.edtAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListAccountActivity.class);
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
                LocalDate localDate = LocalDate.parse(s.toString(), Constants.DATE_FORMATTER);
                //transactionViewModel.getTransactionAddRequest().setDate(localDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}