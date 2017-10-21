package com.connectz.util;

import java.io.File;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;


public class JettyServer {
	private int port;
	private WebAppContext webapp;
	private ServletContextHandler context;
	private ResourceHandler resource_handler;
	private Server server;
	
	public JettyServer(int port){
		this.port = port;
	}
	
	public void start() throws Exception{
		this.server = new Server(port);
		 // Add the ResourceHandler to the server.
        HandlerList handlers = new HandlerList();
		if(this.resource_handler!=null)
			handlers.addHandler(resource_handler);
		if(this.context!=null)
			handlers.addHandler(context);
		if(this.webapp!=null){
			handlers.addHandler(webapp);
		}
		handlers.addHandler(new DefaultHandler());
		
		server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);
		server.setHandler(handlers);
		server.start();
        server.join();
	}
	
	public void stop() throws Exception{
		this.server.stop();
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public ServletContextHandler getContext() {
		return context;
	}


	public void setContext(ServletContextHandler context) {
		this.context = context;
	}

	public void setContext(String ContextPath,String ResourceBase) {
		this.context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath(ContextPath==null?"/":ContextPath);
        context.setResourceBase(ResourceBase==null?System.getProperty("java.io.tmpdir"):ResourceBase);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());  
	}
	
	public void addServlet(Class<? extends HttpServlet> servlet, String ContextPath) throws Exception{
		addServlet(servlet,ContextPath,false);
	}
	
	public void addServlet(Class<? extends HttpServlet> servlet, String ContextPath, boolean webapp) throws Exception{
		if(!webapp){
			if(this.context!=null){
				this.context.addServlet(new ServletHolder(servlet), ContextPath);
			}else{
				throw new Exception("ServletContextHandler is Null");
			}
		}else{
			if(this.webapp!=null){
				this.webapp.addServlet(new ServletHolder(servlet), ContextPath);
			}else{
				throw new Exception("WebAppContext is Null");
			}
		}
		
	}

	public ResourceHandler getResource_handler() {
		return resource_handler;
	}


	public void setResource_handler(ResourceHandler resource_handler) {
		this.resource_handler = resource_handler;
	}

	public void setResource_handler(String ResourceBase, String[] WelcomeFiles) {
		this.resource_handler = new ResourceHandler();
		// Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(WelcomeFiles==null?new String[]{ "index.html" }:WelcomeFiles);
        resource_handler.setResourceBase(ResourceBase==null?".":ResourceBase);
	}

	public WebAppContext getWebapp() {
		return webapp;
	}

	public void setWebapp(WebAppContext webapp) {
		this.webapp = webapp;
	}
	
	public void setWebapp(String ContextPath, String DefaultsDescriptor, File warFile) {
		this.webapp = new WebAppContext();  
		webapp.setContextPath(ContextPath);  
		   
		// Additional web application descriptor containing test components.  
		webapp.setDefaultsDescriptor(DefaultsDescriptor);  
		webapp.setWar(warFile.getAbsolutePath());
	}
}
