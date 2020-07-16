package com.htfate.databasecenter.amain.service.manageCenter;


import com.htfate.databasecenter.amain.entity.po.TableInfo;
import com.htfate.databasecenter.amain.entity.vo.VOTableAndField;

import java.util.List;
import java.util.Map;

/**
 * 模板管理接口
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-01-14
 */
public interface ModelManageService {
    // 创建模板
    String create(int sign, Long params);

    /**
     * 保存表信息
     *
     * @param flag   0-保存1-保存修改
     * @param params 参数
     * @return 结果
     */
    String save(Long flag, Map<String, Object> params);

    /**
     * 删除表单
     *
     * @param ids tableIds
     * @return 结果
     */
    String deleteTable(String ids);

    /**
     * 查找表单
     *
     * @return 集合
     */
    List<TableInfo> search(Map<String, Object> params);

    /**
     * 根据 tableId 查找数据
     *
     * @param id tableId
     * @return 表和表字段信息
     */
    VOTableAndField formInfo(Long id);

    Object test();
}
