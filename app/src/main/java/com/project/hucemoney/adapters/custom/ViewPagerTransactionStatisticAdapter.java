package com.project.hucemoney.adapters.custom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.hucemoney.views.fragments.CurrentTransactionFragment;
import com.project.hucemoney.views.fragments.MonthTransactionFragment;
import com.project.hucemoney.views.fragments.YearTransactionFragment;

public class ViewPagerTransactionStatisticAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public ViewPagerTransactionStatisticAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MonthTransactionFragment();
            case 2:
                return new YearTransactionFragment();
            case 0:
            default:
                return new CurrentTransactionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}