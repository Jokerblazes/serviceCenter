package com.joker.utils;

import com.joker.entity.NonEmpty;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 检查对象的工具类
 * Created by joker on 2017/12/7.
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public class CheckUntils {
    private CheckUntils() {}

    /**
     * 检查是否未空
     * @param object
     * @return
     */
    public static boolean checkNull(Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:
             fields) {
            NonEmpty nonEmpty = f.getAnnotation(NonEmpty.class);
            if (nonEmpty == null)
                continue;
            String name = name = f.getName();
            Method method = null;
            String methodName = "get" + StringUtils.captureName(name);
            try {
                method = clazz.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(name+"字段需要有get方法");
            }
            try {
                Object value = method.invoke(object);
                if (value == null)
                    return false;
                if (!isJavaBasicType(value)) {
                    checkNull(value);
                } else {
                    if (value.equals(0) || value.equals(""))
                        return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 检查类型
     * @param object
     * @return
     */
    public static boolean isJavaBasicType(Object object) {
        if (object instanceof Number)
            return true;
        else if (object instanceof Character)
            return true;
        else if (object instanceof String)
            return true;
        return false;
    }

}
