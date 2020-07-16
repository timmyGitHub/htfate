package com.htfate.databasecenter.amain.service.dofield.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htfate.databasecenter.amain.service.dofield.DOFieldService;
import com.htfate.databasecenter.amain.entity.po.DOField;
import com.htfate.databasecenter.amain.mapper.DOFieldDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 模板管理中的表信息
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-04-02
 */
@Service
public class DOFieldServiceImpl extends ServiceImpl<DOFieldDao, DOField> implements DOFieldService {
    @Autowired
    private DOFieldDao dofieldDao;

    /**
     * list查询
     *
     * @param params 参数
     */
    @Override
    public List<DOField> getList(Map<String, Object> params) {
        //查询数据
        return dofieldDao.getList(params);
    }

    /**
     * list 模糊查询
     *
     * @param params 参数
     */
    @Override
    public List<DOField> getListLike(Map<String, Object> params) {
        //查询数据
        return dofieldDao.getListLike(params);
    }

    /**
     * 新增
     *
     * @param obj 对象
     */
    /*@Override
    public void save(DOField obj) {
        dofieldDao.save(obj);
    }

    *//**
     * 批量新增
     *
     * @param list 对象
     *//*
    @Override
    public int batchSave(List<DOField> list) {
        return dofieldDao.batchSave(list);
    }

    *//**
     * 修改
     *
     * @param obj 对象
     *//*
    @Override
    public void update(DOField obj) {
        dofieldDao.update(obj);
    }

    *//**
     * 批量修改
     *
     * @param list 对象
     *//*
    @Override
    public void batchUpdate(List<DOField> list) {
        dofieldDao.batchUpdate(list);
    }

    *//**
     * 通过id返回对象
     *//*
    @Override
    public DOField getObjById(String id) {
        return dofieldDao.getObjById(id);
    }*/

    /**
     * 通过ids返回对象
     */
    @Override
    public List<DOField> getObjByIds(String ids) {
        return dofieldDao.getObjByIds(ids);
    }

    /**
     * 删除
     *
     * @param ids 多个 id 用逗号隔开
     */
    @Override
    public void deleteByIds(String ids) {
        dofieldDao.deleteByIds(ids);
    }

    /**
     * 条件删除
     *
     * @param params 条件, 一个 name 中若有多个值，用逗号隔开，例：map.put('id','123,312,231,312')
     */
    @Override
    public void deleteByMap(Map<String, Object> params) {
        dofieldDao.deleteByMap(params);
    }
}
