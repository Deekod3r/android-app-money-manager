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
@Entity(tableName = FieldData.TABLE_CATEGORIES,
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = FieldData.FIELD_UUID,
                childColumns = FieldData.CATEGORY_FIELD_USER,
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(FieldData.CATEGORY_FIELD_USER)})
public class Category implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = FieldData.FIELD_UUID)
    @NonNull
    private String UUID;
    @ColumnInfo(name = FieldData.FIELD_ID)
    @NonNull
    private String id;
    @ColumnInfo(name = FieldData.CATEGORY_FIELD_USER)
    @NonNull
    private String user;
    @ColumnInfo(name = FieldData.CATEGORY_FIELD_NAME)
    @NonNull
    private String name;
    @ColumnInfo(name = FieldData.CATEGORY_FIELD_TYPE)
    private boolean type;
//    @ColumnInfo(name = "icon")
//    private String icon;
    @ColumnInfo(name = FieldData.CATEGORY_FIELD_PARENT, defaultValue = "0")
    private String parent;
    @ColumnInfo(name = FieldData.CATEGORY_FIELD_NOTE)
    private String note;

    @Ignore
    public Category(@NonNull String UUID, @NonNull String id, @NonNull String user, @NonNull String name, boolean type, String parent, String note) {
        this.UUID = UUID;
        this.id = id;
        this.user = user;
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.note = note;
    }

    @Ignore
    protected Category(@NotNull Parcel in) {
        UUID = Objects.requireNonNull(in.readString());
        id = Objects.requireNonNull(in.readString());
        name = Objects.requireNonNull(in.readString());
        type = in.readByte() != 0;
        note = in.readString();
        parent = in.readString();
        user = Objects.requireNonNull(in.readString());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
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
        dest.writeByte((byte) (type ? 1 : 0));
        dest.writeString(note);
        dest.writeString(parent);
        dest.writeString(user);
    }
}
