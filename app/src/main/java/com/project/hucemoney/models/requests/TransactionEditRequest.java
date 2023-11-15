package com.project.hucemoney.models.requests;

import androidx.annotation.NonNull;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEditRequest {
    @NonNull
    private String UUID;
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
    private String note;
}
