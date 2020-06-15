package com.json.osp.interceptor;

import com.json.osp.dao.UserDao;
import com.json.osp.entity.UserInfo;
import com.json.osp.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserDao userDao;
    /**
     * 主要做事前拦截，即用户操作发生前，改写preHandle里的逻辑，进行拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        Long visitorId = HttpServletRequestUtil.getLong(request, "visitoId");
//        // 说明是游客
//        if (visitorId != -1L) {
//            UserInfo user = userDao.queryUserInfo(2L);
//            request.getSession().setAttribute("userInfo", user);
//        }
        System.out.println("拦截请求");
        // 从session中取出用户信息来
        Object userObj = request.getSession().getAttribute("userInfo");
        if (userObj != null) {
            // 若用户信息不为空则将session里的用户信息转换成UserInfo实体类对象
            UserInfo user = (UserInfo) userObj;
            if(user.getUserId() != null) {
                return true;
            }
        }
        // 若不满足登录验证，则直接跳转到帐号登录页面
        PrintWriter out = response.getWriter();
        out.println("{\"exist\":false}");
        return false;
    }
}
