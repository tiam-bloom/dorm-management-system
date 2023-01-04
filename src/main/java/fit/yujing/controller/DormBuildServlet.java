package fit.yujing.controller;

import fit.yujing.dao.DormBuildDao;
import fit.yujing.dao.impl.DormBuildDaoImpl;
import fit.yujing.pojo.DormBuild;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2023/1/2 23:13
 * @Description:
 */
@Slf4j
@WebServlet("/dormBuild.action")
public class DormBuildServlet extends HttpServlet {
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        req.setCharacterEncoding("utf-8");
        switch (action) {
            // 展示全部
            case "list":
                showBuildsList(req, resp);
                break;
            case "deleteOrAcive":
                String disabled = req.getParameter("disabled");
                String id = req.getParameter("id");
                int rows = dormBuildDao.updateBuildDisabled(disabled, id);
                req.setAttribute("tips", rows > 0 ? "成功" : "失败");
                showBuildsList(req, resp);
            case "preAdd":
                req.setAttribute("mainRight", "/jsp/dormBuildAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            case "preUpdate":
                id = req.getParameter("id");
                DormBuild build = dormBuildDao.findDormBuildById(Integer.parseInt(id));
                req.setAttribute("build", build);
                req.setAttribute("mainRight", "/jsp/dormBuildAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            default:
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        switch (action) {
            // 查询
            case "list":
                String id = req.getParameter("id");
                DormBuild dormBuild = dormBuildDao.findDormBuildById(Integer.parseInt(id));
                List<DormBuild> builds = new ArrayList<>(Collections.singleton(dormBuild));
                showBuildsList(req, resp, builds);
                break;
            case "save":
                dormBuild = new DormBuild();
                dormBuild.setName(req.getParameter("name"));
                dormBuild.setRemark(req.getParameter("remark"));
                id = req.getParameter("id");
                log.info("封装参数: {}",dormBuild);
                String tips;
                if (id != null && !"".equals(id)) {
                    // 修改
                    dormBuild.setId(Integer.parseInt(id));
                    int rows = dormBuildDao.updateDormBuild(dormBuild);
                    tips = rows>0?"修改成功":"修改失败";
                }else{
                    int rows = dormBuildDao.addDormBuild(dormBuild);
                    tips = rows>0?"添加成功":"添加失败";
                }
                req.setAttribute("tips",tips);
                showBuildsList(req, resp);
                break;
            default:
        }
    }

    private void showBuildsList(HttpServletRequest req, HttpServletResponse resp, List<DormBuild>... builds) throws ServletException, IOException {
        List<DormBuild> buildSelects = dormBuildDao.findAllBuilds();
        req.setAttribute("builds", builds.length == 0 ? buildSelects : builds[0]);
        req.setAttribute("buildSelects", buildSelects);
        req.setAttribute("mainRight", "/jsp/dormBuildList.jsp");
        req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
    }
}
