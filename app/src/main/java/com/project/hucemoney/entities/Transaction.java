package com.project.hucemoney.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.crossref.TransactionGoal;
import com.project.hucemoney.utils.AnnotationUtils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = FieldData.TABLE_TRANSACTIONS,
        foreignKeys = {
                @ForeignKey(
                        entity = Account.class,
                        parentColumns = FieldData.FIELD_UUID,
                        childColumns = FieldData.TRANSACTION_FIELD_ACCOUNT,
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = FieldData.FIELD_UUID,
                        childColumns = FieldData.TRANSACTION_FIELD_CATEGORY,
                        onDelete = ForeignKey.SET_NULL)},
        indices = {
                @Index(FieldData.TRANSACTION_FIELD_ACCOUNT),
                @Index(FieldData.TRANSACTION_FIELD_CATEGORY)
        })
@TypeConverters(AnnotationUtils.class)
public class Transaction implements Parcelable {
        @PrimaryKey
        @ColumnInfo(name = FieldData.FIELD_UUID)
        @NonNull
        private String UUID;
        @ColumnInfo(name = FieldData.FIELD_ID)
        @NonNull
        private String id;
        @ColumnInfo(name = FieldData.TRANSACTION_FIELD_ACCOUNT)
        @NonNull
        private String account;
        @ColumnInfo(name = FieldData.TRANSACTION_FIELD_DATE)
        @NonNull
        private LocalDate date;
        @ColumnInfo(name = FieldData.TRANSACTION_FIELD_TYPE)
        @NonNull
        private Boolean type;
        @ColumnInfo(name = FieldData.TRANSACTION_FIELD_CATEGORY)
        @NonNull
        private String category;
        @ColumnInfo(name = FieldData.TRANSACTION_FIELD_AMOUNT)
        private long amount ;
        @ColumnInfo(name = FieldData.TRANSACTION_FIELD_NOTE)
        @NonNull
        private String note;

        @Ignore
        public Transaction(@NonNull String UUID, @NonNull String id, @NonNull String account, @NonNull LocalDate date, @NonNull Boolean type, @NonNull String category, long amount, @NonNull String note) {
                this.UUID = UUID;
                this.id = id;
                this.account = account;
                this.date = date;
                this.type = type;
                this.category = category;
                this.amount = amount;
                this.note = note;
        }

        @Ignore
        protected Transaction(@NotNull Parcel in) {
                UUID = Objects.requireNonNull(in.readString());
                id = Objects.requireNonNull(in.readString());
                account = Objects.requireNonNull(in.readString());
                date = LocalDate.parse(in.readString());
                type = in.readByte() != 0;
                category = Objects.requireNonNull(in.readString());
                amount = in.readLong();
                note = Objects.requireNonNull(in.readString());
        }
        public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
                @Override
                public Transaction createFromParcel(Parcel in) {
                        return new Transaction(in);
                }

                @Override
                public Transaction[] newArray(int size) {
                        return new Transaction[size];
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
                dest.writeString(account);
                dest.writeString(date.toString());
                dest.writeByte((byte) (type ? 1 : 0));
                dest.writeString(category);
                dest.writeLong(amount);
                dest.writeString(note);
        }

}
