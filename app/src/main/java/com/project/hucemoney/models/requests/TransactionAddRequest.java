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
public class TransactionAddRequest {
    @NonNull
    private String name;
    @NonNull
    private long amount;
    @NonNull
    private String category;
    @NonNull
    private String account;
    @NonNull
    private LocalDate date;
    @NonNull
    private Boolean type;
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
