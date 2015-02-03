package com.zms.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class Main extends Activity {

    static DbOperation db = null;
    static List<App> appInfo = null;
    PackageManager packageManager = null;
    int orderFlag = 0; // 排序方式标志,0-默认排序，1-名字排序，2-时间排序
    static boolean isEqual = false;

    List<ResolveInfo> apps = null;
    List<ResolveInfo> apps2 = null;
    private static final String TAG = "ScrollLayoutTest";
    private ScrollLayout mScrollLayout;
    private static final float APP_PAGE_SIZE = 36.0f;
    private Context mContext;
    int upCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.main);
        mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 1, "● 默认排序[当前]");
        menu.add(0, 2, 1, "○ 名称排序");
        menu.add(0, 3, 1, "○ 时间排序");
        menu.add(0, 4, 1, "   关于应用");
        menu.add(0, 5, 1, "   更新桌面");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: // 默认排序[当前]
                break;
            case 2: // 名称排序
                Intent intent1 = new Intent(this, DeskByName.class);
                startActivity(intent1);
                break;
            case 3: // 按安装时间排序
                Intent intent2 = new Intent(this, DeskByTime.class);
                startActivity(intent2);
                break;
            case 4:// 关于
                Intent intent3 = new Intent(this, About.class);
                startActivity(intent3);
                break;
            case 5:// 更新桌面
                onCreate(null);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void initViews() {
        packageManager = getPackageManager();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        // get all apps
        apps = packageManager.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo pi : apps) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            PackageManager manager = this.getPackageManager();
            map.put("appIcon", pi.loadIcon(packageManager));
            map.put("appName", pi.loadLabel(packageManager).toString());
            map.put("appPack", pi.activityInfo.packageName);
            try {
                map.put("appTime", Long.toString(manager.getPackageInfo(
                        pi.activityInfo.packageName, 0).firstInstallTime));
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            items.add(map);
        }

        // ToDB
        db = new DbOperation(this);
        long countlong = db.getCount(); // 获取数据库中记录数目
        int countint = (int) countlong;
        upCount = items.size();// +++++++++++++++++++++++++++++++++++++++upCount
        if (countint == items.size()) {
            isEqual = true;
            // 若记录数目与当前items.size()相等，则不插入应用信息，只插入时间
            for (int m = 0; m < items.size(); m++) {
                // 插入时间：
                String appPack2 = (String) items.get(m).get("appPack");
                try {
                    PackageManager manager = this.getPackageManager();
                    String appTime2 = Long.toString(manager.getPackageInfo(
                            appPack2, 0).firstInstallTime);
                    db.insertTime(appTime2, appPack2);
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            // 若不等，则先清空表数据，然后再插入应用信息，然后插入时间
            db.deleteTable();
            for (int i = 0; i < items.size(); i++) {
                String appName = (String) items.get(i).get("appName");
                String appPack = (String) items.get(i).get("appPack");
                String appTime = (String) items.get(i).get("Time");
                App appInfo = new App(appName, appPack, appTime);
                DbOperation db = new DbOperation(this);
                db.insertApp(appInfo);
                // 插入时间：
                String appPack2 = (String) items.get(i).get("appPack");
                try {
                    PackageManager manager = this.getPackageManager();
                    String appTime2 = Long.toString(manager.getPackageInfo(
                            appPack2, 0).firstInstallTime);
                    db.insertTime(appTime2, appPack2);
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        // 读取数据库信息
        db = new DbOperation(this);
        if (orderFlag == 0) {
            appInfo = db.query("appTable");
        } else if (orderFlag == 1) {
            appInfo = db.queryByName("appTable");
        } else if (orderFlag == 2) {
            appInfo = db.queryByTime("appTable");
        }
        final int PageCount = (int) Math.ceil(apps.size() / APP_PAGE_SIZE);
        Log.e(TAG, "size:" + apps.size() + " page:" + PageCount);
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(this);
            this.registerForContextMenu(appPage);
            appPage.setAdapter(new AppAdapter(this, appInfo, i, apps, items));
            appPage.setNumColumns(6);
            appPage.setOnItemClickListener(listener);
            mScrollLayout.addView(appPage);
        }
    }

    public OnItemClickListener listener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            GridView listView = (GridView) parent;
            App appInfo = (App) (listView.getItemAtPosition(position));
            String packName = appInfo.getPack();
            Intent mainIntent = mContext.getPackageManager()
                    .getLaunchIntentForPackage(packName);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(mainIntent);
            } catch (ActivityNotFoundException noFound) {
                Toast.makeText(mContext, "未发现该应用!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("操作");
        menu.add(0, 1, Menu.NONE, "更名");
        menu.add(0, 2, Menu.NONE, "卸载");
    }

    public boolean onContextItemSelected(MenuItem item) {
        // 得到当前被选中的item信息
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
                .getMenuInfo();
        final int a = menuInfo.position;
        switch (item.getItemId()) {
            case 1:
                final EditText e = new EditText(this);
                new AlertDialog.Builder(this).setTitle("更名")
                        // .setIcon(R.drawable.logo2)
                        .setView(e)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int i) {

                                        int b1 = mScrollLayout.getCurScreen();
                                        int num1 = b1 * 36 + a;
                                        String appName = appInfo.get(num1)
                                                .getName();
                                        System.out.println(appName);
                                        String str = e.getText().toString();
                                        System.out.println(str);
                                        db.changeName(str, appName);
                                        // 更新界面
                                        onCreate(null);
                                    }
                                }).setNegativeButton("取消", null).show();
                break;
            case 2:
                int b1 = mScrollLayout.getCurScreen();
                int num1 = b1 * 36 + a;
                String appPack = appInfo.get(num1).getPack();
                Uri uri = Uri.parse("package:" + appPack);
                System.out.println(appPack);
                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                intent.setData(uri);// 设置获取到的URI
                db.deleteApp(appPack);
                startActivity(intent);
                // break;
            default:
                // 更新+延时
                try {
                    Thread.currentThread().sleep(300);
                    onCreate(null);
                } catch (InterruptedException e22) {
                    e22.printStackTrace();
                }
                onCreate(null);
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class AppIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onCreate(null);
        }
    }

    private void registerIntentReceivers() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(new AppIntentReceiver(), filter);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
        // this.finish();
    }
}
