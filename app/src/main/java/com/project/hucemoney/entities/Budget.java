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
import androidx.room.TypeConverters;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.utils.AnnotationUtils;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = FieldData.TABLE_BUDGETS,
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = FieldData.FIELD_UUID,
                        childColumns = FieldData.BUDGET_FIELD_USER,
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = FieldData.FIELD_UUID,
                        childColumns = FieldData.BUDGET_FIELD_CATEGORY,
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(FieldData.BUDGET_FIELD_USER), @Index(FieldData.BUDGET_FIELD_CATEGORY)})
@TypeConverters(AnnotationUtils.class)
public class Budget implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = FieldData.FIELD_UUID)
    private String UUID;
    @NonNull
    @ColumnInfo(name = FieldData.FIELD_ID)
    private String id;
    @NonNull
    @ColumnInfo(name = FieldData.BUDGET_FIELD_USER)
    private String user;
    @NonNull
    @ColumnInfo(name = FieldData.BUDGET_FIELD_NAME)
    private String name;
    @ColumnInfo(name = FieldData.BUDGET_FIELD_START_DATE)
    private LocalDate startDate;
    @ColumnInfo(name = FieldData.BUDGET_FIELD_END_DATE)
    private LocalDate endDate;
    @ColumnInfo(name = FieldData.BUDGET_FIELD_INITIAL_LIMIT)
    private long initialLimit;
    @ColumnInfo(name = FieldData.BUDGET_FIELD_CURRENT_BALANCE)
    private long currentBalance;
    @ColumnInfo(name = FieldData.BUDGET_FIELD_CATEGORY)
    private String category;
    @ColumnInfo(name = FieldData.BUDGET_FIELD_NOTE)
    private String note;

    @Ignore
    public Budget(@NonNull String UUID, @NonNull String id, @NonNull String user, @NonNull String name, LocalDate startDate, LocalDate endDate, long initialLimit, long currentBalance, String category, String note) {
        this.UUID = UUID;
        this.id = id;
        this.user = user;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialLimit = initialLimit;
        this.currentBalance = currentBalance;
        this.category = category;
        this.note = note;
    }

    @Ignore
    protected Budget(Parcel in) {
        UUID = Objects.requireNonNull(in.readString());
        id = Objects.requireNonNull(in.readString());
        user = Objects.requireNonNull(in.readString());
        name = Objects.requireNonNull(in.readString());
        startDate = LocalDate.parse(in.readString());
        endDate = LocalDate.parse(in.readString());
        initialLimit = in.readLong();
        currentBalance = in.readLong();
        category = in.readString();
        note = in.readString();
    }

    public static final Creator<Budget> CREATOR = new Creator<Budget>() {
        @Override
        public Budget createFromParcel(Parcel in) {
            return new Budget(in);
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
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
        dest.writeString(user);
        dest.writeString(name);
        dest.writeString(startDate.toString());
        dest.writeString(endDate.toString());
        dest.writeLong(initialLimit);
        dest.writeLong(currentBalance);
        dest.writeString(category);
        dest.writeString(note);
    }
}
