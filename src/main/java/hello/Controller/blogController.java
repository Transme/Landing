package hello.Controller;

import hello.bean.Blog;
import hello.bean.BlogListResult;
import hello.bean.BlogResult;
import hello.bean.User;
import hello.service.AuthService;
import hello.service.BlogService;
import hello.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Controller
public class blogController {

    @Inject
    BlogService blogService;
    @Inject
    AuthService authService;
    @Inject
    UserService userService;

    @GetMapping("blog")
    @ResponseBody
    public BlogListResult getBlog(Integer page, Integer user_id, boolean atIndex){
        if(page == null && user_id == null){
            List<Blog> blog = blogService.getBlogByPage(1, 10);
            int total = blogService.getTotalBlog();
            return BlogListResult.success("获取成功", blog, total, 1);
        }
        if (user_id == null) {
            List<Blog> blog = blogService.getBlogByPage(page, 10);
            int total = blogService.getTotalBlog();
            return BlogListResult.success("获取成功", blog, total, page);
        }
        if(page == null) {
            List<Blog> blog = blogService.getBlogByPage(1, 10, user_id);
            int total = blogService.getTotalBlog();
            return BlogListResult.success("获取成功", blog, total, page);
        }
        List<Blog> blog = blogService.getBlogByPage(page, 10, user_id);
        int total = blogService.getTotalBlog();
        return BlogListResult.success("获取成功", blog, total, page);
    }

    @GetMapping("blog/{id}")
    @ResponseBody
    public BlogResult getBlogById(@PathVariable("id") Long id){
        try{
            return BlogResult.success("获取成功", blogService.getBlogById(id));
        }catch (Exception e){
            return BlogResult.fail("系统异常");
        }
    }

    @PostMapping("blog")
    @ResponseBody
    public BlogResult createBlog(@RequestParam Map<String, String> params){
        try{
            return authService.getCurrentUser()
                    .map(user -> blogService.save(fromParam(params, user)))
                    .orElse(BlogResult.fail("登录后才能进行操作！"));
        }catch (Exception e){
            return BlogResult.fail("系统异常");
        }
    }

    @PatchMapping("blog/{id}")
    @ResponseBody
    public BlogResult alterBlog(@RequestParam Map<String, String> params, @PathVariable("id") Long id){
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.alterBlog(id,fromParam(params, user)))
                    .orElse(BlogResult.fail("用户没有登录"));
        } catch (Exception e) {
            return BlogResult.fail("系统异常");
        }
    }

    @DeleteMapping("blog/{id}")
    @ResponseBody
    public BlogResult deleteBlog(@PathVariable("id") Long id){
        try{
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlog(id, user.getId()))
                    .orElse(BlogResult.fail("用户未登录"));
        }catch (Exception e){
            return BlogResult.fail("系统异常");
        }
    }

    private Blog fromParam(Map<String, String> params, User user){
         String title = params.get("title");
         String content = params.get("content");
         String description = params.get("description");
         Blog blog = new Blog();
         blog.setContent(content);
         blog.setTitle(title);
         blog.setDescription(description);
         blog.setUserId(user.getId());
         blog.setUser(userService.getUserById(user.getId()));
         return blog;
    }
}
