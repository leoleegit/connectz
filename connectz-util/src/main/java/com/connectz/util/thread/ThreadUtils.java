package com.connectz.util.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class ThreadUtils {
	private static Logger logger = Logger.getLogger(ThreadUtils.class);
	private static ExecutorService exec =  Executors.newCachedThreadPool();
	private static Map<Object,Timer> timers = new HashMap<Object,Timer>();
	
	public static void execute(Runnable thread){
		try{
			exec.execute(thread);
		}catch(Exception e){
			logger.error("", e);
		}
	}
	
	public static Timer schedule(String name, TimerTask task, long delay){
		Timer timer = new Timer(name==null?ThreadUtils.class.getName():name);
		timer.schedule(task, delay);
		return timer;
	}
	
	public static Timer schedule(String name, TimerTask task, long firstTime, long period){
		Timer timer = new Timer(name==null?ThreadUtils.class.getName():name);
		timer.schedule(task, firstTime, period);
		return timer;
	}
	
	public static void putTimer(Object key, Timer timer){
		if(timers.containsKey(key)){
			Timer t = timers.remove(key);
			try{
				t.cancel();
				t = null;
			}catch(Exception e){}	
		}
		timers.put(key, timer);		
	}
	
	public static Timer getTimer(Object key){
		if(timers.containsKey(key))
			return timers.get(key);
		return null;
	}
	
	public static Timer removeTimer(Object key){
		if(timers.containsKey(key))
			return timers.remove(key);
		return null;
	}
}
