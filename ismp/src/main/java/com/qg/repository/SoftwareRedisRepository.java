package com.qg.repository;

import com.qg.domain.Software;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class SoftwareRedisRepository {
    /**
     * 私有变量
     */
    private final StringRedisTemplate stringRedisTemplate;
    private static final String CACHE_PREFIX = "software:";
    private static final String TYPE_PREFIX = "type:";

    /**
     * 注入
     *
     * @param redisTemplate
     */
    public SoftwareRedisRepository(StringRedisTemplate redisTemplate) {
        this.stringRedisTemplate = redisTemplate;
    }


    /**
     * 缓存已发行的轮播图数据（自动过滤未发行的）
     *
     * @param softwareList
     */
    public void cachePublishedPicture(List<Software> softwareList) {
        String key = CACHE_PREFIX + "picture";
        String json = JSONUtil.toJsonStr(softwareList);
        // 缓存五分钟内有效
        stringRedisTemplate.opsForValue().set(key, json, 5, TimeUnit.MINUTES);
    }

    /**
     * 获取已缓存的轮播图数据（确保只有已发行的）
     */
    public List<Software> getPublishedPicture() {
        String key = CACHE_PREFIX + "picture";
        String json = stringRedisTemplate.opsForValue().get(key);
        return JSONUtil.toList(json, Software.class);
    }


    /**
     * 缓存分类最新10个软件
     *
     * @param type
     * @param softwareList
     */
    public void cacheTypeNew(String type, List<Software> softwareList) {
        String key = CACHE_PREFIX + TYPE_PREFIX + type;
        String json = JSONUtil.toJsonStr(softwareList);
        // 缓存五分钟内有效
        stringRedisTemplate.opsForValue().set(key, json, 5, TimeUnit.MINUTES);
    }

    /**
     * 获取分类最新10个软件缓存
     *
     * @param type
     * @return
     */
    public List<Software> getCachedTypeNew(String type) {
        String key = CACHE_PREFIX + TYPE_PREFIX + type;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSONUtil.toList(json, Software.class);
    }
}