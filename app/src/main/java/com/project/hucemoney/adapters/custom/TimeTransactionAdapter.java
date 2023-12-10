package com.project.hucemoney.adapters.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.entities.pojo.TimeSummary;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TimeTransactionAdapter extends RecyclerView.Adapter<TimeTransactionAdapter.ViewHolder> {

    private final List<TimeSummary> timeSummaries;
    private Context context;
    private TimeTransactionAdapter.OnItemClickListener onItemClickListener;

    public TimeTransactionAdapter(Context context, List<TimeSummary> timeSummaries) {
        this.timeSummaries = timeSummaries;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_transaction, parent, false);
        return new TimeTransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTransactionAdapter.ViewHolder holder, int position) {
        TimeSummary timeSummary = timeSummaries.get(position);
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.time.setText(timeSummary.getTime());
        holder.income.setText(String.format("%s %s", format.format(timeSummary.getIncome()), context.getString(R.string.vi_currency)));
        holder.expense.setText(String.format("%s %s", format.format(timeSummary.getExpense()), context.getString(R.string.vi_currency)));
        long sum = timeSummary.getIncome() - timeSummary.getExpense();
        holder.sum.setText(format.format(sum));
        if (sum > 0) {
            holder.sum.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.sum.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        if (timeSummaries != null && timeSummaries.size() > 0)
            return timeSummaries.size();
        else {
            return 0;
        }
    }

    public void setData(List<TimeSummary> timeSummaries) {
        this.timeSummaries.clear();
        this.timeSummaries.addAll(timeSummaries);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView time, income, expense, sum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tvTime);
            income = itemView.findViewById(R.id.tvSumIncome);
            expense = itemView.findViewById(R.id.tvSumExpense);
            sum = itemView.findViewById(R.id.tvSum);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(TimeSummary timeSummary, int position);
    }

    public void setOnItemClickListener(TimeTransactionAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
