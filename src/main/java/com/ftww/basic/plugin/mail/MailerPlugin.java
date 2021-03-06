package com.ftww.basic.plugin.mail;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftww.basic.kits.PropertiesKit;
import com.jfinal.plugin.IPlugin;

/**
 * 需要再做修改。
 * @author FireTercel 2015年8月3日 
 *
 */
public class MailerPlugin implements IPlugin{
	private Logger log = LoggerFactory.getLogger(MailerPlugin.class);
	
	private String config = "application.properties";
	private Properties properties;

	private String host;
	private String sslport;
	private String timeout;
	private String connectout;
	private String port;
	private String ssl;
	private String tls;
	private String debug;
	private String user;
	private String password;
	private String name;
	private String from;
	private String encode;
	
	public static MailerConf mailerConf;
	
	public MailerPlugin(){}
	
	public MailerPlugin(String config){
		this.config = config;
	}

	@Override
	public boolean start() {
		properties = PropertiesKit.me().loadPropertyFile(config);
		host = properties.getProperty("smtp.host","");
		if(host == null || host.isEmpty()){
			throw new RuntimeException("email host has not found!");
		}
		port = properties.getProperty("smtp.port", "");
		ssl = properties.getProperty("smtp.ssl", "false");
	    sslport = properties.getProperty("smtp.sslport", "");
	    timeout = properties.getProperty("smtp.timeout", "60000");
	    connectout = properties.getProperty("smtp.connectout", "60000");
	    tls = properties.getProperty("smtp.tls", "false");
	    debug = properties.getProperty("smtp.debug", "false");
	    user = properties.getProperty("smtp.user", "");
	    
		if (user == null || user.isEmpty()) {
			throw new RuntimeException("email user has not found!");
		}
		password = properties.getProperty("smtp.password", "");
		if (password == null || password.isEmpty()) {
			throw new RuntimeException("email password has not found!");
		}
		name = properties.getProperty("smtp.name", "");
		from = properties.getProperty("smtp.from", "");
		if(from == null || from.isEmpty()){
			throw new RuntimeException("email from has not found!");
		}
		
		encode = properties.getProperty("smtp.encode", "UTF-8");
		mailerConf = new MailerConf(host, sslport, Integer.parseInt(timeout), Integer.parseInt(connectout), port, Boolean.parseBoolean(ssl), Boolean.parseBoolean(tls), Boolean.parseBoolean(debug), user, password, name, from, encode);

		log.debug("插件MailerPlugin 启动。");
		return true;
	}

	@Override
	public boolean stop() {
	    host = null;
	    port = null;
	    ssl = null;
	    user = null;
	    password = null;
	    name = null;
	    from = null;
	    log.debug("插件MailerPlugin 停止。");
	    return true;
	}
	

}
