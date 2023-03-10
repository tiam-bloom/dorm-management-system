package fit.yujing.controller;

import fit.yujing.dao.DormBuildDao;
import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.DormBuildDaoImpl;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.pojo.DormBuild;
import fit.yujing.pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/29 10:23
 * @Description:
 */
@WebServlet("/dormManager.action")
public class DormManagerServlet extends HttpServlet {
    private UserDao userDao = new UserDaoImpl();
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "list":
                List<User> users = userDao.findAllUsers(1);
                showDormManagerList(req, resp, users);
                break;
            case "preAdd":
                req.setAttribute("mainRight", "/jsp/dormManagerAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            case "preUpdate":
                String id = req.getParameter("id");
                User user = userDao.findUserById(id);
                req.setAttribute("user", user);
                List<DormBuild> builds = dormBuildDao.findDormBuildsByUserId(Integer.parseInt(id));
                req.setAttribute("builds", builds);
                req.setAttribute("mainRight", "/jsp/dormManagerAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            case "deleteOrAcive":
                String disabled = req.getParameter("disabled");
                id = req.getParameter("id");
                int rows = userDao.updateUserDisabled(Integer.parseInt(disabled), id);
                String tips;
                if ("0".equals(disabled)) {
                    tips = rows > 0 ? "????????????" : "????????????";
                } else {
                    tips = rows > 0 ? "????????????" : "????????????";
                }
                req.setAttribute("tips", tips);
                showDormManagerList(req, resp, userDao.findAllUsers(1));
                break;
            default:

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        switch (action) {
            // ??????
            case "list":
                String searchType = req.getParameter("searchType");
                String keyword = req.getParameter("keyword");
                List<User> users = userDao.findUsersBySearchType(searchType, keyword);
                req.setAttribute("keyword", keyword);
                showDormManagerList(req, resp, users);
                break;
            // ??????
            case "save":
                String name = req.getParameter("name");
                String passWord = req.getParameter("passWord");
                String sex = req.getParameter("sex");
                String tel = req.getParameter("tel");
                String id = req.getParameter("id");
                // ????????????
                User user = new User();
                user.setName(name);
                user.setPassword(passWord);
                user.setSex(sex);
                user.setTel(tel);
                // ??????
                String tips;
                if (id != null && !"".equals(id)) {
                    user.setId(Integer.parseInt(id));
                    int i = userDao.updateDormManager(user);
                    tips = i > 0 ? "????????????!" : "????????????!";
                } else {
                    // ??????
                    int i = userDao.addDormManager(user);
                    tips = i > 0 ? "????????????!" : "????????????!";
                }
                req.setAttribute("tips", tips);
                showDormManagerList(req, resp, userDao.findAllUsers(1));
                break;
            default:
        }


    }

    private void showDormManagerList(HttpServletRequest req, HttpServletResponse resp, List<User> users) throws ServletException, IOException {
        for (User user : users) {
            user.setDormBuilds(dormBuildDao.findDormBuildsByUserId(user.getId()));
        }
        req.setAttribute("mainRight", "/jsp/dormManagerList.jsp");
        req.setAttribute("users", users);
        req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
    }
}
