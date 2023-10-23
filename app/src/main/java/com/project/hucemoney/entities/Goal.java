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

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = FieldData.TABLE_GOALS,
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = FieldData.FIELD_UUID,
                childColumns = "user",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("user")})
public class Goal implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = FieldData.FIELD_UUID)
    @NonNull
    private String UUID;
    @ColumnInfo(name = FieldData.FIELD_ID)
    @NonNull
    private String id;
    @ColumnInfo(name = FieldData.GOAL_FIELD_USER)
    @NonNull
    private String user;
    @ColumnInfo(name = FieldData.GOAL_FIELD_NAME)
    @NonNull
    private String name;
    @ColumnInfo(name = FieldData.GOAL_FIELD_TARGET_AMOUNT)
    @NonNull
    private long targetAmount;
    @ColumnInfo(name = FieldData.GOAL_FIELD_CURRENT_AMOUNT)
    @NonNull
    private long currentAmount;
    @ColumnInfo(name = FieldData.GOAL_FIELD_START_DATE)
    @NonNull
    private LocalDate startDate;
    @ColumnInfo(name = FieldData.GOAL_FIELD_END_DATE)
    @NonNull
    private LocalDate endDate;
    @ColumnInfo(name = FieldData.GOAL_FIELD_NOTE)
    private String note;

    @Ignore
    public Goal(@NonNull String UUID, @NonNull String id, @NonNull String user, @NonNull String name, long targetAmount, long currentAmount, @NonNull LocalDate startDate, @NonNull LocalDate endDate, String note) {
        this.UUID = UUID;
        this.id = id;
        this.user = user;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    @Ignore
    protected Goal(@NotNull Parcel in) {
        UUID = Objects.requireNonNull(in.readString());
        id = Objects.requireNonNull(in.readString());
        name = Objects.requireNonNull(in.readString());
        targetAmount = in.readLong();
        currentAmount = in.readLong();
        startDate = LocalDate.parse(in.readString());
        endDate = LocalDate.parse(in.readString());
        note = in.readString();
        user = Objects.requireNonNull(in.readString());
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
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
        dest.writeLong(targetAmount);
        dest.writeLong(currentAmount);
        dest.writeString(startDate.toString());
        dest.writeString(endDate.toString());
        dest.writeString(note);
        dest.writeString(user);
    }
}
