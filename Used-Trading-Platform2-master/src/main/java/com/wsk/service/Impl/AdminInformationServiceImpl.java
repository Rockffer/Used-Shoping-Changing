package com.wsk.service.Impl;

import com.wsk.dao.AdminInformationMapper;
import com.wsk.dao.AllKindsMapper;
import com.wsk.pojo.AdminInformation;
import com.wsk.service.AdminInformationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminInformationServiceImpl implements AdminInformationService {
    @Resource
    private AdminInformationMapper adminInformationMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return adminInformationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AdminInformation record) {
        return adminInformationMapper.insert(record);
    }

    @Override
    public int insertSelective(AdminInformation record) {
        return adminInformationMapper.insertSelective(record);
    }

    @Override
    public AdminInformation selectByPrimaryKey(Integer id) {
        return adminInformationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AdminInformation record) {
        return adminInformationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AdminInformation record) {
        return adminInformationMapper.updateByPrimaryKey(record);
    }

    @Override
    public AdminInformation selectByNo(int ano) {
        return adminInformationMapper.selectByNo(ano);
    }
}
