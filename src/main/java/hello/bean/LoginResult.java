package hello.bean;

public class LoginResult  extends Result<User> {
    boolean isLogin;

    protected LoginResult(ResultState state, String message, User data, boolean isLogin) {
        super(state, message, data);
        this.isLogin = isLogin;
    }

    public static LoginResult success(User user){
        return new LoginResult(ResultState.OK, null, user, true);
    }

    public static LoginResult success(String msg, boolean isLogin){
        return new LoginResult(ResultState.OK, msg, null, isLogin);
    }

    public static LoginResult success(User user, String msg){
        return new LoginResult(ResultState.OK, msg, user, true);
    }

    public static LoginResult fail(String msg){
        return new LoginResult(ResultState.FAIL, msg, null, false);
    }
    public static LoginResult fail(String msg, User user){
        return new LoginResult(ResultState.FAIL, msg, user, false);
    }
    public boolean isLogin() {
        return isLogin;
    }
}
