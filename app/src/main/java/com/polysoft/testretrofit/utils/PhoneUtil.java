package com.polysoft.testretrofit.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sangyr
 * on 2016/10/19--->19:25
 */

public class PhoneUtil {

    /**
     * 判断是不是小米
     *
     * @return
     */
    public static boolean isMIUI() {
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是不是魅族
     *
     * @return
     */
    public static boolean isFlyme() {
        if (Build.MANUFACTURER.equals("Meizu")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 获取当前应用程序的版本号
     *
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            // 获取版本号名称
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            // 包名找不到的异常
            e.printStackTrace();
        }
        return null;
    }

}
