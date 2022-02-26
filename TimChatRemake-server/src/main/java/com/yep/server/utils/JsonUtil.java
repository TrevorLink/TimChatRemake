package com.yep.server.utils;

import com.alibaba.fastjson.JSON;

/**
 * JSON工具类
 * @author 28459
 */
public class JsonUtil extends JSON {


    public static String parseToString(Object object) {

        return toJSONString(object);

    }

    public static <T> T parseToObject(String text, Class<T> clazz) {
        return parseObject(text, clazz);
    }

}