package fit.yujing.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Tiam
 * @Date 2022/12/29 9:42
 * @Description:
 */
public class CookieUtil {
    public static String getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        List<Cookie> cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(name))
                .collect(Collectors.toList());
        return cookie.size() > 0 ? cookie.get(0).getValue() : "";
    }
}
