package com.flyteam.bbqvideo.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 * Integer 干啥的
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}