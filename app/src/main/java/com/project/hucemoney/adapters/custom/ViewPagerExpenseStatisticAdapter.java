package com.project.hucemoney.adapters.custom;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.hucemoney.views.fragments.DayExpenseFragment;
import com.project.hucemoney.views.fragments.MonthExpenseFragment;
import com.project.hucemoney.views.fragments.YearExpenseFragment;

public class ViewPagerExpenseStatisticAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public ViewPagerExpenseStatisticAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MonthExpenseFragment();
            case 2:
                return new YearExpenseFragment();
            case 0:
            default:
                return new DayExpenseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
