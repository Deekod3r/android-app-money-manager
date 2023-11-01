package com.project.hucemoney.entities.pojo;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.crossref.TransactionGoal;

import java.util.List;

public class GoalWithTransactions {
    @Embedded
    public Goal goal;
    @Relation(
            parentColumn = "UUID",
            entityColumn = "UUID",
            associateBy = @Junction(value = TransactionGoal.class,
                    parentColumn = "goalUUID",
                    entityColumn = "transactionUUID")
    )
    public List<Transaction> transactions;
}
