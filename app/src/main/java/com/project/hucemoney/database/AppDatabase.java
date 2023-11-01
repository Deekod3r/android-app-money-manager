package com.project.hucemoney.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.hucemoney.DAOs.AccountDAO;
import com.project.hucemoney.DAOs.BudgetDAO;
import com.project.hucemoney.DAOs.CategoryDAO;
import com.project.hucemoney.DAOs.GoalDAO;
import com.project.hucemoney.DAOs.TransactionDAO;
import com.project.hucemoney.DAOs.TransactionGoalDAO;
import com.project.hucemoney.DAOs.UserDAO;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.User;
import com.project.hucemoney.entities.crossref.TransactionGoal;
import com.project.hucemoney.utils.AnnotationUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Account.class, Goal.class, Category.class, Transaction.class, TransactionGoal.class, Budget.class},
        version = 16, exportSchema = false)
@TypeConverters(AnnotationUtils.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract AccountDAO accountDAO();
    public abstract GoalDAO goalDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract BudgetDAO budgetDAO();
    public abstract TransactionDAO transactionDAO();
    public abstract TransactionGoalDAO transactionGoalCrossRefDAO();

    private static final String DATABASE_NAME = "huce.money";

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .setQueryCallback((s, list) -> Log.d("RoomQuery", "SQL Query: " + s + " with args: " + list), Executors.newSingleThreadExecutor())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

