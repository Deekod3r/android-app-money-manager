package com.project.hucemoney.entities.pojo;

import androidx.room.Ignore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryStatistic {
    private String name;
    private long amount;
    private boolean type;
    @Ignore
    public CategoryStatistic() {
    }
}
