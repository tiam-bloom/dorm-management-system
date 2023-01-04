package fit.yujing.dao;

import fit.yujing.pojo.User;

import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/27 19:06
 * @Description:
 */
public interface UserDao {
    /**
     * 根据学号查询
     * @param stuCode
     * @return
     */
    User findByStuCode(String stuCode);


    /**
     * 根据 姓名 / 性别 / 电话 模糊查询
     * @param searchType
     * @param keyword
     * @return
     */
    List<User> findUsersBySearchType(String searchType,String keyword);

    /**
     * 添加管理员
     * @param user
     * @return 受影响的行数
     */
    int addDormManager(User user);

    /**
     * 查询所有身份为 roleId的用户
     * @param roleId
     * @return
     */
    List<User> findAllUsers(Integer roleId);

    /**
     * 联表查询所有学生 roleId=2
     * @return
     */
    List<User> findAllUsers();


    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findUserById(String id);

    /**
     * 更新 User
     * @param user
     * @return
     */
    int updateDormManager(User user);

    /**
     * 更新激活状态
     * @param disabled
     * @param id
     * @return
     */
    int updateUserDisabled(int disabled, String id);

    /**
     * 根据id修改密码
     * @param newPassword
     * @param id
     * @return
     */
    int updateUserPwd(String newPassword, Integer id);

    /**
     * 模糊查询
     * @param searchType
     * @param keyword
     * @param dormBuildId
     * @return
     */
    List<User> findUsersBySearchType(String searchType, String keyword, String dormBuildId);

    /**
     * 修改学生信息
     * @param user
     * @return
     */
    int updateStudent(User user);

    /**
     * 添加学生信息
     * @param user
     * @return
     */
    int addStudent(User user);

    /**
     * 根据学号查询
     * @param stuCode
     * @return
     */
    User findStudentByStuCode(String stuCode);
}
