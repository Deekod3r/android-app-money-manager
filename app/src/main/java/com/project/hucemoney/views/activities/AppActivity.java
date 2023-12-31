package com.project.hucemoney.views.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.hucemoney.R;
import com.project.hucemoney.databinding.ActivityAppBinding;
import com.project.hucemoney.views.fragments.AccountFragment;
import com.project.hucemoney.views.fragments.HomeFragment;
import com.project.hucemoney.views.fragments.OtherFragment;
import com.project.hucemoney.views.fragments.StatisticFragment;
import com.project.hucemoney.views.fragments.TransactionFragment;

public class AppActivity extends AppCompatActivity {
    private ActivityAppBinding binding;
    private boolean doubleBackToExitPressedOnce = false;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        controlAction();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn một lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        replaceFragment(new HomeFragment());
    }

    private void controlAction() {
        binding.navbarBottom.setOnItemSelectedListener(item -> {
            int i = item.getItemId();
            Fragment newFragment = null;

            if (currentFragment != null) {
                if (i == R.id.nav_home && currentFragment instanceof HomeFragment) {
                    return true;
                } else if (i == R.id.nav_account && currentFragment instanceof AccountFragment) {
                    return true;
                } else if (i == R.id.nav_transaction && currentFragment instanceof TransactionFragment) {
                    return true;
                } else if (i == R.id.nav_statistic && currentFragment instanceof StatisticFragment) {
                    return true;
                } else if (i == R.id.nav_other && currentFragment instanceof OtherFragment) {
                    return true;
                }
            }

            if (i == R.id.nav_home) {
                newFragment = new HomeFragment();
            } else if (i == R.id.nav_account) {
                newFragment = new AccountFragment();
            } else if (i == R.id.nav_transaction) {
                newFragment = new TransactionFragment();
            } else if (i == R.id.nav_statistic) {
                newFragment = new StatisticFragment();
            } else if (i == R.id.nav_other) {
                newFragment = new OtherFragment();
            }

            if (newFragment != null) {
                replaceFragment(newFragment);
                currentFragment = newFragment;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void setSelectedNavItem(int itemId) {
        binding.navbarBottom.setSelectedItemId(itemId);
    }

}