package com.json.osp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.osp.dao.RecordDao;
import com.json.osp.dto.Assistant;
import com.json.osp.entity.BoughtRecord;
import com.json.osp.entity.BrowseRecord;
import com.json.osp.entity.Product;
import com.json.osp.entity.UserInfo;
import com.json.osp.service.AdminService;
import com.json.osp.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RecordDao recordDao;

    /**
     * 加载销售人员信息
     * @param req
     * @return
     */
    @RequestMapping(value = "loadAssistantInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> loadAssistantInfo(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            List<Assistant> assistantList = adminService.getAssistantInfo();
            modelMap.put("success", true);
            modelMap.put("assistantList", assistantList);
            return modelMap;
        }catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }

    }

    /**
     * 删除销售人员
     * @param req
     * @return
     */
    @RequestMapping(value = "deleteAssistant", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> deleteAssistant(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            // 获取userId
            long userId = HttpServletRequestUtil.getLong(req, "userId");
            adminService.deleteOneAssistant(userId);
            modelMap.put("success", true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
    }

    /**
     * 修改销售人员信息
     * @param req
     * @return
     */
    @RequestMapping(value = "modifyAssistant", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyAssistant(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();

        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Assistant assistant = null;
        try {
            // 获取参数信息
            String assistantStr = HttpServletRequestUtil.getString(req, "assistant");
            assistant = objectMapper.readValue(assistantStr, Assistant.class);
            adminService.modifyAssistant(assistant);

            List<Assistant> assistantList = adminService.getAssistantInfo();
            modelMap.put("success", true);
            modelMap.put("assistantList", assistantList);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
    }

    @RequestMapping(value = "getBoughtRecord", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBoughtRecord(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            // 获取productCategoryId
            long productCategoryId = HttpServletRequestUtil.getLong(req, "productCategoryId");
            List<BoughtRecord> boughtRecordList = adminService.getBoughtRecord(productCategoryId);
            modelMap.put("success", true);
            modelMap.put("boughtRecordList",boughtRecordList);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
    }

    @RequestMapping(value = "getBoughtRecordByPcId", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBoughtRecordByPcId(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            // 获取productCategoryId
            UserInfo userInfo = (UserInfo)req.getSession().getAttribute("userInfo");
            long productCategoryId = userInfo.getProductCategory().getProductCategoryId();
            List<BoughtRecord> boughtRecordList = adminService.getBoughtRecord(productCategoryId);
            modelMap.put("success", true);
            modelMap.put("boughtRecordList",boughtRecordList);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
    }


    @RequestMapping(value = "getBrowseRecordByproductCategory", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBrowseRecordByproductCategory(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            UserInfo userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            List<BrowseRecord> browseRecordList = recordDao.queryBrowseRecord(null, userInfo.getProductCategory().getProductCategoryId());
            modelMap.put("success", true);
            modelMap.put("browseRecordList",browseRecordList);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }

    }

    @RequestMapping(value = "getProductByProductCategoryId", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductByProductCategoryId(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            // 获取productCategoryId
            UserInfo userInfo = (UserInfo)req.getSession().getAttribute("userInfo");
            long productCategoryId = userInfo.getProductCategory().getProductCategoryId();
            List<Product> productList = adminService.getProductByProductCategoryId(productCategoryId);
            modelMap.put("success", true);
            modelMap.put("productList",productList);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
    }


}
