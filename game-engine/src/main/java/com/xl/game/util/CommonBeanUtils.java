package com.xl.game.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuliang
 * @date 2018-08-13
 */
public class CommonBeanUtils {

    private static final String sdf10 = "yyyy-MM-dd";
    private static final String sdf19 = "yyyy-MM-dd HH:mm:ss";

    public static List<String> list = new ArrayList<String>();

    /**
     * 复制对象属性值，把source的值复制到target对象中
     * 该方法目前仅支持(String,int,byte,Integer,BigDecimal,Date,boolean,Object implement Serializable And List 等类型)
     */
    public static <T> void copyPropertiesElseList(T source, T target, List<String> listVals) {
        if (isEmpty(source) || isEmpty(target)) {
            return;
        }
        listVals.add("serialVersionUID");
        listVals.add("create_by");
        listVals.add("create_date");
        // 得到类对象
        Class<? extends Object> sourceClass = source.getClass();
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            String sourceFieldName = sourceField.getName();
            if (listVals.contains(sourceFieldName)) {
                continue;
            }
            setFiledFromSourceToTarget(sourceField, source, target);
        }
    }

    /**
     * 复制对象属性值，把source的值复制到target对象中
     * 该方法目前仅支持(String,int,byte,Integer,BigDecimal,Date,boolean,Object implement Serializable And List 等类型)
     */
    public static <T> void copyProperties(T source, T target) {
        if (isEmpty(source) || isEmpty(target)) {
            return;
        }
        // 得到类对象
        Class<? extends Object> sourceClass = source.getClass();
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            String sourceFieldName = sourceField.getName();
            if (toUpperCaseEquals(sourceFieldName, "SERIALVERSIONUID")) {
                continue;
            }
            setFiledFromSourceToTarget(sourceField, source, target);
        }
    }

    /**
     * 将field对象属性值从source赋值到target中
     */
    public static final <T> void setFiledFromSourceToTarget(Field field, T source, T target) {
        String sourceFieldName = field.getName();
        Object sourceFieldVal = getFiledVal(field, source);
        if (isEmpty(sourceFieldVal)) {
            return;
        }
        Class<? extends Object> targetClass = target.getClass();
        Field[] targetFields = targetClass.getDeclaredFields();
        for (int k = 0; k < targetFields.length; k++) {
            Field targetField = targetFields[k];
            String targetFieldName = targetField.getName();
            if (toUpperCaseEquals(sourceFieldName, targetFieldName)) {
                String sourceType = field.getType().toString();// 得到此属性的类型
                String targetType = targetField.getType().toString();// 得到此属性的类型
                Class<?> targetFieldValClass = targetField.getDeclaringClass();
                Field[] targetFieldValClassFields = targetFieldValClass.getDeclaredFields();
                sourceFieldVal = toValBySourceType(sourceFieldName, sourceFieldVal, targetType, sourceType, targetFieldValClassFields);
                boolean setResult = setFiledVal(targetField, target, sourceFieldVal);
                if (setResult) {
                    break;
                }
            }
        }
    }

    /**
     * toValBySourceType  把object从sourceType类型变成targetType，取得object值
     */
    private static final Object toValBySourceType(String objectName, Object object, String targetType, String sourceType, Field[] fieldValClassFields) {
        Object objectRes = null;
        if (targetType.toUpperCase().endsWith("INT") || targetType.toUpperCase().endsWith("INTEGER")) {
            if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = Integer.parseInt(object.toString());
            } else if (sourceType.toUpperCase().endsWith("INT") || sourceType.toUpperCase().endsWith("INTEGER")) {
                objectRes = object;//数字转化数字，不需要考虑转化
            } else if (sourceType.toUpperCase().endsWith("BIGDECIMAL")) {
                objectRes = Integer.parseInt(String.valueOf(object));//原类型是BIGDECIMAL，要转化成数字格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = null;//原类型是DATE，要转化成数字格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("BOOLEAN")) {
                if ("true".equals(object.toString().trim())) {
                    objectRes = 1;
                } else if ("false".equals(object.toString().trim())) {
                    objectRes = 0;
                } else {
                    objectRes = null;//原类型是BIGDECIMAL，要转化成INTEGER，不考虑这种情况（待定）
                }
            } else if (sourceType.toUpperCase().endsWith("BYTE")) {
                objectRes = Integer.parseInt((object.toString()));
            }
            else if (sourceType.toUpperCase().endsWith("LONG")) {
                objectRes = Integer.valueOf((object.toString()));
            }
        } else if (targetType.toUpperCase().endsWith("BIGDECIMAL")) {
            if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = new BigDecimal(object.toString());
            } else if (sourceType.toUpperCase().endsWith("INT") || sourceType.toUpperCase().endsWith("INTEGER")) {
                objectRes = new BigDecimal(String.valueOf(object));
            } else if (sourceType.toUpperCase().endsWith("BIGDECIMAL")) {
                objectRes = object;//BIGDECIMAL转化BIGDECIMAL，不需要考虑转化
            } else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = null;//原类型是DATE，要转化成BIGDECIMAL格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("BOOLEAN")) {
                if ("true".equals(object.toString().trim())) {
                    objectRes = new BigDecimal("1");
                } else if ("false".equals(object.toString().trim())) {
                    objectRes = new BigDecimal("0");
                } else {
                    objectRes = null;//原类型是BOOLEAN，要转化成BIGDECIMAL，不考虑这种情况（待定）
                }
            } else if (sourceType.toUpperCase().endsWith("BYTE")) {
                objectRes = new BigDecimal(object.toString());
            }
        } else if (targetType.toUpperCase().endsWith("DATE")) {
            if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = objectToDate(object);
            } else if (sourceType.toUpperCase().endsWith("INT") || sourceType.toUpperCase().endsWith("INTEGER")) {
                objectRes = null;//原类型是数字，要转化成日期格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("BIGDECIMAL")) {
                objectRes = null;//原类型是BIGDECIMAL，要转化成日期格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = object;//日期转化成日期格式，不需要考虑转化
            } else if (sourceType.toUpperCase().endsWith("BOOLEAN")) {
                objectRes = null;//原类型是BIGDECIMAL，要转化成日期格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("BYTE")) {
                objectRes = null;
            } else if (sourceType.toUpperCase().endsWith("LONG")) {
                objectRes = objectToLongDate(object);
            }
        } else if (targetType.toUpperCase().endsWith("BOOLEAN")) {
            if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = parseBoolean(object.toString());
            } else if (sourceType.toUpperCase().endsWith("INT") || sourceType.toUpperCase().endsWith("INTEGER")
                    || sourceType.toUpperCase().endsWith("BIGDECIMAL") || sourceType.toUpperCase().endsWith("BYTE")) {
                if ("1".equals(object.toString().trim())) {
                    objectRes = true;
                } else if ("0".equals(object.toString().trim())) {
                    objectRes = false;
                } else {
                    objectRes = null;//原类型是数字，要转化成boolean格式，不考虑这种情况（待定）
                }
            } else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = null;//原类型是DATE，要转化成boolean格式，不考虑这种情况（待定）
            } else if (sourceType.toUpperCase().endsWith("BOOLEAN")) {
                objectRes = object;//BOOLEAN转BOOLEAN，不需要考虑转化
            }
        } else if (targetType.toUpperCase().endsWith("STRING")) {
            if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = object;//字符串转字符串，不需要考虑转化
            } else if (sourceType.toUpperCase().endsWith("INT") || sourceType.toUpperCase().endsWith("INTEGER")) {
                objectRes = String.valueOf(object);
            } else if (sourceType.toUpperCase().endsWith("BIGDECIMAL")) {
                objectRes = String.valueOf(object);
            } else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = dateToObject(object);
            } else if (sourceType.toUpperCase().endsWith("BOOLEAN")) {
                objectRes = String.valueOf(object);
            } else if (sourceType.toUpperCase().endsWith("BYTE")) {
                objectRes = String.valueOf(object);
            } else if (sourceType.toUpperCase().endsWith("LONG")) {
                objectRes = String.valueOf(object);
            }

        } else if (targetType.toUpperCase().endsWith("LONG")) {
            if (sourceType.toUpperCase().endsWith("LONG")) {
                objectRes = object;//字符串转字符串，不需要考虑转化
            } else if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = Long.valueOf(String.valueOf(object));
            }else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = objectToLongDate(object);//日期转化成日期格式，不需要考虑转化
            }else if (sourceType.toUpperCase().endsWith("INTEGER")) {
                objectRes = Long.valueOf(object.toString());//日期转化成日期格式，不需要考虑转化
            }
        } else if (targetType.toUpperCase().endsWith("BYTE")) {
            if (sourceType.toUpperCase().endsWith("STRING")) {
                objectRes = String.valueOf(object);
            } else if (sourceType.toUpperCase().endsWith("INT") || sourceType.toUpperCase().endsWith("INTEGER")) {
                objectRes = Integer.parseInt(String.valueOf(object));
            } else if (sourceType.toUpperCase().endsWith("BIGDECIMAL")) {
                objectRes = new BigDecimal(String.valueOf(object));
            } else if (sourceType.toUpperCase().endsWith("DATE")) {
                objectRes = null;
            } else if (sourceType.toUpperCase().endsWith("BOOLEAN")) {
                if ("1".equals(object.toString().trim())) {
                    objectRes = true;
                } else if ("0".equals(object.toString().trim())) {
                    objectRes = false;
                } else {
                    objectRes = null;//原类型是BYTE，要转化成boolean格式，不考虑这种情况（待定）
                }
            } else if (sourceType.toUpperCase().endsWith("BYTE")) {
                objectRes = object;
            }

        } else if (targetType.toUpperCase().endsWith("LIST") && sourceType.toUpperCase().endsWith("LIST")) {
            for (Field targetClaField : fieldValClassFields) {
                String targetClaFieldName = targetClaField.getName();
                if (objectName.equals(targetClaFieldName)) {
                    List<Object> listObj = new ArrayList<Object>();
                    Class<?> sonClass = getFieldClass(targetClaField);
                    Object son = null;
                    try {
                        son = sonClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ArrayList<?> listSource = (ArrayList<?>) object;
                    for (Object obj : listSource) {
                        copyProperties(obj, son);
                        listObj.add(son);
                    }
                    objectRes = listObj;
                    break;
                }
            }
        } else {
            for (Field targetClaField : fieldValClassFields) {
                String targetClaFieldName = targetClaField.getName();
                if (objectName.equals(targetClaFieldName)) {
                    String sonClassStr = targetClaField.getType().getCanonicalName();
                    Class<?> sonClass = null;
                    try {
                        sonClass = Class.forName(sonClassStr);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Object son = null;
                    try {
                        if (sonClass != null) {
                            son = sonClass.newInstance();
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    copyProperties(object, son);
                    objectRes = son;
                    break;
                }
            }
        }

        return objectRes;
    }

    /**
     * 获取field的类型，如果是复合对象，获取的是泛型的类型
     */
    private static Class getFieldClass(Field field) {
        Class fieldClazz = field.getType();
        if (fieldClazz.isAssignableFrom(List.class)) {
            Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
            if (fc instanceof ParameterizedType) { // 如果是泛型参数的类型
                ParameterizedType pt = (ParameterizedType) fc;
                fieldClazz = (Class) pt.getActualTypeArguments()[0]; //得到泛型里的class类型对象。
            }
        }
        return fieldClazz;
    }

    /**
     * isEmpty(判断是否为空,如果为空返回true,否则false)
     */
    private static final boolean isEmpty(Object str) {
        if (null == str) {
            return true;
        }
        if ("".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * isNotEmpty(判断是否为空,如果为空返回false,否则true)
     */
    private static final boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * isEmpty(判断是否为空,如果为空返回true,否则false)
     */
    private static final boolean isEmpty(String str) {
        if (null == str) {
            return true;
        }
        if ("".equals(str)) {
            return true;
        }
        if ("".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * isNotEmpty(判断是否为空,如果为空返回false,否则true)
     */
    private static final boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * isEmpty(判断是否为空,如果为空返回true,否则false)
     */
    private static final boolean isEmpty(Integer index) {
        if (null == index) {
            return true;
        }
        return false;
    }

    /**
     * isNotEmpty(判断是否为空,如果为空返回false,否则true)
     */
    private static final boolean isNotEmpty(Integer str) {
        return !isEmpty(str);
    }

    /**
     * 将date格式的Object转成object格式
     */
    private static final String dateToObject(Object date) {
        String str = null;
        if (isEmpty(date)) {
            return null;
        }
        try {
            str = new SimpleDateFormat(sdf19).format(date);
        } catch (Exception e) {
            try {
                str = new SimpleDateFormat(sdf10).format(date);
            } catch (Exception e1) {
            }
        }
        return str;
    }

    /**
     * 将object格式的Object转成date格式
     */
    private static final Date objectToDate(Object object) {
        Date date = null;
        if (isEmpty(object)) {
            return date;
        }
        try {
            date = new SimpleDateFormat(sdf19).parse(object.toString());
        } catch (Exception e) {
            try {
                date = new SimpleDateFormat(sdf10).parse(object.toString());
            } catch (Exception e1) {
            }
        }
        return date;
    }


    /**
     * 将object格式的Object转成date格式
     */
    private static final Long objectToLongDate(Object object) {
        Date date = null;
        if (isEmpty(object)) {
            return null;
        }
        try {
            date = (Date)object;
//            date = new SimpleDateFormat(sdf19).parse(object.toString());
        } catch (Exception e) {
            try {
                date = new SimpleDateFormat(sdf10).parse(object.toString());
            } catch (Exception e1) {
            }
        }
        return date.getTime();
    }

    /**
     * toUpperCaseEquals(strA toUpperCase compare strB toUpperCase)
     */
    private static final boolean toUpperCaseEquals(String strA, String strB) {
        if (null == strA && null == strB) {
            return true;
        }
        if (null == strA) {
            return false;
        }
        if (null == strB) {
            return false;
        }
        if ("".equals(strA.trim()) && "".equals(strB.trim())) {
            return true;
        }
        if ("".equals(strA.trim())) {
            return false;
        }
        if ("".equals(strB.trim())) {
            return false;
        }
        if (strA.trim().toUpperCase().equals(strB.trim().toUpperCase())) {
            return true;
        }
        return false;
    }

    /**
     * getFiledVal 通过反射机制获取field对象的属性值
     */
    private static final <T> Object getFiledVal(Field field, T t) {
        field.setAccessible(true); // 设置些属性是可以访问的
        Object fieldVal = null;
        try {
            fieldVal = field.get(t);
        } catch (Exception e) {
        }
        return fieldVal;
    }

    /**
     * getFiledVal 通过反射机制给field对象的属性赋值val
     */
    private static final <T> boolean setFiledVal(Field field, T t, Object val) {
        field.setAccessible(true); // 设置些属性是可以访问的
        try {
            if (null != val) {
                field.set(t, val);
                return true;
            }
        } catch (Exception e) {
        }
        return false;


    }

    /**
     * 将字符串的"true","false"，"0","1" 转换成Boolean类型的true,false
     */
    private static final boolean parseBoolean(Object str) {
        if ("true".equals(str.toString()) || "false".equals(str.toString())) {
            return Boolean.parseBoolean(str.toString());
        }
        if ("0".equals(str.toString())) {
            return false;
        }
        if ("1".equals(str.toString())) {
            return true;
        }
        return false;
    }

}
