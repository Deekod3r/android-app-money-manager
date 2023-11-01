package com.project.hucemoney.entities.crossref;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.entities.Transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(tableName = "transaction_goal", primaryKeys = {"transactionUUID", "goalUUID"},
        foreignKeys = {
                @ForeignKey(entity = Transaction.class,
                        parentColumns = FieldData.FIELD_UUID,
                        childColumns = "transactionUUID"),
                @ForeignKey(entity = Goal.class,
                        parentColumns = FieldData.FIELD_UUID,
                        childColumns = "goalUUID")
        })
@Getter
@Setter
@NoArgsConstructor
public class TransactionGoal {
    @NonNull
    @ColumnInfo(name = "transactionUUID", index = true)
    private String transactionUUID;
    @NonNull
    @ColumnInfo(name = "goalUUID", index = true)
    private String goalUUID;
    @ColumnInfo(name = "amount")
    private long amount;

    @Ignore
    public TransactionGoal(@NonNull String transactionUUID, @NonNull String goalUUID, long amount) {
        this.transactionUUID = transactionUUID;
        this.goalUUID = goalUUID;
        this.amount = amount;
    }
}

