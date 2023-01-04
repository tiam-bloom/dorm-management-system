package fit.yujing.dao.impl;

import fit.yujing.dao.UserDao;
import fit.yujing.pojo.DormBuild;
import fit.yujing.pojo.User;
import fit.yujing.utils.JDBCUtil;
import fit.yujing.utils.ResultSetUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/27 19:06
 * @Description:
 */
public class UserDaoImpl implements UserDao {
    @Override
    public User findByStuCode(String stuCode) {
        // 获取连接
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from tb_user where stu_code = ? and disabled=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            // 注入参数
            ps.setString(1, stuCode);
            // 执行查询
            rs = ps.executeQuery();
            // 处理结果
            if (rs.next())
                return ResultSetUtil.ResultSetToUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
        return null;
    }

    @Override
    public List<User> findUsersBySearchType(String searchType, String keyword) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_user where " + searchType + " like ? and role_id = 1";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            return ResultSetUtil.ResultSetToUsers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public int addDormManager(User user) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into tb_user(name,password,sex,tel) values (?,?,?,?)";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getSex());
            ps.setString(4, user.getTel());
            rows = ps.executeUpdate();
            // 提交事务
            con.commit();
        } catch (SQLException e) {
            try {
                // 出现异常回滚
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public List<User> findAllUsers(Integer roleId) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_user where role_id = ? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, roleId);
            rs = ps.executeQuery();
            return ResultSetUtil.ResultSetToUsers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public List<User> findAllUsers() {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select u.*,d.name,r.* from tb_user u  left join tb_dormbuild d on u.dormBuildId = d.id left join tb_record r on r.student_id = u.id where role_id = 2 ";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            // 处理结果
            return getUsers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }


    @Override
    public User findUserById(String id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_user where id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            // 处理结果
            return rs.next() ? ResultSetUtil.ResultSetToUser(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public int updateDormManager(User user) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update tb_user set name = ?,password = ?,sex=?,tel = ? where id = ?";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getSex());
            ps.setString(4, user.getTel());
            ps.setInt(5, user.getId());
            rows = ps.executeUpdate();
            // 提交事务
            con.commit();
        } catch (SQLException e) {
            try {
                // 出现异常回滚
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public int updateUserDisabled(int disabled, String id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update tb_user set disabled = ? where id = ?";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setInt(1, disabled);
            ps.setString(2, id);
            rows = ps.executeUpdate();
            // 提交事务
            con.commit();
        } catch (SQLException e) {
            try {
                // 出现异常回滚
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public int updateUserPwd(String newPassword, Integer id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update tb_user set password = ? where id = ?";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, id);
            rows = ps.executeUpdate();
            // 提交事务
            con.commit();
        } catch (SQLException e) {
            try {
                // 出现异常回滚
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public List<User> findUsersBySearchType(String searchType, String keyword, String dormBuildId) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select u.*,d.name,r.* from tb_user u  left join tb_dormbuild d on u.dormBuildId = d.id " +
                "left join tb_record r on r.student_id = u.id where u." + searchType + " like ? and u.role_id = 2 and u.dormBuildId like ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + dormBuildId + "%");
            rs = ps.executeQuery();
            return getUsers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    private List<User> getUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = ResultSetUtil.ResultSetToUser(rs);
            DormBuild dormBuild = new DormBuild();
            dormBuild.setName(rs.getString("d.name"));
            user.setDormBuild(dormBuild);
            users.add(user);
        }
        return users;
    }

    @Override
    public int updateStudent(User user) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update tb_user set " +
                "name = ?," +
                "password = ?," +
                "sex=?," +
                "tel = ?," +
                "stu_code=?," +
                "dorm_code=?," +
                "dormBuildId=?" +
                " where id = ?";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getSex());
            ps.setString(4, user.getTel());
            ps.setString(5, user.getStuCode());
            ps.setString(6, user.getDormCode());
            ps.setInt(7, user.getDormBuildId());
            ps.setInt(8, user.getId());
            rows = ps.executeUpdate();
            // 提交事务
            con.commit();
        } catch (SQLException e) {
            try {
                // 出现异常回滚
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public int addStudent(User user) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into tb_user(name,password,sex,tel,stu_code,dorm_code,dormBuildId,role_id) values (?,?,?,?,?,?,?,?)";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getSex());
            ps.setString(4, user.getTel());
            ps.setString(5, user.getStuCode());
            ps.setString(6, user.getDormCode());
            ps.setInt(7, user.getDormBuildId());
            // 默认身份学生
            ps.setInt(8,2);
            rows = ps.executeUpdate();
            // 提交事务
            con.commit();
        } catch (SQLException e) {
            try {
                // 出现异常回滚
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public User findStudentByStuCode(String stuCode) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_user where stu_code = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, stuCode);
            rs = ps.executeQuery();
            // 处理结果
            return rs.next() ? ResultSetUtil.ResultSetToUser(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }
}
