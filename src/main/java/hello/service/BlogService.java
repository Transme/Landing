package hello.service;

import hello.bean.Blog;
import hello.bean.BlogResult;
import hello.dao.BlogDao;
import hello.dao.UserDao;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    private BlogDao blogDao;
    private UserDao userDao;
    private final SqlSession sqlSession;

    @Inject
    public BlogService(BlogDao blogDao,
                       UserDao userDao,
                       SqlSession sqlSession){
        this.blogDao = blogDao;
        this.userDao = userDao;
        this.sqlSession = sqlSession;
    }

    public List<Blog> getBlogByPage(int page, int pageSize){
        int offset = (page - 1)*10;
        List<Blog> blogs = blogDao.getBlogByPage(offset, pageSize);
        for (Blog blog : blogs) {
            blog.setUser(userDao.selectByPrimaryKey(blog.getUserId()));
        }
        return blogs;
    }

    public List<Blog> getBlogByPage(int page, int pageSize, int userId){
        int offset = (page - 1)*10;
        List<Blog> blogs = blogDao.getBlogByPage1(offset, pageSize, userId);
        for (Blog blog : blogs) {
            blog.setUser(userDao.selectByPrimaryKey(blog.getUserId()));
        }
        return blogs;
    }

    public Blog getBlogById(Long id){
        Blog blog = blogDao.selectByPrimaryKey(id);
        blog.setUser(userDao.selectByPrimaryKey(blog.getUserId()));
        return blog;
    }

    public int getTotalBlog(){
        return blogDao.getTotalBlog();
    }

    @Transactional
    public BlogResult save(Blog blog){
            try{
            blogDao.insert(blog);
            Blog b =  blogDao.selectByPrimaryKey(blog.getId());
            b.setUser(userDao.selectByPrimaryKey(b.getUserId()));
            return BlogResult.success("创建成功",b);
        }catch (Exception e){
            return BlogResult.fail("系统异常");
        }
    }

    public BlogResult alterBlog(Long id, Blog blog){
        Blog blogInDb = getBlogById(id);
        if(blogInDb == null){
            return BlogResult.fail("博客不存在");
        }
        if(!blogInDb.getUserId().equals(blog.getUserId())){
            return BlogResult.fail("无法修改别人的博客");
        }
        try{
            blog.setId(id);
            blogDao.updateByPrimaryKey(blog);
            Blog b = blogDao.selectByPrimaryKey(id);
            b.setUser(userDao.selectByPrimaryKey(b.getUserId()));
            return BlogResult.success("修改成功", b);
        }catch (Exception e){
            e.printStackTrace();
            return BlogResult.fail("系统异常");
        }
    }

    @Transactional
    public BlogResult deleteBlog(Long id, Long userId){
        Blog blog = blogDao.selectByPrimaryKey(id);
        if(blog == null){
            return BlogResult.fail("博客不存在");
        }
        if(!blog.getUserId().equals(userId)){
            return BlogResult.fail("不能修改别人的博客");
        }
        try{
            blogDao.deleteByPrimaryKey(id);
            return BlogResult.success("删除成功");
        }catch (Exception e){
            return BlogResult.fail("系统异常");
        }
    }
}
