package com.project.hucemoney.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.project.hucemoney.database.FieldData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity (tableName = FieldData.TABLE_USERS)
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = FieldData.FIELD_UUID)
    private String UUID;
    @ColumnInfo(name = FieldData.FIELD_ID)
    @NonNull
    private String id;
    @ColumnInfo(name = FieldData.USER_FIELD_USERNAME)
    @NonNull
    private String username;
    @ColumnInfo(name = FieldData.USER_FIELD_EMAIL)
    @NonNull
    private String email;
    @ColumnInfo(name = FieldData.USER_FIELD_PASSWORD)
    @NonNull
    private String password;
    @ColumnInfo(name = FieldData.FIELD_IS_DELETED, defaultValue = "false")
    private Boolean isDeleted;

    @Ignore
    public User(@NonNull String UUID, @NonNull String id, @NonNull String username, @NonNull String email, @NonNull String password, Boolean isDeleted) {
        this.UUID = UUID;
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isDeleted = isDeleted;
    }
}

