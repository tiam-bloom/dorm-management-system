package fit.yujing.service.impl;

import fit.yujing.dao.UserDao;
import fit.yujing.dao.impl.UserDaoImpl;
import fit.yujing.service.UserService;

/**
 * @Author Tiam
 * @Date 2022/12/27 21:54
 * @Description:
 */
public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

}
