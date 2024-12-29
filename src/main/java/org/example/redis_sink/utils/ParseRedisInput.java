package org.example.redis_sink.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class ParseRedisInput implements Serializable {
    public static Map<String, String> parse(Object obj) {
        if (obj instanceof Map<?, ?>) {
            return (Map<String, String>) obj;
        }

        if (obj == null) {
            throw new IllegalArgumentException("The input element is null.");
        }

        Map<String, String> map = new HashMap<>();
        Class<?> objClass = obj.getClass();

        while (objClass != null) {
            Field[] fields = objClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String fieldValue = "error";
                try {
                    fieldValue = field.get(obj).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                map.put(fieldName, fieldValue);
            }

            objClass = objClass.getSuperclass();
        }

        return map;
    }
}
