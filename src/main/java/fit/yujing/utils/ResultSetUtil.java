package fit.yujing.utils;

import fit.yujing.pojo.DormBuild;
import fit.yujing.pojo.Record;
import fit.yujing.pojo.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/29 10:39
 * @Description:
 */
public class ResultSetUtil {
    public static Record ResultSetToRecord(ResultSet rs) throws SQLException {
        return new Record(rs.getInt(1),
                rs.getInt(2),
                rs.getDate(3),
                rs.getString(4),
                rs.getInt(5),
                null);
    }
    public static List<Record> ResultSetToRecords(ResultSet rs) throws SQLException {
        List<Record> records = new ArrayList<>();
        while (rs.next()) {
            records.add(ResultSetToRecord(rs));
        }
        return records;
    }
    public static DormBuild ResultSetToDormBuild(ResultSet rs) throws SQLException {
        return new DormBuild(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getInt(4));
    }

    public static User ResultSetToUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getString(7),
                rs.getInt(8),
                rs.getInt(9),
                rs.getInt(10),
                rs.getInt(11),
                null,
                null);
    }

    public static List<User> ResultSetToUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(ResultSetToUser(rs));
        }
        return users;
    }

    public static List<DormBuild> ResultSetToDormBuilds(ResultSet rs) throws SQLException {
        List<DormBuild> dormBuilds = new ArrayList<>();
        while (rs.next()) {
            dormBuilds.add(ResultSetToDormBuild(rs));
        }
        return dormBuilds;
    }
}
