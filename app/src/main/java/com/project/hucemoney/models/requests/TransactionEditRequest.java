package com.project.hucemoney.models.requests;

import androidx.annotation.NonNull;

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
}
