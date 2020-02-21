package hello.service;

import hello.bean.LoginResult;
import hello.bean.User;
import hello.dao.UserDao;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {

    @Inject
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Inject
    private UserDao userDao;

    public User getUserByUserName(String username){
        return userDao.getUserByUsername(username);
    }
    public User getUserById(Long id) { return userDao.selectByPrimaryKey(id); }
    public void save(String username, String password){
        User user = new User();
        Date date = new Date();
        user.setUsername(username);
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
        user.setAvatar("https://blog-server.hunger-valley.com/avatar/69.jpg");
        userDao.insert(user);
    }

    public LoginResult update(User user){
        try{
            userDao.updateByPrimaryKey(user);
            return LoginResult.fail("修改成功", getUserByUserName(user.getUsername()));
        }catch (Exception e){
            return LoginResult.fail("系统故障");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " 不存在！");
        }

        return new org.springframework.security.core.userdetails.User(
                username, user.getEncryptedPassword(), Collections.emptyList());
    }
}
