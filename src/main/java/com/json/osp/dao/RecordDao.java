package com.json.osp.dao;

import com.json.osp.entity.BoughtRecord;
import com.json.osp.entity.BrowseRecord;
import com.json.osp.entity.LoginLogoutInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecordDao {
    int insertBoughtRecord(BoughtRecord boughtRecord);

    List<BoughtRecord> queryBoughtRecord(@Param("userId") Long userId,
                           @Param("productCategoryId")Long productCategoryId);

    int insertBrowseRecord(BrowseRecord browseRecord);

    List<BrowseRecord> queryBrowseRecord(@Param("userId")Long userId,
                                         @Param("productCategoryId")Long productCategoryId);

    int insertLog(LoginLogoutInfo loginLogoutInfo);
}
