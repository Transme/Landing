package hello.bean;

public class BlogResult extends Result<Blog> {
    private BlogResult(ResultState state, String msg, Blog data){
        super(state, msg, data);
    }

    public static BlogResult success(String msg, Blog data){
        return new BlogResult(ResultState.OK, msg, data);
    }

    public static BlogResult success(String msg){
        return new BlogResult(ResultState.OK, msg,null);
    }

    public static BlogResult fail(String msg){
        return new BlogResult(ResultState.FAIL, msg, null);
    }
}
