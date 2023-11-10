package com.project.hucemoney.entities.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Transaction;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TransactionWithCategoryAndAccount {
    @Embedded
    public Transaction transaction;

    @Relation(
            parentColumn = "category",
            entityColumn = "UUID"
    )
    public Category category;

    @Relation(
            parentColumn = "account",
            entityColumn = "UUID"
    )
    public Account account;
}
