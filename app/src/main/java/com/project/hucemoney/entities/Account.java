package com.project.hucemoney.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.project.hucemoney.database.FieldData;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = FieldData.TABLE_ACCOUNTS,
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "UUID",
                childColumns = "user",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("user")})
public class Account implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = FieldData.FIELD_UUID)
    private String UUID;
    @NonNull
    @ColumnInfo(name = FieldData.FIELD_ID)
    private String id;
    @NonNull
    @ColumnInfo(name = FieldData.ACCOUNT_FIELD_USER)
    private String user;
    @NonNull
    @ColumnInfo(name = FieldData.ACCOUNT_FIELD_NAME)
    private String name;
    @ColumnInfo(name = FieldData.ACCOUNT_FIELD_AMOUNT)
    private long amount;
    @ColumnInfo(name = FieldData.ACCOUNT_FIELD_NOTE)
    private String note;

    @Ignore
    public Account(@NonNull String UUID, @NonNull String id, @NonNull String user, @NonNull String name, long amount, String note) {
        this.UUID = UUID;
        this.id = id;
        this.user = user;
        this.name = name;
        this.amount = amount;
        this.note = note;
    }

    @Ignore
    protected Account(@NotNull Parcel in) {
        UUID = Objects.requireNonNull(in.readString());
        id = Objects.requireNonNull(in.readString());
        name = Objects.requireNonNull(in.readString());
        amount = in.readLong();
        note = in.readString();
        user = Objects.requireNonNull(in.readString());
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UUID);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeLong(amount);
        dest.writeString(note);
        dest.writeString(user);
    }
}
