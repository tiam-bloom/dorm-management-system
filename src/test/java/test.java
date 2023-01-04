import fit.yujing.dao.RecordDao;
import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.RecordDaoImpl;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.pojo.Record;
import fit.yujing.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/27 20:44
 * @Description:
 */
public class test {
    private UserDao userDao = new UserDaoImpl();
    private RecordDao recordDao = new RecordDaoImpl();
    @Test
    void test(){
        User user = userDao.findByStuCode("0001");
        System.out.println(user);
    }

    @Test
    void test01(){
        List<User> users = userDao.findAllUsers(1);
        System.out.println(users);
    }

    @Test
    void test02(){
        List<User> users = userDao.findUsersBySearchType("tel","1");
        System.out.println(users);
    }

    @Test
    void test03(){
        List<User> users = userDao.findUsersBySearchType("tel","1","");
        System.out.println(users);
    }

    @Test
    void test04(){
        List<Record> records = recordDao.findRecordsBySearchType("0000-01-01",
                "9999-12-31",
                "1",
                "name",
                "");
        System.out.println(records);
    }
}
