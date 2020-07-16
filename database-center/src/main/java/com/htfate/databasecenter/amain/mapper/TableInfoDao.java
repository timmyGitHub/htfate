package com.htfate.databasecenter.amain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htfate.databasecenter.amain.entity.po.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 模版管理
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-04-03
 */
//@Repository
public interface TableInfoDao extends BaseMapper<TableInfo> {

    /**
     * list查询
     *
     * @param params 参数
     * @return 返回的数据
     */
    List<TableInfo> getList(Map<String, Object> params);

    /**
     * list 模糊查询
     *
     * @param params 参数
     * @return 返回的数据
     */
    List<TableInfo> getListLike(Map<String, Object> params);

    /**
     * 总数
     *
     * @param params 参数
     * @return 数量
     */
    int getCount(Map<String, Object> params);

    /**
     * 总数
     *
     * @param params 参数
     * @return 数量
     */
    int getCountLike(Map<String, Object> params);

    /**
     * 管理员 list查询
     *
     * @param params 参数
     * @return 返回的数据
     */
    List<TableInfo> getAdminList(Map<String, Object> params);

    /**
     * 管理员 list 模糊查询
     *
     * @param params 参数
     * @return 返回的数据
     */
    List<TableInfo> getAdminListLike(Map<String, Object> params);

    /**
     * 管理员 总数
     *
     * @param params 参数
     * @return 数量
     */
    int getAdminCount(Map<String, Object> params);

    /**
     * 管理员 总数
     *
     * @param params 参数
     * @return 数量
     */
    int getAdminCountLike(Map<String, Object> params);

    /**
     * 新增
     *
     * @param obj 实体对象
     */
//    void save(TableInfo obj);

    /**
     * 批量新增
     *
     * @param list 实体对象
     */
//    int batchSave(List<TableInfo> list);

    /**
     * 修改
     *
     * @param obj 实体对象
     */
//    void update(TableInfo obj);

    /**
     * 批量修改
     *
     * @param list 实体对象
     */
//    void batchUpdate(List<TableInfo> list);

    /**
     * 删除
     *
     * @param ids 多个 id 用逗号隔开
     */
    void deleteByIds(@Param("ids") String ids);

    /**
     * 批量删除
     *
     * @param params 条件, 一个 name 中若有多个值，用逗号隔开，例：map.put('id','123,312,231,312')
     */
//    void deleteByMap(Map<String, Object> params);

    /**
     * 通过id返回对象
     *
     * @param id id
     * @return 对应的对象
     */
//    TableInfo getObjById(@Param(("id")) Long id);

    /**
     * 通过id返回对象
     *
     * @param ids ids 用逗号隔开
     * @return 对应的对象
     */
    List<TableInfo> getObjByIds(@Param("ids") String ids);

    /**
     * 管理员 删除
     *
     * @param ids 多个 id 用逗号隔开
     */
    void deleteAdminByIds(@Param("ids") String ids);

    /**
     * 管理员 批量删除
     *
     * @param params 条件, 一个 name 中若有多个值，用逗号隔开，例：map.put('id','123,312,231,312')
     */
    void deleteAdminByMap(Map<String, Object> params);

    /**
     * 管理员 通过id返回对象
     *
     * @param id id
     * @return 对应的对象
     */
    TableInfo getAdminObjById(@Param("id") Long id);

    /**
     * 管理员 通过id返回对象
     *
     * @param ids ids 用逗号隔开
     * @return 对应的对象
     */
    List<TableInfo> getAdminObjByIds(@Param("ids") String ids);
}  
