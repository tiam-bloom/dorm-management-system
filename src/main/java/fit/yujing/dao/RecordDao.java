package fit.yujing.dao;

import fit.yujing.pojo.Record;

import java.util.List;

/**
 * @Author Tiam
 * @Date 2023/1/2 23:32
 * @Description:
 */
public interface RecordDao {
    /**
     * 查询全部缺勤记录
     * @return
     */
    List<Record> findAllRecords();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Record findRecordById(String id);

    /**
     * 修改激活状态
     * @param disabled
     * @param id
     * @return
     */
    int updateRecordDisabled(String disabled, String id);

    /**
     * 修改缺勤记录
     * @param record
     * @return
     */
    int updateRecord(Record record);

    /**
     * 添加缺勤记录
     * @param record
     * @return
     */
    int addRecord(Record record);

    /**
     * 模糊查询
     * @param startDate
     * @param endDate
     * @param dormBuildId
     * @param searchType
     * @param keyword
     * @return
     */
    List<Record> findRecordsBySearchType(String startDate, String endDate, String dormBuildId, String searchType, String keyword);
}
