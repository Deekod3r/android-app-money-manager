package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
    private boolean isMoreActive;
    private int positionSelected = -1;

    public AccountAdapter(Context context, List<Account> accounts, boolean isMoreActive) {
        this.accounts = accounts;
        this.context = context;
        this.isMoreActive = isMoreActive;
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
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.name.setText(account.getName());
        holder.amount.setText(String.format("%s %s", format.format(account.getAmount()), context.getString(R.string.vi_currency)));
        if(isMoreActive) {
            holder.more.setVisibility(View.VISIBLE);
        }
        if (positionSelected == position) {
            holder.more.setVisibility(View.VISIBLE);
            holder.more.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_check_circle_blue_24));
        }
        holder.more.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(account, position);
            }
        });
        if (!isMoreActive) {
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(account, position);
                }
            });
        }
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

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
        notifyItemChanged(positionSelected);
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
