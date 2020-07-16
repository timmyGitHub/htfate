package com.htfate.wangeditor;

import com.alibaba.fastjson.JSON;
import com.purerland.utilcenter.util.YHTUtils;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestMain {
	public static void main (String[] args) {
		String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
		String result = EmojiParser.parseToUnicode(str);
		System.out.println(result);

		Map<String, Object> params = new HashMap<>();
		params.put("userId","asdf");
		YHTUtils.checkParams(params, new ArrayList<>() {{
			add("userId");
		}});
	}
}
