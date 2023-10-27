package com.project.hucemoney.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.repositories.AccountRepository;
import com.project.hucemoney.repositories.GoalRepository;
import com.project.hucemoney.utils.SessionManager;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;
    private SessionManager sessionManager;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        this.goalRepository = new GoalRepository(application);
        sessionManager = new SessionManager(application);
    }
}
