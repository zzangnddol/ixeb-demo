package com.inzent.ixeb.service.util;

public class TimeWatcher {
	private long lastTime;

	public TimeWatcher() {
		lastTime = System.currentTimeMillis();
	}
	
	public void reset() {
		lastTime = System.currentTimeMillis();
	}
	
	public long elapsedTime() {
		return  System.currentTimeMillis() - lastTime;
	}
}
