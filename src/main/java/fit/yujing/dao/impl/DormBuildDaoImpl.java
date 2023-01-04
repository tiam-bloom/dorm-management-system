package fit.yujing.dao.impl;

import fit.yujing.dao.DormBuildDao;
import fit.yujing.pojo.DormBuild;
import fit.yujing.utils.JDBCUtil;
import fit.yujing.utils.ResultSetUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2023/1/2 21:13
 * @Description:
 */
public class DormBuildDaoImpl implements DormBuildDao {
    @Override
    public List<DormBuild> findAllBuilds() {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_dormbuild";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            // 处理结果
            return ResultSetUtil.ResultSetToDormBuilds(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public List<DormBuild> findDormBuildsByUserId(Integer userId) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT d.* FROM tb_manage_dormbuild m  join tb_dormbuild d on m.dormBuild_id = d.id where m.user_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            // 处理结果
            return ResultSetUtil.ResultSetToDormBuilds(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public DormBuild findDormBuildById(Integer id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_dormbuild where id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            // 处理结果
            return rs.next()?ResultSetUtil.ResultSetToDormBuild(rs):null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public int updateBuildDisabled(String disabled, String id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update tb_dormbuild set disabled = ? where id = ?";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(disabled));
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
    public int updateDormBuild(DormBuild dormBuild) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update tb_dormbuild set name = ?,remark=? where id = ?";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, dormBuild.getName());
            ps.setString(2, dormBuild.getRemark());
            ps.setInt(3, dormBuild.getId());
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
    public int addDormBuild(DormBuild dormBuild) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into tb_dormbuild(name,remark,disabled) values (?,?,?)";
        int rows = 0;
        try {
            // 关闭自动提交事物
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, dormBuild.getName());
            ps.setString(2, dormBuild.getRemark());
            // 默认未激活
            ps.setInt(3, 1);
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
}
