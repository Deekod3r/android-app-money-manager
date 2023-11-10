package com.project.hucemoney.entities.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Category;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class BudgetWithCategory {
    @Embedded
    public Budget budget;

    @Relation(
            parentColumn = "category",
            entityColumn = "UUID"
    )
    public Category category;
}
