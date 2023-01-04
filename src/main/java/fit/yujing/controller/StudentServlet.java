package fit.yujing.controller;

import fit.yujing.dao.DormBuildDao;
import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.DormBuildDaoImpl;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.pojo.DormBuild;
import fit.yujing.pojo.User;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/30 13:23
 * @Description:
 */

@Slf4j
@WebServlet("/student.action")
public class StudentServlet extends HttpServlet {
    private UserDao userDao = new UserDaoImpl();
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            // 展示全部
            case "list":
                showStudentList(req, resp);
                break;
            // 删除
            case "deleteOrActive":
                String id = req.getParameter("id");
                String disabled = req.getParameter("disabled");
                int rows = userDao.updateUserDisabled(Integer.parseInt(disabled), id);
                req.setAttribute("tips", rows > 0 ? "成功!" : "失败!");
                showStudentList(req, resp);
                break;
            // 添加界面
            case "preAdd":
                List<DormBuild> builds = dormBuildDao.findAllBuilds();
                req.setAttribute("builds", builds);
                req.setAttribute("mainRight", "/jsp/studentAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            // 修改界面
            case "preUpdate":
                // 填充参数
                id = req.getParameter("id");
                User userUpdate = userDao.findUserById(id);
                req.setAttribute("userUpdate", userUpdate);
                builds = dormBuildDao.findAllBuilds();
                req.setAttribute("builds", builds);
                req.setAttribute("mainRight", "/jsp/studentAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            default:
        }
    }

    private void showStudentList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> students = userDao.findAllUsers();
        req.setAttribute("students", students);
        List<DormBuild> builds = dormBuildDao.findAllBuilds();
        req.setAttribute("builds", builds);
        req.setAttribute("mainRight", "/jsp/studentList.jsp");
        req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        switch (action) {
            // 查询
            case "list":
                String searchType = req.getParameter("searchType");
                String keyword = req.getParameter("keyword");
                String dormBuildId = req.getParameter("dormBuildId");
                log.info("参数1:{}, 参数2:{}, 参数3:{}", searchType, keyword, dormBuildId);
                List<User> students = userDao.findUsersBySearchType(searchType, keyword, dormBuildId);
                log.info("查询结果: {}", students);
                req.setAttribute("students", students);
                List<DormBuild> builds = dormBuildDao.findAllBuilds();
                req.setAttribute("builds", builds);
                req.setAttribute("mainRight", "/jsp/studentList.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            // 保存
            case "save":
                // 有id提交修改, 无提交添加
                User user = new User();
                user.setStuCode(req.getParameter("stuCode"));
                user.setName(req.getParameter("name"));
                user.setSex(req.getParameter("sex"));
                user.setTel(req.getParameter("tel"));
                user.setPassword(req.getParameter("password"));
                user.setDormBuildId(Integer.parseInt(req.getParameter("dormBuildId")));
                user.setDormCode(req.getParameter("dormCode"));
                String id = req.getParameter("id");
                String tips;
                if (id != null && !"".equals(id)) {
                    // 修改
                    user.setId(Integer.parseInt(id));
                    int rows = userDao.updateStudent(user);
                    tips = rows > 0 ? "修改成功" : "修改失败";
                } else {
                    // 添加
                    int rows = userDao.addStudent(user);
                    tips = rows > 0 ? "添加成功" : "添加失败";
                }
                req.setAttribute("tips", tips);
                showStudentList(req, resp);
                break;
            default:
        }
    }
}
