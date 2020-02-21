package hello.dao;

import hello.bean.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogDao {
    int getTotalBlog();

    List<Blog> getBlogByPage(int offset, int pageSize);

    List<Blog> getBlogByPage1(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("userId") int userId);

    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    Blog selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Blog record);
}