package fit.yujing.controller;

import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Tiam
 * @Date 2023/1/1 16:26
 * @Description:
 */
@WebServlet("/password.action")
public class ChangePasswordServlet extends HttpServlet {
    private UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "preChange":
                req.setAttribute("mainRight", "/jsp/passwordChange.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            default:
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "change":
                String oldPassword = req.getParameter("oldPassword");
                String newPassword = req.getParameter("newPassword");
                // 验证旧密码
                User session_user = (User) req.getSession().getAttribute("session_user");
                Integer id = session_user.getId();
                User user = userDao.findUserById(String.valueOf(id));
                String tips = null;
                if (!oldPassword.equals(user.getPassword())){
                    tips = "原密码错误";
                }else{
                    // 修改密码
                    int rows = userDao.updateUserPwd(newPassword,id);
                    if (rows>0){
                        req.setAttribute("error","身份信息已过期, 请重新登录");
                        req.getRequestDispatcher("/loginOut.action").forward(req, resp);
                        return;
                    }else{
                        tips = "修改失败";
                    }
                }
                req.setAttribute("tips",tips);
                req.setAttribute("mainRight", "/jsp/passwordChange.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            default:
        }


    }
}
