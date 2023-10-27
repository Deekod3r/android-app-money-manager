package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.views.fragments.BottomSheetAccountFragment;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private final List<Account> accounts;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AccountAdapter(Context context, List<Account> accounts) {
        this.accounts = accounts;
        this.context = context;
    }

    public AccountAdapter(List<Account> accounts, OnItemClickListener onItemClickListener) {
        this.accounts = accounts;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accounts.get(position);
        holder.name.setText(account.getName());
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.amount.setText(String.format("%s %s", format.format(account.getAmount()), context.getString(R.string.vi_currency)));
        holder.itemView.setOnClickListener(v -> {
        });
        holder.more.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(account, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (accounts != null && accounts.size() > 0)
            return accounts.size();
        else {
            return 0;
        }
    }

    public void setData(List<Account> accounts) {
        this.accounts.clear();
        this.accounts.addAll(accounts);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, amount;
        ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvAccountName);
            amount = itemView.findViewById(R.id.tvAccountAmount);
            more = itemView.findViewById(R.id.imgMoreActionAccount);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Account account, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
