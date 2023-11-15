package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<TransactionWithCategoryAndAccount> transactions;
    private Context context;
    private TransactionAdapter.OnItemClickListener onItemClickListener;

    public TransactionAdapter(Context context, List<TransactionWithCategoryAndAccount> transactions) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        TransactionWithCategoryAndAccount transactionWithCategoryAndAccount = transactions.get(position);
        Transaction transaction = transactionWithCategoryAndAccount.transaction;
        holder.tvName.setText(transaction.getName());
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.ivType.setImageResource(transaction.getType() ? R.drawable.baseline_down_green_24 : R.drawable.baseline_up_red_24);
        holder.tvAmount.setText(String.format("%s %s", format.format(transaction.getAmount()), context.getString(R.string.vi_currency)));
        holder.tvAmount.setTextColor(transaction.getType() ? context.getColor(R.color.green) : context.getColor(R.color.red));
        holder.tvCategory.setText(transactionWithCategoryAndAccount.category.getName());
        holder.tvAccount.setText(transactionWithCategoryAndAccount.account.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(transactionWithCategoryAndAccount);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (transactions != null && transactions.size() > 0)
            return transactions.size();
        else {
            return 0;
        }
    }

    public void setData(List<TransactionWithCategoryAndAccount> transactions) {
        this.transactions.clear();
        this.transactions.addAll(transactions);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount, tvCategory, tvAccount;
        ImageView ivType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivType = itemView.findViewById(R.id.ivTransactionType);
            tvName = itemView.findViewById(R.id.tvTransactionName);
            tvAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvCategory = itemView.findViewById(R.id.tvTransactionCategory);
            tvAccount = itemView.findViewById(R.id.tvTransactionAccount);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TransactionWithCategoryAndAccount transaction);
    }

    public void setOnItemClickListener(TransactionAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
