package com.project.hucemoney.entities.pojo;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TransactionGroup {
    public LocalDate date;
    public List<TransactionWithCategoryAndAccount> transactions;
}
