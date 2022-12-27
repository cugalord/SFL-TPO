package com.example.sfl_mobile.data;

import com.example.sfl_mobile.Common;
import com.example.sfl_mobile.data.model.LoggedInUser;

import java.io.IOException;

import Data.DataStaff;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            /*Common.dbapi.login(username, password);
            System.out.println(username + " " + password);
            if (Common.dbapi.isConnectionEstablished()) {
                System.out.println("Here");
                DataStaff staffUser = Common.dbapi.getStaffDataFromUsername(username);
                LoggedInUser user = new LoggedInUser(
                        username,
                        staffUser.getFirstNameAndInitial()
                );
                return new Result.Success(user);
            }
            return new Result.Error(new IOException("Wrong username or password."));*/

            LoggedInUser user;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Common.dbapi.login(username, password);
                }
            }).start();

            if (Common.dbapi.isConnectionEstablished()) {
                DataStaff staffUser = Common.dbapi.getStaffDataFromUsername(username);
                user = new LoggedInUser(
                        username,
                        staffUser.getFirstNameAndInitial()
                );
                return new Result.Success<>(user);
            }
            return new Result.Error(new IOException("Wrong username or password."));

            /*// TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");*/
            //return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        Common.dbapi.logout();
        // TODO: revoke authentication
    }
}