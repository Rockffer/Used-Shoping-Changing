package com.wsk.service;

import com.wsk.pojo.UserWant;
import com.wsk.pojo.WantContext;

import java.util.List;


public interface WantContextService {
    int deleteByPrimaryKey(Integer id);

    int insert(WantContext record);

    int insertSelective(WantContext record);

    WantContext selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WantContext record);

    int updateByPrimaryKey(WantContext record);

    List<WantContext> selectByUWid(int uwid, int start);

    int getCounts(int uwid);

    List <WantContext> selectAll();
}
