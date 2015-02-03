package com.zms.launcher;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class App {
	private int id;
	private String appName;
	private String appPack;
	private String appTime;

	public App() {
		super();
	}

	public App(String appName) {
		super();
		this.appName = appName;
	}

	

	public App(String appName, String appPack) {
		super();
		this.appName = appName;
		this.appPack = appPack;
	}



	public App(String appName, String appPack, String appTime) {
		super();
		this.appName = appName;
		this.appPack = appPack;
		this.appTime = appTime;
	}

	public App(int id, String appName, String appTime) {
		super();
		this.id = id;
		this.appName = appName;
		this.appTime = appTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return appName;
	}

	public void setName(String appName) {
		this.appName = appName;
	}

	// public String getTime() {
	// return appTime;
	// }
	// public void setTime(String appTime) {
	// this.appTime = appTime;
	// }
	// public Bitmap getIcon() {
	// return appIcon;
	// }
	// public void setIcon(Bitmap appIcon) {
	// this.appIcon = appIcon;
	// }
	public String getPack() {
		return appPack;
	}

	public void setPack(String appPack) {
		this.appPack = appPack;
	}

}
