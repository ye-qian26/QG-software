package com.qg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.json.JsonParseException;

public class JsonParserUtil {

    // 线程安全的Gson实例
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    // 私有构造函数，防止实例化
    private JsonParserUtil() {}

    /**
     * 将对象转换为JSON字符串
     * @param obj 要转换的对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * 将JSON字符串解析为指定类型的对象
     * @param json JSON字符串
     * @param clazz 目标对象类型
     * @param <T> 泛型类型
     * @return 解析后的对象
     * @throws JsonParseException 如果JSON格式错误
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonParseException {
        return GSON.fromJson(json, clazz);
    }
}
