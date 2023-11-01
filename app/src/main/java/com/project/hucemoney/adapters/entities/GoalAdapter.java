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
import com.project.hucemoney.entities.Goal;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private final List<Goal> goals;
    private Context context;
    private GoalAdapter.OnItemClickListener onItemClickListener;

    public GoalAdapter(Context context, List<Goal> goals) {
        this.goals = goals;
        this.context = context;
    }

    public GoalAdapter(List<Goal> goals, GoalAdapter.OnItemClickListener onItemClickListener) {
        this.goals = goals;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public GoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        return new GoalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.ViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.name.setText(goal.getName());
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.target.setText(String.format("%s %s", format.format(goal.getTargetAmount()), context.getString(R.string.vi_currency)));
        holder.current.setText(String.format("Đã đạt: %s %s", format.format(goal.getCurrentAmount()), context.getString(R.string.vi_currency)));
        holder.time.setText(String.format("%s - %s", goal.getStartDate().format(Constants.DATE_FORMATTER), goal.getEndDate().format(Constants.DATE_FORMATTER)));
        long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndDate());
        if (daysDifference < 0) {
            holder.timeRemaining.setText("Đã kết thúc");
            holder.timeRemaining.setTextColor(context.getColor(R.color.red));
        } else {
            holder.timeRemaining.setText(String.format("Còn lại: %s ngày", daysDifference));
        }
        int progress = (int) (goal.getCurrentAmount() * 100 / goal.getTargetAmount());
        holder.progressBar.setProgress(progress);
        if (LocalDate.now().isAfter(goal.getEndDate()) && progress < 100) {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
        } else if (progress < 100) {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(goal, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (goals != null && goals.size() > 0)
            return goals.size();
        else {
            return 0;
        }
    }

    public void setData(List<Goal> goals) {
        this.goals.clear();
        this.goals.addAll(goals);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, target, current, time, timeRemaining;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvGoalName);
            target = itemView.findViewById(R.id.tvGoalTarget);
            current = itemView.findViewById(R.id.tvGoalCurrent);
            time = itemView.findViewById(R.id.tvGoalTime);
            timeRemaining = itemView.findViewById(R.id.tvGoalRemaining);
            progressBar = itemView.findViewById(R.id.progressBarGoal);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Goal goal, int position);
    }

    public void setOnItemClickListener(GoalAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}