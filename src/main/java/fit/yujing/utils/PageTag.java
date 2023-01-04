package fit.yujing.utils;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author Tiam
 * @Date 2023/1/1 12:40
 * @Description: jsp自定义标签
 */
@Getter
@Setter
public class PageTag extends BodyTagSupport {
    private Integer totalNum;
    private Integer pageSize;
    private Integer pageIndex;
    private String submitUrl;

    @Override
    public int doStartTag() throws JspException {
        // TODO Auto-generated method stub
        return super.doStartTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        // TODO Auto-generated method stub
        return super.doAfterBody();
    }

    @Override
    public int doEndTag() throws JspException {
        // TODO Auto-generated method stub
        // 将属性值写入到页面显示
        PrintWriter out = null;
        try {
            out = pageContext.getResponse().getWriter();
            // 写入值并输出
            out.print(this.pageSize);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return super.doEndTag();
    }
}
