package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final List<Category> categories;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.categories = categories;
        this.context = context;
    }
    public CategoryAdapter(List<Category> categories, CategoryAdapter.OnItemClickListener onItemClickListener) {
        this.categories = categories;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        String type;
        if (category.isType() == Constants.TYPE_EXPENSE) {
            type = "Danh mục chi";
        } else {
            type = "Danh mục thu";
        }
        holder.name.setText(category.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(category, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categories != null && categories.size() > 0)
            return categories.size();
        else {
            return 0;
        }
    }

    public void setData(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCategoryName);
        }

    }
    public interface OnItemClickListener {
        void onItemClick(Category category, int position);
    }
    public void setOnItemClickListener(CategoryAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
