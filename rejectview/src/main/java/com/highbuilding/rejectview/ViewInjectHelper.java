package com.highbuilding.rejectview;

import android.app.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * created by xiangrong@baidu.com
 **/
public class ViewInjectHelper {

    /**
     * 注入主布局文件
     *
     * @param activity
     */
    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (null != contentView)// 存在
        {
            int contentViewLayoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView",
                        int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入所有的控件
     *
     * @param activity
     */
    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有成员变量
        for (Field field : fields) {
            ViewInject viewInjectAnnotation = field
                    .getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1) {
                    // 初始化View
                    try {
                        Method method = clazz.getMethod("findViewById",
                                int.class);
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        field.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }


    public static void init(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
    }

}
