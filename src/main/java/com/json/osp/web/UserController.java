package com.json.osp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.osp.dao.RecordDao;
import com.json.osp.dto.ImageHolder;
import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.LoginLogoutInfo;
import com.json.osp.entity.Product;
import com.json.osp.entity.UserInfo;
import com.json.osp.service.UserInfoService;
import com.json.osp.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserInfoService  userInfoService;
    @Autowired
    private RecordDao recordDao;


    /**
     * 创建普通用户账号，销售人员不行
     * @param req
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody // 返回json
    private Map<String, Object> createUserAuth(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();

        // 获取参数信息
        String userInfoStr = HttpServletRequestUtil.getString(req, "userInfo");
        String localAuthStr = HttpServletRequestUtil.getString(req, "localAuth");

        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = null;
        LocalAuth localAuth = null;
        try {
            userInfo = objectMapper.readValue(userInfoStr, UserInfo.class);
            localAuth = objectMapper.readValue(localAuthStr, LocalAuth.class);
        } catch (Exception e) {
            modelMap.put("success", -1);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 判断账户名是否重复
        if(userInfoService.checkUniqueUsername(localAuth.getUsername())) {
            modelMap.put("success", 0);
            modelMap.put("errMsg", "账号已存在");
            return modelMap;
        }

        // 获取上传的头像
        CommonsMultipartFile userImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                req.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) req;
            userImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        // 注册用户
        if(userInfo != null && localAuth != null) {
            try {
                ImageHolder imageHolder = null;
                if(userImg != null)
                    imageHolder = new ImageHolder(userImg.getOriginalFilename(), userImg.getInputStream());
//                // 添加用户类型为普通用户
//                userInfo.setUserType(1);
                userInfoService.addUser(userInfo, localAuth, imageHolder);
                modelMap.put("success", 1);
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", -1);
                modelMap.put("errMsg", "创建用户失败");
                e.printStackTrace();
                return modelMap;
            }
        }

        modelMap.put("success", -1);
        modelMap.put("errMsg", "请输入创建的用户信息");
        return modelMap;
    }

    /**
     * 需要用拦截器来拦截没有登录的
     * @param req
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getUserInfo(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        UserInfo userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
        LocalAuth localAuth = (LocalAuth) req.getSession().getAttribute("localAuth");
        modelMap.put("success", true);
        modelMap.put("userInfo", userInfo);
        modelMap.put("localAuth", localAuth);
        return modelMap;
    }

    /**
     * 修改用户信息
     * @param req
     * @return
     */
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    @ResponseBody // 返回json
    private Map<String, Object> modifyUserInfo(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();


        // 获取session的值
        Long userId = ((UserInfo)req.getSession().getAttribute("userInfo")).getUserId();
        Long localAuthId = ((LocalAuth)req.getSession().getAttribute("localAuth")).getLocalAuthId();
        // 获取参数信息
        String userInfoStr = HttpServletRequestUtil.getString(req, "userInfo");
        String localAuthStr = HttpServletRequestUtil.getString(req, "localAuth");

        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = null;
        LocalAuth localAuth = null;
        try {
            userInfo = objectMapper.readValue(userInfoStr, UserInfo.class);
            localAuth = objectMapper.readValue(localAuthStr, LocalAuth.class);
            userInfo.setUserId(userId);
            localAuth.setLocalAuthId(localAuthId);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 获取上传的头像
        CommonsMultipartFile userImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                req.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) req;
            userImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        // 修改用户信息
        if(userInfo != null && localAuth != null) {
            try {
                ImageHolder imageHolder = null;
                if(userImg != null)
                    imageHolder = new ImageHolder(userImg.getOriginalFilename(), userImg.getInputStream());
                userInfoService.modifyUserInfo(userInfo, localAuth, imageHolder);
                // 重新设置session
                req.getSession().setAttribute("userInfo", userInfo);
                req.getSession().setAttribute("localAuth", localAuth);
                modelMap.put("success", true);
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "修改用户信息失败");
                e.printStackTrace();
                return modelMap;
            }
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "请输入修改的信息");
        return modelMap;
    }

    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> login(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        String localAuthStr = HttpServletRequestUtil.getString(req, "localAuth");
        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = new UserInfo();
        LocalAuth localAuth = null;
        try {
            localAuth = objectMapper.readValue(localAuthStr, LocalAuth.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        localAuth = userInfoService.checkLocalAuth(localAuth);
        // 检查是否正确
        if(localAuth != null) {
            userInfo.setUserId(localAuth.getUserId());
            userInfo = userInfoService.getUserInfo(userInfo.getUserId());
            List<Product> shoppingCar = new ArrayList<>();
            // 加入到session中
            req.getSession().setAttribute("userInfo", userInfo);
            req.getSession().setAttribute("localAuth", localAuth);
            req.getSession().setAttribute("shoppingCar", shoppingCar);

            // 收集登录数据
            LoginLogoutInfo loginLogoutInfo = new LoginLogoutInfo();
            loginLogoutInfo.setIp(req.getRemoteHost());
            loginLogoutInfo.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            loginLogoutInfo.setUserId(userInfo.getUserId());
            loginLogoutInfo.setLogType("登录");
            recordDao.insertLog(loginLogoutInfo);

            modelMap.put("success", true);
            modelMap.put("userInfo", userInfo);
            return modelMap;
        }
        else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名或密码错误");
            return modelMap;
        }
    }

    /**
     * 退出登录
     * @param req
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> logout(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            UserInfo userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            // 收集登录数据
            LoginLogoutInfo loginLogoutInfo = new LoginLogoutInfo();
            loginLogoutInfo.setIp(req.getRemoteHost());
            loginLogoutInfo.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            loginLogoutInfo.setUserId(userInfo.getUserId());
            req.getSession().removeAttribute("userInfo");
            req.getSession().removeAttribute("localAuth");
            loginLogoutInfo.setLogType("登出");
            recordDao.insertLog(loginLogoutInfo);
            modelMap.put("success", true);
            modelMap.put("userInfo", null);
            return modelMap;
        }
        catch (Exception e) {
            modelMap.put("success", true);
            modelMap.put("userInfo", null);
            return modelMap;
        }
    }

    /**
     * 判断用户名是否存在
     * @param req
     * @return
     */
    @RequestMapping(value = "/hasUsername", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> hasUsername(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        String username = HttpServletRequestUtil.getString(req, "username");
        if(userInfoService.checkUniqueUsername(username))
            modelMap.put("hasUsername", true);
        else
            modelMap.put("hasUsername", false);
        return modelMap;
    }
}
