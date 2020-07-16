package com.htfate.wangeditor.service.wang;

import com.htfate.wangeditor.entity.dto.PasteContext;

import java.util.List;
import java.util.Map;

public interface WangService {
    /**
     * 保存文本信息
     * @param params 参数
     * @return 结果
     */
    boolean save(Map<String, Object> params);

    boolean savePaste(Map<String, Object> params);

    List<PasteContext> listPaste(String userId, int pageIndex, int pageSize);
}
