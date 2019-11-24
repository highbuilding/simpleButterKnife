package com.xr.viewbind;

import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InjectView {
    public static void bind(AppCompatActivity mActivity) {
        String clsName = mActivity.getClass().getName();
        try {
            Class<?> viewBindingClass = Class.forName(clsName + "_bindView");
            /*以下调用带参的、私有构造函数*/
            Constructor declaredConstructor = viewBindingClass.getDeclaredConstructor(new Class[]{mActivity.getClass()});
            declaredConstructor.setAccessible(true);
            declaredConstructor.newInstance(new Object[]{mActivity});

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}
