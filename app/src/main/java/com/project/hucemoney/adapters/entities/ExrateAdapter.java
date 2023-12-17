package com.project.hucemoney.adapters.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.entities.Exrate;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ExrateAdapter extends RecyclerView.Adapter<ExrateAdapter.ViewHolder> {

    private final List<Exrate> exrates;
    private Context context;
    private ExrateAdapter.OnItemClickListener onItemClickListener;
    private boolean type;

    public ExrateAdapter(Context context, List<Exrate> exrates, boolean type) {
        this.exrates = exrates;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ExrateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exrate, parent, false);
        return new ExrateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExrateAdapter.ViewHolder holder, int position) {
        Exrate exrate = exrates.get(position);
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        format.setMaximumFractionDigits(6);
        holder.code.setText(exrate.getCurrencyCode());
        if(type) {
            holder.transfer.setText(String.format("%s %s", format.format(1/exrate.getTransferRateAsDouble()), exrate.getCurrencyCode()));
            holder.buy.setText(String.format("Mua vào:\n1 VND ~ %s %s", format.format(1/exrate.getBuyRateAsDouble()), exrate.getCurrencyName()));
            holder.sell.setText(String.format("Bán ra:\n1 VND ~ %s %s", format.format(1/exrate.getSellRateAsDouble()), exrate.getCurrencyName()));
        } else {
            holder.transfer.setText(String.format("%s VND", format.format(exrate.getTransferRateAsDouble())));
            holder.buy.setText(String.format("Mua vào:\n1 %s ~ %s VND", exrate.getCurrencyName(), format.format(exrate.getBuyRateAsDouble())));
            holder.sell.setText(String.format("Bán ra:\n1 %s ~ %s VND", exrate.getCurrencyName(), format.format(exrate.getSellRateAsDouble())));
        }
        holder.flag.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), Constants.FLAGS_REGION.get(exrate.getCurrencyCode()), null));
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(exrate, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exrates != null && exrates.size() > 0)
            return exrates.size();
        else {
            return 0;
        }
    }

    public void setData(List<Exrate> exrates) {
        this.exrates.clear();
        this.exrates.addAll(exrates);
        notifyDataSetChanged();
    }

    public void changeType(boolean type) {
        this.type = type;
        notifyItemRangeChanged(0, exrates.size());
    }

    public boolean getType() {
        return type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView code, transfer, buy, sell;
        ImageView flag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.tvCode);
            transfer = itemView.findViewById(R.id.tvTransfer);
            buy = itemView.findViewById(R.id.tvBuy);
            sell = itemView.findViewById(R.id.tvSell);
            flag = itemView.findViewById(R.id.imgFlag);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Exrate exrate, int position);
    }

    public void setOnItemClickListener(ExrateAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
