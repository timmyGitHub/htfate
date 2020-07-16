package com.htfate.databasecenter.amain.service.dofield;


import com.baomidou.mybatisplus.extension.service.IService;
import com.htfate.databasecenter.amain.entity.po.DOField;

import java.util.List;
import java.util.Map;

/**
 * 模板管理中的表信息
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-04-02
 */
public interface DOFieldService extends IService<DOField> {

    /**
     * list查询
     *
     * @param params 参数
     */
    List<DOField> getList(Map<String, Object> params);

    /**
     * list 模糊查询
     *
     * @param params 参数
     */
    List<DOField> getListLike(Map<String, Object> params);

    /**
     * 新增
     *
     * @param obj 对象
     */
//    void save(DOField obj);

    /**
     * 批量新增
     *
     * @param list 对象
     */
//    int batchSave(List<DOField> list);

    /**
     * 修改
     *
     * @param obj 对象
     */
//    void update(DOField obj);

    /**
     * 批量修改
     *
     * @param list 实体对象
     */
//    void batchUpdate(List<DOField> list);

    /**
     * 删除
     *
     * @param ids 多个 id 用逗号隔开
     */
    void deleteByIds(String ids);

    /**
     * 条件删除
     *
     * @param params 条件, 一个 name 中若有多个值，用逗号隔开，例：map.put('id','123,312,231,312')
     */
    void deleteByMap(Map<String, Object> params);

    /**
     * 通过id返回对象
     *
     * @param id id
     */
//    DOField getObjById(String id);

    /**
     * 通过ids返回对象
     *
     * @param ids ids
     */
    List<DOField> getObjByIds(String ids);
}
