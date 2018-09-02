package com.hoperun.arcfacedemo.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisUtils {

	@Autowired
	private StringRedisTemplate template;
	
	@Autowired
	private RedisTemplate<String,Object> redisTemlate;

	/** 判断当前是否存在此key */
	public boolean isExist(String key) {
		return this.template.hasKey(key);
	}

	/** 存入数据 */
	public void set(String key, String value) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		ops.set(key, value);
	}

	/** 获取数据 */
	public String get(String key) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		return ops.get(key);
	}

	/** 删除数据 */
	public void del(String key) {
		this.template.delete(key);
	}

	/** 刷新过期时间 */
	public void expire(String key, int time) {
		this.template.expire(key, time, TimeUnit.MILLISECONDS);

	}

	/** 存入数据并设置过期时间 */
	public void save(String key, String value, int time) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		ops.set(key, value, time, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 存储对象
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveObject(String key,Object value) {
		try {  
			redisTemlate.opsForValue().set(key, value);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
	}
	
	
	/**
	 * 根据key值获取对象
	 * @param key
	 * @return
	 */
	public Object getObject(String key) {
		try {
			return redisTemlate.opsForValue().get(key);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	
}
