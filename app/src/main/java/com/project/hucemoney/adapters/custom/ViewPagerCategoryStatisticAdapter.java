package com.project.hucemoney.adapters.custom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.hucemoney.entities.pojo.CategoryStatistic;
import com.project.hucemoney.views.fragments.CategoryStatisticFragment;

import java.util.List;

public class ViewPagerCategoryStatisticAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2;

    public ViewPagerCategoryStatisticAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new CategoryStatisticFragment(true);
        }
        return new CategoryStatisticFragment(false);
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
