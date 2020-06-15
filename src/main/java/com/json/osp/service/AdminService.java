package com.json.osp.service;

import com.json.osp.dto.Assistant;
import com.json.osp.entity.BoughtRecord;
import com.json.osp.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    /**
     * 获取销售人员信息
     * @return
     */
    public List<Assistant> getAssistantInfo();

    /**
     * 删除销售人员
     * @param userId
     */
    void deleteOneAssistant(Long userId);

    /**
     * 修改销售人员信息
     * @param assistant
     */
    void modifyAssistant(Assistant assistant);

    List<BoughtRecord> getBoughtRecord(long productCategoryId);

    List<Product> getProductByProductCategoryId(long productCategoryId);
}
