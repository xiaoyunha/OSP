package com.json.osp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.osp.dto.ImageHolder;
import com.json.osp.entity.*;
import com.json.osp.service.ProductService;
import com.json.osp.util.HttpServletRequestUtil;
import org.apache.commons.collections.iterators.ObjectGraphIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 添加商品
     * @param req
     * @return
     */
    @RequestMapping(value = "/addProduct" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取参数信息
        String productStr = HttpServletRequestUtil.getString(req, "product");
        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        try {
            product = objectMapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "获取不到Product");
            return modelMap;
        }
        // 获取上传的商品图片
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                req.getSession().getServletContext());
        try {
            // 若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if (multipartResolver.isMultipart(req)) {

                handleImage(req, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }

        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "文件流出现错误");
            return modelMap;
        }

        if(product != null && productImgList.size() > 0) {
            try{
                productService.addProduct(product, productImgList);
                modelMap.put("success", true);
                return modelMap;
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", "添加商品失败");
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "获取不到Product");
        return modelMap;
    }

        @RequestMapping(value = "/modifyProduct" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取参数信息
        String productStr = HttpServletRequestUtil.getString(req, "product");
        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        try {
            product = objectMapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "获取不到Product");
            return modelMap;
        }
        if(product != null) {
            try{
                productService.modifyProduct(product);
                modelMap.put("success", true);
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "修改商品信息失败");
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "获取不到Product");
        return modelMap;
    }

    @RequestMapping(value = "/getProductInfo" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductInfo(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取参数信息
        long pId = HttpServletRequestUtil.getLong(req, "productId");
        long pcId = HttpServletRequestUtil.getLong(req, "productCategoryId");
        Long productId = pId == -1L ? null : pId;
        Long productCategoryId = pcId == -1L ? null : pcId;
        List<Product> productList = null;
        try{
            productList = productService.getProductList(productId, productCategoryId);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        modelMap.put("success", true);
        modelMap.put("productList", productList);
        return modelMap;
    }

    /**
     * 加入购物车
     * @param req
     * @return
     */
    @RequestMapping(value = "/addShoppingCar" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addShoppingCar(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取参数信息
        String productStr = HttpServletRequestUtil.getString(req, "product");
        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        try {
            product = objectMapper.readValue(productStr, Product.class);
            // 一次只能添加一件
            product.setCount(1);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "获取不到Product");
            return modelMap;
        }

        // 从session中获取购物车
        List<Product>shoppingCar = (List)req.getSession().getAttribute("shoppingCar");
        shoppingCar.add(product);
        req.getSession().setAttribute("shoppingCar", shoppingCar);
        modelMap.put("success", true);
        return modelMap;
    }

    /**
     * 加载购物车
     * @param req
     * @return
     */
    @RequestMapping(value = "/loadShoppingCar" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> loadShoppingCar(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        try{
            List<Product> shoppingCar = (List<Product>) req.getSession().getAttribute("shoppingCar");
            modelMap.put("shoppingCar", shoppingCar);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }

    /**
     * 购买一件商品
     * @param req
     * @return
     */
    @RequestMapping(value = "/buyOne" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> buyOne(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        List<Product> shoppingCar = null;
        UserInfo userInfo = null;
        int index = -1;
        try{
            shoppingCar = (List<Product>) req.getSession().getAttribute("shoppingCar");
            userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            index = HttpServletRequestUtil.getInt(req,"index");
            if (index == -1 || shoppingCar == null || userInfo  == null)
                throw  new Exception();
        } catch (Exception e) {
            modelMap.put("success", 5);
            return modelMap;
        }
        Product buyOne = shoppingCar.get(index);
        int success = 1;
        try {
            success = productService.buyOne(buyOne, userInfo);
        } catch (Exception e) {
            modelMap.put("success", 5);
            return modelMap;
        }
        if (success == 1) {
            shoppingCar.remove(index);
            // 更新session
            req.getSession().setAttribute("userInfo", userInfo);
            req.getSession().setAttribute("shoppingCar", shoppingCar);
        }

        modelMap.put("success", success);
        modelMap.put("shoppingCar", shoppingCar);
        return modelMap;
    }

    @RequestMapping(value = "/buyAll" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> buyAll(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        List<Product> shoppingCar = null;
        UserInfo userInfo = null;
        try{
            shoppingCar = (List<Product>) req.getSession().getAttribute("shoppingCar");
            userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            if (shoppingCar == null || userInfo  == null)
                throw  new Exception();
        } catch (Exception e) {
            modelMap.put("success", 4);
            return modelMap;
        }
        int success = 1;
        try {
            success = productService.buyAll(shoppingCar, userInfo);
        } catch (Exception e) {
            modelMap.put("success", 4);
            return modelMap;
        }
        if(success == 1) {
            // 清空购物车
            shoppingCar  = new ArrayList<>();
            // 更新session
            req.getSession().setAttribute("userInfo", userInfo);
            req.getSession().setAttribute("shoppingCar", shoppingCar);
        }
        modelMap.put("success", success);
        modelMap.put("shoppingCar", shoppingCar);
        return modelMap;
    }

    /**
     * 删除购物车商品
     * @param req
     * @return
     */
    @RequestMapping(value = "/deleteOne" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> deleteOne(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        List<Product> shoppingCar = null;
        int index = -1;
        try{
            shoppingCar = (List<Product>) req.getSession().getAttribute("shoppingCar");
            index = HttpServletRequestUtil.getInt(req,"index");
            if (index == -1 || shoppingCar == null)
                throw  new Exception();
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
        shoppingCar.remove(index);
        req.getSession().setAttribute("shoppingCar", shoppingCar);
        modelMap.put("shoppingCar", shoppingCar);
        modelMap.put("success", true);
        return modelMap;
    }

    @RequestMapping(value = "/getBoughtRecord" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBoughtRecord(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        UserInfo userInfo = null;
        try{
            userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            if (userInfo == null)
                throw  new Exception();
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
        List<BoughtRecord> boughtRecordList = productService.getBoughtRecord(userInfo);
        for(BoughtRecord boughtRecord : boughtRecordList) {
            boughtRecord.getProduct().setCount(1);
        }
        modelMap.put("boughtRecordList", boughtRecordList);
        modelMap.put("success", true);
        return modelMap;
    }

    /**
     * 添加浏览记录
     * @param req
     * @return
     */
    @RequestMapping(value = "/addBrowseRecord" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addBrowseRecord(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        UserInfo userInfo = null;
        // 获取参数信息
        String productStr = HttpServletRequestUtil.getString(req, "product");
        // 转换成对应实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        try {
            product = objectMapper.readValue(productStr, Product.class);
            userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            if(userInfo == null || product == null)
                throw new Exception();
            productService.addBrowseRecord(userInfo, product);
            modelMap.put("success", true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "获取不到Product");
            return modelMap;
        }
    }

    /**
     * 浏览记录
     * @param req
     * @return
     */
    @RequestMapping(value = "/getBrowseRecord" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBrowseRecord(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        UserInfo userInfo = null;
        try{
            userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            if (userInfo == null)
                throw  new Exception();
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
        List<BrowseRecord> browseRecordList = productService.getBrowseRecord(userInfo);
        modelMap.put("browseRecordList", browseRecordList);
        modelMap.put("success", true);
        return modelMap;
    }

    /**
     * 推荐系统
     * @param req
     * @return
     */
    @RequestMapping(value = "/getRecommendProduct" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getRecommendProduct(HttpServletRequest req) {
        Map<String, Object> modelMap = new HashMap<>();
        UserInfo userInfo = null;
        try{
            userInfo = (UserInfo) req.getSession().getAttribute("userInfo");
            if (userInfo == null)
                throw  new Exception();
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }
        try {
            List<Product> productList = productService.RecommendProduct(userInfo);
            modelMap.put("productList", productList);
            modelMap.put("success", true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            return modelMap;
        }

    }

    /**
     * 从文件流中取出图片，构建productImgList
     * @param request
     * @param productImgList
     * @return
     * @throws IOException
     */
    private void handleImage(HttpServletRequest request, List<ImageHolder> productImgList)
            throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        List<MultipartFile> multipartFiles = multipartRequest.getFiles("files");
        for(MultipartFile item : multipartFiles) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile)item;
            if (productImgFile != null) {
                // 若取出的第i个详情图片文件流不为空，则将其加入详情图列表
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
                        productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                // 若取出的第i个详情图片文件流为空，则终止循环
                break;
            }
        }
    }
}
