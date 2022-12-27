package com.example.sfl_mobile.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String userName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String userName) {
        this.displayName = displayName;
        this.userName = userName;
    }

    String getDisplayName() {
        return displayName;
    }

    String getUserName() {
        return userName;
    }
}