package com.zms.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter {
	private List<App> mList;
	private Context mContext;
	public static final int APP_PAGE_SIZE = 36;
	private List<HashMap<String, Object>> items;

	public AppAdapter(Context context, List<App> list, int page,
			List<ResolveInfo> apps, List<HashMap<String, Object>> items) {
		mContext = context;
		this.items = items;
		mList = new ArrayList<App>();
		int i = page * APP_PAGE_SIZE;
		int iEnd = i + APP_PAGE_SIZE;
		while ((i < apps.size()) && (i < iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public App getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		App appInfo = mList.get(position);
		AppItem appItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.app, null);

			appItem = new AppItem();
			appItem.mAppIcon = (ImageView) v.findViewById(R.id.ivAppIcon);
			appItem.mAppName = (TextView) v.findViewById(R.id.tvAppName);

			v.setTag(appItem);
			convertView = v;
		} else {
			appItem = (AppItem) convertView.getTag();
		}
		// set the icon
		String appPack = appInfo.getPack();



		
		for (int i = 0; i < items.size(); i++) {

			if (items.get(i).get("appPack").equals(appPack)) {
				appItem.mAppIcon.setImageDrawable((Drawable) items.get(i).get(
						"appIcon"));
			}
		}
		appItem.mAppName.setText(appInfo.getName());
		return convertView;
	}

	class AppItem {
		ImageView mAppIcon;
		TextView mAppName;
	}
}