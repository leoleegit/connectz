package com.connectz.util.json;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class SpotlightXConfig extends FileConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<LifecycleJson> lifecycles;
	private List<String> applications; //class1,class2
	private List<String> systemproperty; //property1:value1,property2:value2
	
	public SpotlightXConfig(String json) {
		super(json);
		// TODO Auto-generated constructor stub
		logger.info(toJson());
	}

	
	public SpotlightXConfig(InputStream is) {
		super(is);
		logger.info(toJson());
	}

	public List<LifecycleJson> getLifecycles() {
		return lifecycles;
	}

	public void setLifecycles(List<LifecycleJson> lifecycles) {
		this.lifecycles = lifecycles;
	}
	
	@Override
	public void setListObject(String name, Object args) {
		// TODO Auto-generated method stub
		if("lifecycles".equals(name)){
			lifecycles = generateArray(LifecycleJson.class, getJSONArray((String)args));
		}else if("applications".equals(name)){
			String str = (String)args;
			String[] mebs = str.split(",");
			applications  = Arrays.asList(mebs);
			logger.info(applications);
		}else if("systemproperty".equals(name)){
			String str = (String)args;
			String[] mebs = str.split(",");
			systemproperty  = Arrays.asList(mebs);
			logger.info(systemproperty);
			if(systemproperty!=null && systemproperty.size()>0)
				for(String propery : systemproperty){
					if(propery.indexOf(":")>-1){
						String key = propery.split(":")[0];
						String value = propery.split(":")[1];
						System.setProperty(key.trim(), value.trim());
						logger.info(String.format("system property --> %s : %s", key.trim(),value.trim()));
					}
				}
		}
	}


	public List<String> getApplications() {
		return applications;
	}


	public void setApplications(List<String> applications) {
		this.applications = applications;
	}


	public List<String> getSystemproperty() {
		return systemproperty;
	}


	public void setSystemproperty(List<String> systemproperty) {
		this.systemproperty = systemproperty;
	}
}
