package com.project.hucemoney.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.TimeSummary;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long save(Transaction entity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(Transaction entity);

    @Delete
    int delete(Transaction entity);

    @Query("DELETE FROM " + FieldData.TABLE_TRANSACTIONS)
    int deleteAll();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid LIMIT 1")
    Transaction findByUuid(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "WHERE " + FieldData.FIELD_ID + " = :id LIMIT 1")
    Transaction findByID(String id);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "ORDER BY :sortedField ASC")
    List<Transaction> findAllSortedASC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "ORDER BY :sortedField DESC")
    List<Transaction> findAllSortedDESC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "LIMIT :limit OFFSET (:page - 1) * :limit")
    List<Transaction> findAllPaginated(int page, int limit);

    @Query("SELECT EXISTS (SELECT 1 " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid)")
    boolean isExists(String uuid);

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_TRANSACTIONS)
    long count();

    @androidx.room.Transaction
    @Query("SELECT " + FieldData.TABLE_TRANSACTIONS +".* " +
            "FROM " + FieldData.TABLE_TRANSACTIONS + " " +
            "INNER JOIN " + FieldData.TABLE_ACCOUNTS + " " +
            "ON " + FieldData.TABLE_TRANSACTIONS + "." + FieldData.TRANSACTION_FIELD_ACCOUNT + " = " + FieldData.TABLE_ACCOUNTS + "." + FieldData.FIELD_UUID + " " +
            "WHERE " + FieldData.TABLE_ACCOUNTS + "." + FieldData.ACCOUNT_FIELD_USER + " = :user ORDER BY " + FieldData.TRANSACTION_FIELD_DATE + " DESC")
    LiveData<List<TransactionWithCategoryAndAccount>> findAll(String user);

    @Query("SELECT " +
            "strftime('%Y-%m-%d', date) AS time, " +
            "SUM(CASE WHEN type = 1 THEN transactions.amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN type = 0 THEN transactions.amount ELSE 0 END) AS expense " +
            "FROM transactions INNER JOIN accounts on transactions.account = accounts.UUID " +
            "WHERE user = :user AND date >= date(:selectedDate, 'start of day') AND date <= date(:selectedDate, 'start of day', '+1 day', '-1 second')")
    TimeSummary findDateSummary(String user, LocalDate selectedDate);

    @Query("SELECT " +
            "strftime('%Y-%m', date) AS time, " +
            "SUM(CASE WHEN type = 1 THEN transactions.amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN type = 0 THEN transactions.amount ELSE 0 END) AS expense " +
            "FROM transactions INNER JOIN accounts on transactions.account = accounts.UUID " +
            "WHERE user = :user AND strftime('%Y-%m', date) = strftime('%Y-%m', :selectedDate)")
    TimeSummary findMonthSummary(String user, LocalDate selectedDate);

    @Query("SELECT " +
            "strftime('%Y', date) AS time, " +
            "SUM(CASE WHEN type = 1 THEN transactions.amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN type = 0 THEN transactions.amount ELSE 0 END) AS expense " +
            "FROM transactions INNER JOIN accounts on transactions.account = accounts.UUID " +
            "WHERE user = :user AND strftime('%Y', date) = strftime('%Y', :selectedYear)")
    TimeSummary findYearSummary(String user, LocalDate selectedYear);

    @Query("SELECT " +
            "strftime('%Y-%m', date) AS time, " +
            "SUM(CASE WHEN type = 1 THEN transactions.amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN type = 0 THEN transactions.amount ELSE 0 END) AS expense " +
            "FROM transactions INNER JOIN accounts on transactions.account = accounts.UUID " +
            "WHERE user = :user AND strftime('%Y', date) = strftime('%Y', :selectedYear)" +
            "GROUP BY time")
    List<TimeSummary> findMonthsSummary(String user, LocalDate selectedYear);

    @Query("SELECT " +
            "strftime('%Y', date) AS time, " +
            "SUM(CASE WHEN type = 1 THEN transactions.amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN type = 0 THEN transactions.amount ELSE 0 END) AS expense " +
            "FROM transactions INNER JOIN accounts on transactions.account = accounts.UUID " +
            "WHERE user = :user " +
            "GROUP BY time")
    List<TimeSummary> findYearsSummary(String user);

}
