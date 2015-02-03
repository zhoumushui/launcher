package com.zms.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接收广播：系统启动完成后运行程序
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent newIntent = new Intent(context, Main.class);
            newIntent.setAction("android.intent.action.MAIN");
            newIntent.addCategory("android.intent.category.LAUNCHER");
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
        // 接收广播：设备上新安装了一个应用程序包后
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString().substring(8);
            // String appName = intent.getDataString().;
//			PackageManager pm = context.getPackageManager();
//			String name = null;
//			try {
//				name = pm.getApplicationLabel(
//						pm.getApplicationInfo(packageName,
//								PackageManager.GET_META_DATA)).toString();
//			} catch (NameNotFoundException e) {
//				e.printStackTrace();
//			}
//			System.out.println("---------------" + packageName);
//			DbOperation db = new DbOperation();
//			App appInfo = new App(name, packageName);
//			// DbOperation db = new DbOperation(this);
//			db.insertApp(appInfo);


            // Intent newIntent = new Intent();
            // newIntent.setClassName("com.tchip.desk","com.tchip.desk"+".Desk");
            //
            // newIntent.setAction("android.intent.action.MAIN");
            // newIntent.addCategory("android.intent.category.LAUNCHER");
            // newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // context.startActivity(newIntent);
        }
        // 接收广播：设备上删除了一个应用程序包。
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            System.out.println("********************************");
            // DatabaseHelper db = new DatabaseHelper();
            // DbOperation db = new DbOperation();
            // db.executeSql("delete from users");
        }
    }
}
