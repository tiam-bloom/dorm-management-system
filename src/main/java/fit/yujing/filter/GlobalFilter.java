package fit.yujing.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author Tiam
 * @Date 2022/12/27 13:00
 * @Description: 拦截器
 */
@Slf4j
@WebFilter(urlPatterns = "*")
public class GlobalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        Object user = session.getAttribute("session_user");
        // 获取请求地址
        String requestURI = req.getRequestURI();
        log.info("拦截请求: {}", requestURI);
        switch (requestURI) {
            // 1. 请求登录页面, 有session记录, 直接跳转主页
            case "/":
                if (user != null) req.getRequestDispatcher("/index.action").forward(req, resp);
                break;
            // 2. 请求其他页面, 未session记录, 重定向到登录页面
            default:
                if (user == null) {
                    req.setAttribute("error", "登录身份过期, 请重新登录");
                    req.getRequestDispatcher("/loginOut.action").forward(req, resp);
                }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
