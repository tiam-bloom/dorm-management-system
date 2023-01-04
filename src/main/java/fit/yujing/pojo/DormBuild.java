package fit.yujing.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Tiam
 * @Date 2022/12/27 12:42
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DormBuild {
    private Integer id;
    private String name;
    private String remark;
    private Integer disabled;
}
