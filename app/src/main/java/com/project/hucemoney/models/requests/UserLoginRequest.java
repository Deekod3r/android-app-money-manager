package com.project.hucemoney.models.requests;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;

    public void setUsername(String username) {
        this.username = username.trim();
    }
}
