package com.wsk.service;

import com.wsk.pojo.AdminInformation;

public interface AdminInformationService {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminInformation record);

    int insertSelective(AdminInformation record);

    AdminInformation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminInformation record);

    int updateByPrimaryKey(AdminInformation record);

    AdminInformation selectByNo(int ano);
}
