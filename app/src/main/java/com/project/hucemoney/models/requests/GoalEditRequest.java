package com.project.hucemoney.models.requests;

import androidx.annotation.NonNull;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GoalEditRequest {
    @NonNull
    private String UUID;
    @NonNull
    private String name;
    private long targetAmount;
    private long currentAmount;
    @NonNull
    private LocalDate startDate;
    @NonNull
    private LocalDate endDate;
    private String note;

    public void setName(String name) {
        this.name = name.trim();
    }
    public void setNote (String note) {
        if (note != null) {
            this.note = note.trim();
        }
    }
}
