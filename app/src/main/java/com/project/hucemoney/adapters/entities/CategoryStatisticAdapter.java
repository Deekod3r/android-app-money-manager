package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.pojo.CategoryStatistic;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CategoryStatisticAdapter extends RecyclerView.Adapter<CategoryStatisticAdapter.ViewHolder> {

    private final List<CategoryStatistic> categoryStatistics;
    private Context context;
    private CategoryStatisticAdapter.OnItemClickListener onItemClickListener;
    private int positionSelected = -1;
    private long sumAmount = 0;


    public CategoryStatisticAdapter(Context context, List<CategoryStatistic> categoryStatistics) {
        this.categoryStatistics = categoryStatistics;
        this.context = context;
    }

    public CategoryStatisticAdapter(List<CategoryStatistic> categoryStatistics, CategoryStatisticAdapter.OnItemClickListener onItemClickListener) {
        this.categoryStatistics = categoryStatistics;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryStatisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_statistic, parent, false);
        return new CategoryStatisticAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryStatisticAdapter.ViewHolder holder, int position) {
        CategoryStatistic category = categoryStatistics.get(position);
        holder.name.setText(category.getName());
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        holder.amount.setText(String.format("%s %s", format.format(category.getAmount()), context.getString(R.string.vi_currency)));
        holder.percent.setText(String.format("%s%%", format.format((double) category.getAmount() / sumAmount * 100)));
    }

    @Override
    public int getItemCount() {
        if (categoryStatistics != null && categoryStatistics.size() > 0)
            return categoryStatistics.size();
        else {
            return 0;
        }
    }

    public void setData(List<CategoryStatistic> categoryStatistics) {
        this.categoryStatistics.clear();
        this.categoryStatistics.addAll(categoryStatistics);
        for (CategoryStatistic categoryStatistic : categoryStatistics) {
            sumAmount += categoryStatistic.getAmount();
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, percent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCategoryName);
            amount = itemView.findViewById(R.id.tvAmount);
            percent = itemView.findViewById(R.id.tvPercent);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Category category, int position);
    }

    public void setOnItemClickListener(CategoryStatisticAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
