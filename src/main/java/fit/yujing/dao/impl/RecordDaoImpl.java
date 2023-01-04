package fit.yujing.dao.impl;

import fit.yujing.dao.RecordDao;
import fit.yujing.pojo.DormBuild;
import fit.yujing.pojo.Record;
import fit.yujing.pojo.User;
import fit.yujing.utils.JDBCUtil;
import fit.yujing.utils.ResultSetUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2023/1/2 23:32
 * @Description:
 */
public class RecordDaoImpl implements RecordDao {
    @Override
    public List<Record> findAllRecords() {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_record";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            return ResultSetUtil.ResultSetToRecords(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public Record findRecordById(String id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from tb_record where id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs.next() ? ResultSetUtil.ResultSetToRecord(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }

    @Override
    public int updateRecordDisabled(String disabled, String id) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        int rows = 0;
        String sql = "update tb_record set disabled = ? where id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, disabled);
            ps.setString(2, id);
            rows = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public int updateRecord(Record record) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        int rows = 0;
        String sql = "update tb_record set student_id = ?,date = ?,remark = ? where id = ?";
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setInt(1,record.getStudentId());
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(record.getDate()));
            ps.setString(3, record.getRemark());
            ps.setInt(4, record.getId());
            rows = ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public int addRecord(Record record) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        int rows = 0;
        String sql = "insert into tb_record(student_id,date,remark,disabled) values (?,?,?,?)";
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setInt(1,record.getStudentId());
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(record.getDate()));
            ps.setString(3, record.getRemark());
            ps.setInt(3, 1);
            rows = ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(null, ps, con);
        }
        return rows;
    }

    @Override
    public List<Record> findRecordsBySearchType(String startDate, String endDate, String dormBuildId, String searchType, String keyword) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select r.*,u.stu_code,u.name,u.sex,u.dorm_code,d.name from tb_record r " +
                "join tb_user u on r.student_id = u.id join tb_dormbuild d on u.dormBuildId = d.id " +
                "where r.date >= ? and r.date <= ? and u.dormBuildId like ? and u."+searchType+" like ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,startDate);
            ps.setString(2,endDate);
            ps.setString(3, '%'+dormBuildId+'%');
            ps.setString(4,'%'+keyword+'%');
            rs = ps.executeQuery();
            List<Record> records = new ArrayList<>();
            while (rs.next()) {
                Record record = ResultSetUtil.ResultSetToRecord(rs);
                User user = new User();
                user.setStuCode(rs.getString("u.stu_code"));
                user.setName(rs.getString("u.name"));
                user.setSex(rs.getString("u.sex"));
                user.setDormCode(rs.getString("u.dorm_code"));
                DormBuild dormBuild = new DormBuild();
                dormBuild.setName(rs.getString("d.name"));
                user.setDormBuild(dormBuild);
                record.setUser(user);
                records.add(record);
            }
            return records;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(rs, ps, con);
        }
    }
}
