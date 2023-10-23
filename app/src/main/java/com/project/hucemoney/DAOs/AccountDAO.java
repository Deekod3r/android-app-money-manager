package com.project.hucemoney.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.Account;

import java.util.List;

@Dao
public interface AccountDAO {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long save(Account entity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(Account entity);

    @Delete
    int delete(Account entity);

    @Query("DELETE FROM " + FieldData.TABLE_ACCOUNTS)
    int deleteAll();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid LIMIT 1")
    Account findByUuid(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "WHERE " + FieldData.FIELD_ID + " = :id LIMIT 1")
    Account findByID(String id);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "ORDER BY :sortedField ASC")
    List<Account> findAllSortedASC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "ORDER BY :sortedField DESC")
    List<Account> findAllSortedDESC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "LIMIT :limit OFFSET (:page - 1) * :limit")
    List<Account> findAllPaginated(int page, int limit);

    @Query("SELECT * FROM "+ FieldData.TABLE_ACCOUNTS)
    LiveData<List<Account>> findAll();

    @Query("SELECT EXISTS (SELECT 1 " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid)")
    boolean isExists(String uuid);

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_ACCOUNTS)
    long count();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "WHERE " + FieldData.ACCOUNT_FIELD_USER + " = :user")
    LiveData<List<Account>> findAllByUser(String user);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_ACCOUNTS + " " +
            "WHERE " + FieldData.ACCOUNT_FIELD_NAME + " = :name LIMIT 1")
    Account findByName(String name);
}
