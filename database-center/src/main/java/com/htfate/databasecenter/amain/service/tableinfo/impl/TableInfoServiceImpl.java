package com.htfate.databasecenter.amain.service.tableinfo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htfate.databasecenter.amain.entity.po.TableInfo;
import com.htfate.databasecenter.amain.mapper.TableInfoDao;
import com.htfate.databasecenter.amain.service.tableinfo.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 模版管理
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-04-03
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoDao, TableInfo> implements TableInfoService {
    @Autowired
    private TableInfoDao tableInfoDao;

    /**
     * list查询
     *
     * @param params 参数
     */
    @Override
    public List<TableInfo> getList(Map<String, Object> params) {
        //查询数据
        return tableInfoDao.getList(params);
    }

    /**
     * list 模糊查询
     *
     * @param params 参数
     */
    @Override
    public List<TableInfo> getListLike(Map<String, Object> params) {
        //查询数据
        return tableInfoDao.getListLike(params);
    }

    /**
     * 新增
     *
     * @param obj 对象
     *//*
    @Override
    public void save(TableInfo obj) {
        tableInfoDao.save(obj);
    }

    *//**
     * 批量新增
     *
     * @param list 对象
     *//*
    @Override
    public int batchSave(List<TableInfo> list) {
        return tableInfoDao.batchSave(list);
    }

    *//**
     * 修改
     *
     * @param obj 对象
     *//*
    @Override
    public void update(TableInfo obj) {
        tableInfoDao.update(obj);
    }

    *//**
     * 批量修改
     *
     * @param list 对象
     *//*
    @Override
    public void batchUpdate(List<TableInfo> list) {
        tableInfoDao.batchUpdate(list);
    }

    *//**
     * 通过id返回对象
     *//*
    @Override
    public TableInfo getObjById(Long id) {
        return tableInfoDao.getObjById(id);
    }

    *//**
     * 通过ids返回对象
     */
    @Override
    public List<TableInfo> getObjByIds(String ids) {
        return tableInfoDao.getObjByIds(ids);
    }

    /**
     * 删除
     *
     * @param ids 多个 id 用逗号隔开
     */
    @Override
    public void deleteByIds(String ids) {
        tableInfoDao.deleteByIds(ids);
    }

    /**
     * 条件删除
     *
     * @param params 条件, 一个 name 中若有多个值，用逗号隔开，例：map.put('id','123,312,231,312')
     *//*
    @Override
    public void deleteByMap(Map<String, Object> params) {
        tableInfoDao.deleteByMap(params);
    }*/
}
