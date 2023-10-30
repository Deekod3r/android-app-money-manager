package com.project.hucemoney.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.hucemoney.DAOs.AccountDAO;
import com.project.hucemoney.DAOs.CategoryDAO;
import com.project.hucemoney.DAOs.GoalDAO;
import com.project.hucemoney.DAOs.UserDAO;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.entities.User;
import com.project.hucemoney.utils.AnnotationUtils;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Account.class, Goal.class, Category.class},
        version = 12, exportSchema = false)
@TypeConverters(AnnotationUtils.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract AccountDAO accountDAO();
    public abstract GoalDAO goalDAO();
    public abstract CategoryDAO categoryDAO();

    private static final String DATABASE_NAME = "huce.money";

    private static volatile AppDatabase INSTANCE;

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

