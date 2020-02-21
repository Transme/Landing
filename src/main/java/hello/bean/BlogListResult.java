package hello.bean;

import java.util.List;

public class BlogListResult extends Result<List<Blog>>{

    private int total;
    private int page;
    private int totalPage;

    protected BlogListResult(ResultState state, String message, List<Blog> data, Integer total, Integer page, Integer totalPage) {
        super(state, message, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public static  BlogListResult failure(String msg){
        return new BlogListResult(ResultState.FAIL, msg, null, null, null, null);
    }

    public static BlogListResult success(String msg, List<Blog> data, Integer total, Integer page){
        return new BlogListResult(ResultState.OK, msg, data, total, page, total/10);
    }
}
