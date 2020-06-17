package com.flyteam.bbqvideo.data;

import com.flyteam.bbqvideo.R;
import com.flyteam.bbqvideo.data.model.LoggedInUser;
import com.flyteam.bbqvideo.ui.login.LoginViewModel;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;
    private LoginViewModel loginViewModel = null;
    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(LoginViewModel loginViewModel,String username, String password) {
        // handle login
        this.loginViewModel = loginViewModel;
        dataSource.login(this,username, password);
    }

    public void loginRsp(Result<LoggedInUser> result){
        if(loginViewModel!=null){
            LoggedInUser data= result.getData();
            setLoggedInUser(data);
            loginViewModel.loginRsp(result);
        }
    }
}