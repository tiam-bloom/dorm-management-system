package fit.yujing.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Tiam
 * @Date 2022/12/27 12:46
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String password;
    /**
     * 学号
     */
    private String stuCode;
    private String dormCode;
    private String sex;
    private String tel;
    private Integer dormBuildId;
    /**
     * 0 表示超级管理员 1宿舍管理员 2学生
     */
    private Integer roleId;
    /**
     * 创建人id
     */
    private Integer createUserId;
    private Integer disabled;
    private DormBuild dormBuild;
    private List<DormBuild> dormBuilds;
}
