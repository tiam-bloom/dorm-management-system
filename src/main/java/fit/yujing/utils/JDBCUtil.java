package fit.yujing.utils;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * @Author Tiam
 * @Date 2022/12/27 19:00
 * @Description:
 */
public class JDBCUtil {
    // 获取配置文件
    static ResourceBundle bundle;
    static String driverClass;
    static String url;
    static String username;
    static String password;

    static {
        bundle = ResourceBundle.getBundle("jdbc"); // 无文件后缀
        driverClass = bundle.getString("driverClassName");
        url = bundle.getString("url");
        username = bundle.getString("username");
        password = bundle.getString("password");
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(ResultSet rs, Statement st, Connection co){
        try {
            if(rs!=null) rs.close();
            if(st!=null) st.close();
            if(co!=null) co.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}