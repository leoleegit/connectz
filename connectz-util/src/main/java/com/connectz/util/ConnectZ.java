package com.connectz.util;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.connectz.util.json.LifecycleJson;
import com.connectz.util.json.SpotlightXConfig;
import com.connectz.util.thread.ThreadUtils;

public class ConnectZ implements ServletContextListener, Lifecycle{
	private static List<Lifecycle> Lifecycles  = new ArrayList<Lifecycle>();
	private static Map<Lifecycle,LifecycleJson> LifecyclesInfo  = new HashMap<Lifecycle,LifecycleJson>();
	private static InputStream init_file_is = ConnectZ.class.getResourceAsStream("/spotlightx.json");
	
	private static String HOME = System.getProperty("user.dir");
	private static String log_file = "log4j.xml";
	private static ClassLoader class_loader;
	private SpotlightXConfig sxconfig;
	private static ConnectZ instance;
	private static Logger logger;
	private boolean initialized;
	private String status;

	
	public ConnectZ(){
		instance = this;
		class_loader = new SystemClassLoader();
		sxconfig = new SpotlightXConfig(init_file_is);
		initLogConfig();
	}
	
	public static ConnectZ instance(){
		return instance;
	}
	
	private void initLogConfig(){
		File file = new File(HOME+File.separator+log_file);
		if(file.exists()){
			DOMConfigurator.configure(file.getAbsolutePath());
		}
		logger = Logger.getLogger(getClass());
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		logger.info("SpotlightX Destroyed...");
		destory();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		logger.info("SpotlightX Start...");
		String active = event!=null?event.getServletContext().getInitParameter("spotlightx-active"):"true";  
		if(active!=null){
			setStatus(Boolean.parseBoolean(active)?"ACTIVE":"STOP");
		}else{
			active = System.getProperty("spotlightx-startup-mode", "ACTIVE");  
			setStatus(active.toUpperCase().equals("ACTIVE")?"ACTIVE":"STOP");
		}
		execute(new Runnable(){
			public void run(){	
				init();
			}
		});
	}

	public boolean isInitialized() {
		return initialized;
	}
	
	public void addLifecycle(Lifecycle lifecycle){
		addLifecycle(lifecycle,null,-1);
	}
	
	public void addLifecycle(Lifecycle lifecycle,String name,int index){
		if(lifecycle!=null){
			addLifecycle(new LifecycleJson(name!=null?name:lifecycle.getClass().getName(),lifecycle.getClass().getName(),index).setActive(true),lifecycle);
		}
	}
	
	private void addLifecycle(LifecycleJson info,Lifecycle lifecycle){
		int index = info.getIndex();
		if(index>=0){
			if(index>Lifecycles.size())
				index = Lifecycles.size();
			Lifecycles.add(index, lifecycle);
		}else{
			Lifecycles.add(lifecycle);
		}
		LifecyclesInfo.put(lifecycle, info);
		startLifecycle(info,lifecycle);
	}
	
	private void startLifecycle(LifecycleJson info,final Lifecycle lifecycle){
		if(initialized && info.isActive() && isActive()){
			info.setStatus("ACTIVE");
			if(lifecycle.syncStart()){
				try{
					lifecycle.init();
				}catch(Exception e){
					logger.error("", e);
				}
			}else{
				execute(new Runnable(){
					public void run(){
						try{
							lifecycle.init();
						}catch(Exception e){
							logger.error("", e);
						}
					}
				});
			}
		}else{
			info.setStatus("STOP");
		}
	}
	
	private void stopLifecycle(final LifecycleJson info, final Lifecycle lifecycle){
		execute(new Runnable(){
			public void run(){
				try{
					lifecycle.destory();
					info.setStatus("STOP");
				}catch(Exception e){
					logger.error("", e);
				}
			}
		});
	}
	
	public Lifecycle stopLifecycle(String name){
		Lifecycle lc = findLifecycle(name);
		if(lc!=null){
			LifecycleJson lifejson = LifecyclesInfo.get(lc);
			stopLifecycle(lifejson,lc);
		}
		return lc;
	}
	
	public Lifecycle startLifecycle(String name){
		Lifecycle lc = findLifecycle(name);
		if(lc!=null){
			LifecycleJson lifejson = LifecyclesInfo.get(lc);
			lifejson.setActive(true);
			startLifecycle(lifejson,lc);
		}
		return lc;
	}

	public Lifecycle findLifecycle(String name){
		if(name==null)return null;
		for(Lifecycle lifecycle : LifecyclesInfo.keySet()){
			LifecycleJson lifejson = LifecyclesInfo.get(lifecycle);
			if(name.equals(lifejson.getName()))
				return lifecycle;
		}
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		List<LifecycleJson> lj = sxconfig.getLifecycles();
		for(int i=0;lj!=null && i<lj.size();i++){
			LifecycleJson lifejson = lj.get(i);
			logger.info(lifejson.toJson());
			String clazz = lifejson.getClazz();
			Object object = ReflectUtils.newInstance(clazz, class_loader);
			Lifecycle lifecycle = object!=null?(Lifecycle)object:null;
			addLifecycle(lifejson, lifecycle);
		}
		initialized = true;
		for(Lifecycle lifecycle : Lifecycles){
			LifecycleJson lifejson = LifecyclesInfo.get(lifecycle);
			startLifecycle(lifejson,lifecycle);
		}
		logger.info("initialized ...");
	}
	
	public static Class<?> loadClass(String clazz){
		return ReflectUtils.loadClass(clazz, class_loader);
	}
	
	public static void execute(Runnable thread){
		ThreadUtils.execute(thread);
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		initialized = false;
		for(Lifecycle lifecycle : Lifecycles){
			LifecycleJson lifejson = LifecyclesInfo.get(lifecycle);
			stopLifecycle(lifejson,lifecycle);
		}
	}

	@Override
	public boolean syncStart() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isActive(){
		return "ACTIVE".equalsIgnoreCase(status);
	}

	public SpotlightXConfig getSxconfig() {
		return sxconfig;
	}
}
