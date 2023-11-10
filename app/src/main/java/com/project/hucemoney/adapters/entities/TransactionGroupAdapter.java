package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.TransactionGroup;

import java.text.NumberFormat;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.ViewHolder> {

    private final List<TransactionGroup> transactionGroups;
    private Context context;
    private TransactionGroupAdapter.OnItemClickListener onItemClickListener;

    public TransactionGroupAdapter(Context context, List<TransactionGroup> transactionGroups) {
        this.transactionGroups = transactionGroups;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_group, parent, false);
        return new TransactionGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionGroupAdapter.ViewHolder holder, int position) {
        TransactionGroup transactionGroup = transactionGroups.get(position);
        TransactionAdapter transactionAdapter = new TransactionAdapter(context, transactionGroup.transactions);
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.rvTransaction.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvTransaction.setAdapter(transactionAdapter);
        holder.dayOfWeek.setText(transactionGroup.date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("vi", "VN")));
        holder.dayOfMonth.setText(String.valueOf(transactionGroup.date.getDayOfMonth()));
        holder.monthAndYear.setText(String.format(Locale.getDefault(), "%s/%d", transactionGroup.date.getMonthValue(), transactionGroup.date.getYear()));
        long sumIncome = transactionGroup.transactions.stream()
                .filter(transaction -> transaction.transaction.getType())
                .mapToLong(transaction -> transaction.transaction.getAmount())
                .sum();
        long sumExpense = transactionGroup.transactions.stream()
                .filter(transaction -> !transaction.transaction.getType())
                .mapToLong(transaction -> transaction.transaction.getAmount())
                .sum();

        holder.sumIncome.setText(String.format("%s %s", format.format(sumIncome), context.getString(R.string.vi_currency)));
        holder.sumExpense.setText(String.format("%s %s", format.format(sumExpense), context.getString(R.string.vi_currency)));

    }

    @Override
    public int getItemCount() {
        if (transactionGroups != null && transactionGroups.size() > 0)
            return transactionGroups.size();
        else {
            return 0;
        }
    }

    public void setData(List<TransactionGroup> transactionGroups) {
        this.transactionGroups.clear();
        this.transactionGroups.addAll(transactionGroups);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfMonth, dayOfWeek, monthAndYear, sumIncome, sumExpense;
        RecyclerView rvTransaction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.tvDayOfMonth);
            dayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            monthAndYear = itemView.findViewById(R.id.tvMonthAndYear);
            sumIncome = itemView.findViewById(R.id.tvSumIncome);
            sumExpense = itemView.findViewById(R.id.tvSumExpense);
            rvTransaction = itemView.findViewById(R.id.rvTransaction);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TransactionGroup transactionGroup, int position);
    }

    public void setOnItemClickListener(TransactionGroupAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}

