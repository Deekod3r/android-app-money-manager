package com.project.hucemoney.repositories;

import android.app.Application;

import com.project.hucemoney.DAOs.GoalDAO;
import com.project.hucemoney.database.AppDatabase;

public class GoalRepository {
    private GoalDAO goalDAO;
    public GoalRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        this.goalDAO = appDatabase.goalDAO();
    }
}
