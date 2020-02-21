package hello.Controller;
import hello.bean.LoginResult;
import hello.bean.User;
import hello.service.AuthService;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class authorController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private AuthService authService;

    @Inject
    public authorController(UserService userService,
                            AuthenticationManager authenticationManager,
                            AuthService authService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("author/register")
    @ResponseBody
    public LoginResult register(@RequestParam Map<String, String> info){
        String username = info.get("username");
        String password = info.get("password");
        User user = null;
        if(username == null || password == null){
            return LoginResult.fail("用户名或密码为空");
        }else{
            if(username.length() < 1 || username.length() > 15){
                return LoginResult.fail("用户名格式错误");
            }
            if(password.length() < 6 || password.length() > 16){
                return LoginResult.fail("密码格式错误");
            }
        }
        try{
            userService.save(username, password);
        }catch (Exception e){
            return LoginResult.fail("用户名已经存在！");
        }
        user = userService.getUserByUserName(username);
        return LoginResult.success(user,"注册成功");
    }

    @GetMapping("author")
    @ResponseBody
    public LoginResult isLogin(){
        return authService.getCurrentUser()
                            .map(user -> LoginResult.success("用户已经登录!", true))
                            .orElse(LoginResult.fail("用户没登录"));
    }

    @PostMapping("author/Login")
    @ResponseBody
    public Object Login(@RequestParam Map<String, Object> info, HttpServletRequest request){
        if(request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla")){
            return "死爬虫，滚";
        }

        String username = info.get("username").toString();
        String password = info.get("password").toString();

        UserDetails userDetails;
        try{
            userDetails = userService.loadUserByUsername(username);
        }catch (Exception e){
            return LoginResult.fail("用户不存在！");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try{
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success(userService.getUserByUserName(username), "登录成功！");
        }catch (Exception e){
            return LoginResult.fail("密码错误！");
        }
    }

    @GetMapping("author/logout")
    @ResponseBody
    public LoginResult Logout(){
        LoginResult result = authService.getCurrentUser()
                .map(user -> LoginResult.success("注销登录成功！", false))
                .orElse(LoginResult.fail("用户没登录！"));
        SecurityContextHolder.clearContext();
        return result;
    }

    @PostMapping("author/alterPassword")
    @ResponseBody
    public LoginResult alterPassword(@RequestParam Map<String, String> params){
        String username = params.get("username");
        String password = params.get("password");

        LoginResult result = authService.getCurrentUser()
                .map(user -> userService.update(validationUser(user, username, password)))
                .orElse(LoginResult.fail("用户没登录"));
        SecurityContextHolder.clearContext();
        return result;
    }

    public User validationUser(User user, String username, String password){
        if(!user.getUsername().equals(username)){
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setEncryptedPassword(encoder.encode(password));
        return user;
    }
}
