package com.htfate.usercenter.service.impl;

import com.google.common.base.Strings;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {
	@Autowired
	private StringRedisTemplate redisTemplate;

	public void del(String key){
		if (Strings.isNullOrEmpty(key)) {
			throw new MyException("key is null");
		}
		redisTemplate.delete(key);
	}

	/******************************String 类型操作**********************************/
	public String getValue (String key) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		return redisTemplate.opsForValue().get(key);
	}

	public List<String> getList (Collection<String> key) {
		return redisTemplate.opsForValue().multiGet(key);
	}

	public void setValue (String key, String value) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (YHTUtils.isEmpty(value)) {
			throw new MyException("value is null");
		}
		redisTemplate.opsForValue().set(key, value);
	}

	/******************************List 类型操作**********************************/
	public Long leftPush (String key, String value) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (YHTUtils.isEmpty(value)) {
			throw new MyException("value is null");
		}
		return redisTemplate.opsForList().leftPush(key, value);
	}

	public Long lefPushAll (String key, Collection<String> value) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (YHTUtils.isEmpty(value)) {
			throw new MyException("value is null");
		}
		return redisTemplate.opsForList().leftPushAll(key, value);
	}

	public Long lefPushAll (String key, String... value) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (0 == value.length) {
			throw new MyException("value is null");
		}
		return redisTemplate.opsForList().leftPushAll(key, value);
	}

	public List<String> range (String key, Long index, Long size) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (null == index || 1L == index || index < 0L) {
			index = 0L;
		} else {
			index -= 1L;
		}
		if (null == size || size > 50L) {
			size = 10L;
		}
		return redisTemplate.opsForList().range(key, index, size);
	}
	/******************************Set 类型操作**********************************/

	public Boolean setSetValue(String key,String value) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (null == value) {
			throw new MyException("value is null");
		}
		return redisTemplate.opsForZSet().add(key,value,1);
	}

	public Set<String> setRange (String key, Long index, Long size) {
		if (YHTUtils.isEmpty(key)) {
			throw new MyException("key is null");
		}
		if (null == index || 1L == index || index < 0L) {
			index = 0L;
		} else {
			index -= 1L;
		}
		if (null == size || size > 50L) {
			size = 10L;
		}
		return redisTemplate.opsForZSet().range(key, index, size);
	}
}
