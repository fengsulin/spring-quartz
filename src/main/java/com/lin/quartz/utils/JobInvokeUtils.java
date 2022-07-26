package com.lin.quartz.utils;

import com.lin.quartz.entity.QuartzJob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 调度任务工具类
 */
public class JobInvokeUtils {
    /**
     * 执行任务方法
     * @param job
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    public static void invokeMethod(QuartzJob job) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String invokeTarget = job.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);
        if(! isValidClassName(beanName)){
            Object bean = SpringUtils.getBean(beanName);
            invokeMethod(bean,methodName,methodParams);
        }else {
            Object bean = Class.forName(beanName).newInstance();
            invokeMethod(bean,methodName,methodParams);
        }
    }

    /**
     * 通过反射调用任务方法
     * @param bean
     * @param methodName
     * @param methodParams
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void invokeMethod(Object bean,String methodName,List<Object[]> methodParams) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(StringUtils.isNotNull(methodParams) && methodParams.size() >0){
            Method method = bean.getClass().getDeclaredMethod(methodName,getMethodParamsType(methodParams));
            method.setAccessible(true);
            method.invoke(bean,getMethodParamsValue(methodParams));
        }else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(bean);
        }
    }

    /**
     * 判断调用目标字符串是否合法（比如：类对象名称.方法名称(参数)）
     * @param invokeTarget
     * @return
     */
    public static boolean isValidClassName(String invokeTarget){
        return StringUtils.countMatches(invokeTarget,".") > 1;
    }
    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName,".");
    }

    /**
     * 处理目标字符串，获取方法名称
     * @param invokeTarget
     * @return
     */
    public static String getMethodName(String invokeTarget){
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName,".");
    }

    /**
     * 处理目标字符串，获取参数
     * @param invokeTarget
     * @return
     */
    public static List<Object[]> getMethodParams(String invokeTarget){
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if(StringUtils.isEmpty(methodStr)){
            return null;
        }
        String[] methodParams = methodStr.split(",");
        LinkedList<Object[]> classes = new LinkedList<>();
        for(int i= 0; i<methodParams.length;i++){
            String str = StringUtils.trimToEmpty(methodParams[i]);
            if(StringUtils.contains(str,"'")){
                classes.add(new Object[]{StringUtils.replace(str,"'",""),String.class});
            }
            else if(StringUtils.equals(str,"true") || StringUtils.equals(str,"false")){
                classes.add(new Object[]{Boolean.valueOf(str),Boolean.class});
            }
            // long长整形，包含L
            else if (StringUtils.containsIgnoreCase(str, "L")) {
                classes.add(new Object[]{Long.valueOf(StringUtils.replaceIgnoreCase(str, "L", "")), Long.class});
            }
            // double浮点类型，包含D
            else if (StringUtils.containsIgnoreCase(str, "D")) {
                classes.add(new Object[]{Double.valueOf(StringUtils.replaceIgnoreCase(str, "D", "")), Double.class});
            }
            // 其他类型归类为整形
            else {
                classes.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classes;
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classes = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classes[index] = (Class<?>) os[1];
            index++;
        }
        return classes;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classes = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classes[index] = (Object) os[0];
            index++;
        }
        return classes;
    }

}
