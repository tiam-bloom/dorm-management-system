package fit.yujing.controller;

import fit.yujing.dao.DormBuildDao;
import fit.yujing.dao.RecordDao;
import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.DormBuildDaoImpl;
import fit.yujing.dao.impl.RecordDaoImpl;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.pojo.DormBuild;
import fit.yujing.pojo.Record;
import fit.yujing.pojo.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2023/1/2 23:29
 * @Description:
 */
@Slf4j
@WebServlet("/record.action")
public class RecordServlet extends HttpServlet {
    private UserDao userDao = new UserDaoImpl();
    private RecordDao recordDao = new RecordDaoImpl();

    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "list":
                // 查询全部
                showRecordList(req, resp);
                break;
            case "preAdd":
                req.setAttribute("mainRight", "/jsp/recordAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            case "preUpdate":
                String id = req.getParameter("id");
                Record record = recordDao.findRecordById(id);
                User user = userDao.findUserById(String.valueOf(record.getStudentId()));
                record.setUser(user);
                req.setAttribute("record", record);
                req.setAttribute("mainRight", "/jsp/recordAddOrUpdate.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            case "deleteOrAcive":
                id = req.getParameter("id");
                String disabled = req.getParameter("disabled");
                int rows = recordDao.updateRecordDisabled(disabled, id);
                req.setAttribute("tips", rows > 0 ? "成功" : "失败");
                showRecordList(req, resp);
                break;
            default:
        }
    }

    private void showRecordList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Record> records = recordDao.findAllRecords();
        for (Record record : records) {
            User user = userDao.findUserById(String.valueOf(record.getStudentId()));
            user.setDormBuild(dormBuildDao.findDormBuildById(user.getDormBuildId()));
            record.setUser(user);
        }
        List<DormBuild> builds = dormBuildDao.findAllBuilds();
        req.setAttribute("builds", builds);
        req.setAttribute("records", records);
        req.setAttribute("mainRight", "/jsp/recordList.jsp");
        req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        switch (action) {
            case "list":
                String startDate = req.getParameter("startDate");
                if (startDate == "") startDate = "0000-01-01";
                String endDate = req.getParameter("endDate");
                if (endDate == "") endDate = "9999-12-31";
                String dormBuildId = req.getParameter("dormBuildId");
                String searchType = req.getParameter("searchType");
                String keyword = req.getParameter("keyword");
                List<Record> records = recordDao.findRecordsBySearchType(startDate,endDate,dormBuildId,searchType,keyword);
                List<DormBuild> builds = dormBuildDao.findAllBuilds();
                req.setAttribute("builds", builds);
                req.setAttribute("records", records);
                req.setAttribute("mainRight", "/jsp/recordList.jsp");
                req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
                break;
            case "save":
                String stuCode = req.getParameter("stuCode");
                String date = req.getParameter("date");
                String remark = req.getParameter("remark");
                Record record = new Record();
                try {
                    record.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                } catch (ParseException e) {
                    log.error("时间转换错误, string = {}", date);
                }
                record.setRemark(remark);
                String id = req.getParameter("id");
                String tips = null;
                User user = userDao.findStudentByStuCode(stuCode);
                if (id != null && !id.equals("")) {
                    record.setId(Integer.parseInt(id));
                    if (user == null) {
                        tips = "学号不存在";
                        Record record1 = recordDao.findRecordById(id);
                        record.setStudentId(record1.getStudentId());
                    } else {
                        record.setStudentId(user.getId());
                    }
                    int rows = recordDao.updateRecord(record);
                    if (user != null) {
                        tips = rows > 0 ? "修改成功" : "修改失败";
                    }
                } else {
                    if (user == null) {
                        tips = "添加失败, 学号不存在";
                    } else {
                        record.setStudentId(user.getId());
                        int rows = recordDao.addRecord(record);
                        tips = rows > 0 ? "添加成功" : "添加失败";
                    }
                }
                req.setAttribute("tips", tips);
                showRecordList(req, resp);
                break;
            default:
        }
    }
}
