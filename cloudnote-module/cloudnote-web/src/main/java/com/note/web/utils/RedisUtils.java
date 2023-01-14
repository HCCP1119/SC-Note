package com.note.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @date 2022/12/3 21:46
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisUtils {

    public static final String ROLE_SUF = "_ROLE";

    public static final String PERMISSION_SUF="_PERMISSION";

    public static final String MAILCODE_SUF = "_MAILCODE";

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 缓存基本类型和实体类数据
     *
     * @param key  键
     * @param data 数据
     */
    public <T> void setObject(final String key, final T data) {
        redisTemplate.opsForValue().set(key, data);
    }

    /**
     * 缓存基本类型和实体类数据
     *
     * @param key     键
     * @param data    数据
     * @param timeout 过期时间
     * @param unit    单位
     */
    public <T> void setObject(final String key, final T data, final Long timeout, final TimeUnit unit) {
        redisTemplate.opsForValue().set(key, data, timeout, unit);
    }

    /**
     * 根据 key 拿到缓存中的值
     *
     * @param key 键
     * @return {@link T}
     */
    public <T> T getObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存List集合
     *
     * @param key  键
     * @param data 数据
     * @return {@link Long}
     */
    public <T> Long setList(final String key, final List<T> data) {
        Long count = redisTemplate.opsForList().rightPushAll(key, data);
        return count == null ? 0 : count;
    }

    /**
     * 根据 key 拿到List集合
     *
     * @param key 键
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> getList(final String key){
       return redisTemplate.opsForList().range(key,0,-1);
    }

    /**
     * 缓存Set集合
     *
     * @param key  关键
     * @param data 数据
     * @return {@link Long}
     */
    public <T> void setSet(final String key, final Set<T> data){
        if (data.isEmpty()){
            return;
        }
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T value : data) {
            setOperation.add(value);
        }
    }

    /**
     * 根据 key 拿到Set集合
     *
     * @param key 关键
     * @return {@link Set}<{@link T}>
     */
    public <T> Set<T> getSet(final String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @return boolean
     */
    public boolean expire(final String key, final Long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return boolean
     */
    public boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除某个对象
     *
     * @param key 键
     * @return boolean
     */
    public boolean deleteObject(final String key){
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个对象
     *
     * @param keys 键
     * @return boolean
     */
    public boolean deleteObjects(final Collection<String> keys){
        return redisTemplate.delete(keys) > 0;
    }

    public void incrByKey(final String key){
        redisTemplate.opsForValue().increment(key,1);
    }

}
