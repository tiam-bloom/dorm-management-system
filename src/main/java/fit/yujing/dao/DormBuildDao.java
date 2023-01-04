package fit.yujing.dao;

import fit.yujing.pojo.DormBuild;

import java.util.List;

/**
 * @Author Tiam
 * @Date 2023/1/2 21:12
 * @Description:
 */
public interface DormBuildDao {
    /**
     * 查询全部宿舍
     * @return
     */
    List<DormBuild> findAllBuilds();
    /**
     * 根据userId查询其管理的宿舍楼
     * @param userId
     * @return
     */
    List<DormBuild> findDormBuildsByUserId(Integer userId);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    DormBuild findDormBuildById(Integer id);

    /**
     * 修改激活状态
     * @param disabled
     * @param id
     * @return
     */
    int updateBuildDisabled(String disabled, String id);

    /**
     * 修改
     * @param dormBuild
     * @return
     */
    int updateDormBuild(DormBuild dormBuild);

    /**
     * 删除
     * @param dormBuild
     * @return
     */
    int addDormBuild(DormBuild dormBuild);
}
