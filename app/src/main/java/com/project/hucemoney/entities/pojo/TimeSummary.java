package com.project.hucemoney.entities.pojo;

import androidx.room.Ignore;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeSummary {
    private String time;
    private long expense;
    private long income;

    @Ignore
    public TimeSummary(String time, long expense, long income) {
        this.time = time;
        this.expense = expense;
        this.income = income;
    }
}
