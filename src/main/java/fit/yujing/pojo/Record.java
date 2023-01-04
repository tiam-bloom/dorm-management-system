package fit.yujing.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Tiam
 * @Date 2022/12/27 12:53
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private Integer id;
    private Integer studentId;
    /**
     * 缺勤时间
     */
    private Date date;
    /**
     * 备注
     */
    private String remark;
    private Integer disabled;
    private User user;
}
