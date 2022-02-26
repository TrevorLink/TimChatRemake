package com.yep.mail.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtil extends JSON {


    public static String parseToString(Object object) {

        return toJSONString(object);

    }

    public static <T> T parseToObject(String text, Class<T> clazz) {
        return parseObject(text, clazz);
    }

}