package com.htfate.wangeditor.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htfate.wangeditor.entity.RichText;
import com.htfate.wangeditor.entity.dto.PasteContext;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RichTextMapper extends BaseMapper<RichText> {

	List<PasteContext> listPaste(@Param("userId") String userId,@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
}
