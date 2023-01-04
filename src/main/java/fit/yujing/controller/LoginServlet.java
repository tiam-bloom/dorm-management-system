package fit.yujing.controller;

import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Tiam
 * @Date 2022/12/27 21:49
 * @Description:
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDaoImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stuCode = req.getParameter("stuCode");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");
        User user = userDao.findByStuCode(stuCode);
        if (user == null) {
            req.setAttribute("error", "用户名不存在");
            req.getRequestDispatcher("/").forward(req, resp);
        } else if (!password.equals(user.getPassword())) {
            req.setAttribute("error", "密码错误");
            req.getRequestDispatcher("/").forward(req, resp);
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("session_user", user);
            if (remember != null) {
                // 7天内自动填充密码
                Cookie cookie_stuCode = new Cookie("cookie_stuCode", stuCode);
                Cookie cookie_pwd = new Cookie("cookie_pwd", password);
                cookie_stuCode.setMaxAge(7 * 24 * 60 * 60);
                cookie_pwd.setMaxAge(7 * 24 * 60 * 60);
                resp.addCookie(cookie_stuCode);
                resp.addCookie(cookie_pwd);
            }else{
                // 未勾选 => 删除Cookie
                Cookie[] cookies = req.getCookies();
                List<Cookie> collect = Arrays.stream(cookies)
                        .filter(cookie ->
                        cookie.getName().equals("cookie_stuCode") ||
                        cookie.getName().equals("cookie_pwd"))
                        .collect(Collectors.toList());
                collect.forEach(cookie ->{
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                });
            }
            req.getRequestDispatcher("/index.action").forward(req, resp);
        }
    }
}
