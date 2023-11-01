package com.project.hucemoney.views.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private boolean type = Constants.TYPE_EXPENSE;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    }

    private void controlAction() {
        binding.spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        type = Constants.TYPE_EXPENSE;
                        binding.edtName.setHint("Tên khoản chi");
                        binding.edtGoal.setEnabled(false);
                        binding.edtGoal.setTag(null);
                        binding.edtGoal.setText(null);
                        binding.tvCurrency.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        break;
                    case 1:
                        type = Constants.TYPE_INCOME;
                        binding.edtName.setHint("Tên khoản thu");
                        binding.edtGoal.setEnabled(true);
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

    }

}