package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.pojo.BudgetWithCategory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder>{

    private final List<BudgetWithCategory> budgets;
    private Context context;
    private BudgetAdapter.OnItemClickListener onItemClickListener;

    public BudgetAdapter(Context context, List<BudgetWithCategory> budgets) {
        this.budgets = budgets;
        this.context = context;
    }
    public BudgetAdapter(List<BudgetWithCategory> budgets, BudgetAdapter.OnItemClickListener onItemClickListener) {
        this.budgets = budgets;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public BudgetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new BudgetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.ViewHolder holder, int position) {
        BudgetWithCategory budgetWithCategory = budgets.get(position);
        Budget budget = budgetWithCategory.budget;
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.name.setText(budget.getName());
        holder.time.setText(String.format("%s - %s", budget.getStartDate().format(Constants.DATE_FORMATTER), budget.getEndDate().format(Constants.DATE_FORMATTER)));
        holder.limit.setText(String.format("%s %s", format.format(budget.getInitialLimit()), context.getString(R.string.vi_currency)));
        holder.budgetRemaining.setText(String.format("Còn lại: %s %s", format.format(budget.getInitialLimit() - budget.getCurrentBalance()), context.getString(R.string.vi_currency)));
        long dayDiffToStart = ChronoUnit.DAYS.between(LocalDate.now(), budget.getStartDate());
        if (dayDiffToStart > 0) {
            holder.timeRemaining.setText(String.format("Bắt đầu sau: %s ngày", dayDiffToStart));
        } else {
            long daysDifferenceToEnd = ChronoUnit.DAYS.between(LocalDate.now(), budget.getEndDate());
            if (daysDifferenceToEnd < 0) {
                holder.timeRemaining.setText("Đã kết thúc");
                holder.timeRemaining.setTextColor(context.getColor(R.color.red));
            } else {
                holder.timeRemaining.setText(String.format("Còn lại: %s ngày", daysDifferenceToEnd));
            }
        }
        int progress = (int) (budget.getCurrentBalance() * 100 / budget.getInitialLimit());
        holder.progressBar.setProgress(progress);
        if (budget.getCurrentBalance() > budget.getInitialLimit()) {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
            holder.budgetRemaining.setTextColor(context.getColor(R.color.red));
            holder.notifyBudget.setVisibility(View.VISIBLE);
            holder.notifyBudget.setTextColor(context.getColor(R.color.red));
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(budgetWithCategory, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (budgets != null && budgets.size() > 0)
            return budgets.size();
        else {
            return 0;
        }
    }

    public void setData(List<BudgetWithCategory> budgets) {
        this.budgets.clear();
        this.budgets.addAll(budgets);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, time, timeRemaining, limit, budgetRemaining, notifyBudget;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvBudgetName);
            time = itemView.findViewById(R.id.tvBudgetTime);
            timeRemaining = itemView.findViewById(R.id.tvBudgetTimeRemaining);
            limit = itemView.findViewById(R.id.tvBudgetLimit);
            budgetRemaining = itemView.findViewById(R.id.tvBudgetRemaining);
            progressBar = itemView.findViewById(R.id.progressBarBudget);
            notifyBudget = itemView.findViewById(R.id.tvNotifyBudget);
        }

    }
    public interface OnItemClickListener {
        void onItemClick(BudgetWithCategory budgetWithCategory, int position);
    }
    public void setOnItemClickListener(BudgetAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}