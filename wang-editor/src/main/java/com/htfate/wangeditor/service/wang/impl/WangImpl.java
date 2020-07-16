package com.htfate.wangeditor.service.wang.impl;

import com.purerland.utilcenter.exception.MyException;
import com.purerland.utilcenter.util.YHTUtils;
import com.htfate.wangeditor.dao.RichTextMapper;
import com.htfate.wangeditor.entity.RichText;
import com.htfate.wangeditor.entity.dto.PasteContext;
import com.htfate.wangeditor.service.wang.WangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WangImpl implements WangService {

    @Autowired
    private RichTextMapper richTextMapper;
    /**
     * 保存文本信息
     *
     * @param params 参数
     * @return 结果
     */
    @Override
    public boolean save(Map<String, Object> params) {
        // 校验数据
        checkData(params);
        String title = params.get("title").toString();
        String context = params.get("context").toString();
        RichText richText = new RichText();

        richText.setId(YHTUtils.getUUID());
        richText.setTitle(title);
        richText.setContext(context);
        richText.setCreateDate(new Date());
        richText.setDelflag(0);
        richText.setType(0);
        System.out.println(richText);
        int count = richTextMapper.insert(richText);
        return count > 0;
    }
    /**
     * 保存复制的文本信息
     *
     * @param params 参数
     * @return 结果
     */
    @Override
    public boolean savePaste(Map<String, Object> params) {
        // 校验数据
        YHTUtils.checkParams(params, new ArrayList<>() {{
            add("userId");
            add("context");
            add("type");
        }});
        String timmy = (String) params.get("userTimmy");

        RichText richText = new RichText();

        richText.setId(YHTUtils.getUUID());
        richText.setCreatePerson(params.get("userId").toString());
        richText.setContext(params.get("context").toString());
        richText.setCreateDate(new Date());
        richText.setDelflag(0);
        richText.setType(Integer.parseInt(params.get("type").toString()));
        if (YHTUtils.isNotEmpty(timmy)) {
            richText.setUpdatePerson(timmy);
        }
        if (YHTUtils.isNotEmpty(params.get("title"))) {
            richText.setTitle(params.get("title").toString());
        }
        int count = richTextMapper.insert(richText);
        return count > 0;
    }

    /**
     * 分页获取复制的内容
     * */
    @Override
    public List<PasteContext> listPaste (String userId, int pageIndex, int pageSize) {
        if (YHTUtils.isEmpty(userId)) {
            throw new MyException("userId is null");
        }

        int index = pageIndex - 1, size = pageSize;
        if (index < 0) {
            index = 0;
        }
        if (size <= 0 || size >= 50) {
            size = 10;
        }
        return richTextMapper.listPaste(userId,index, size);
    }

    private void checkData (Map<String, Object> params) {
        String title = (String) params.get("title");
        String context = (String) params.get("context");

        if (null == title) {
            throw new MyException("title is null");
        }
        if (null == context) {
            throw new MyException("context is null");
        }
    }
    private void checkDataPaste (Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String context = (String) params.get("context");

        if (null == userId) {
            throw new MyException("userId is null");
        }
        if (null == context) {
            throw new MyException("context is null");
        }
    }
}
