package com.ftww.basic.run;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.ftww.basic.beetl.func.EscapeXml;
import com.ftww.basic.beetl.func.OrderBy;
import com.ftww.basic.beetl.render.MyBeetlRenderFactory;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.handler.SessionIdHandler;
import com.ftww.basic.kits.StringKit;
import com.ftww.basic.plugin.ControllerPlugin;
import com.ftww.basic.plugin.TablePlugin;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.ftww.basic.plugin.shiro.core.ShiroInterceptor;
import com.ftww.basic.plugin.shiro.core.ShiroPlugin;
import com.ftww.basic.plugin.shiro.realm.MyJdbcAuthzService;
import com.ftww.basic.plugin.sqlinxml.SqlXmlPlugin;
import com.ftww.basic.plugin.web.UrlInterceptor;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionKeys;
import com.jfinal.plugin.activerecord.tx.TxByRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import org.beetl.core.GroupTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AppConfig extends JFinalConfig{
	
	private static Logger log = LoggerFactory.getLogger(AppConfig.class);

	Routes routes;
	
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		log.info(" == Constants == 缓存 Properties配置文件中的配置信息！ ");
		new PropertiesPlugin(loadPropertyFile("init.properties")).start();
		log.info(" == Constants == 设置字符集！ ");
		me.setEncoding(StringKit.encoding);
		log.info(" == Constants == 设置开发模式 ");
		me.setDevMode(getPropertyToBoolean(DictKeys.config_devMode, false));
		log.info("  == Constants == 设置Beetl视图 ");
		me.setMainRenderFactory(new MyBeetlRenderFactory());
		GroupTemplate groupTemplate = MyBeetlRenderFactory.groupTemplate;
		groupTemplate.registerFunction("orderBy", new OrderBy());
		groupTemplate.registerFunction("escapeXml", new EscapeXml());
		log.info("  == Constants == 视图error page设置 ");
		me.setError401View("/common/401.html");
		me.setError403View("/common/403.html");
		me.setError404View("/common/404.html");
		me.setError500View("/common/500.html");
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		log.info("  == Routes ==  路由扫描注册 ");
		this.routes=me;
		new ControllerPlugin(me).start();
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		log.info("  == Plugins--DruidPlugin ==   配置Druid数据库连接池连接属性 ");
		DruidPlugin druidPlugin = new DruidPlugin(
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_jdbcUrl), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_userName), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_passWord), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_driverClass));
		
		log.info("  == Plugins--DruidPlugin ==    配置Druid数据库连接池大小 ");
		druidPlugin.set(
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_initialSize), 
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_minIdle), 
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_maxActive));
		log.info("  == Plugins--DruidPlugin ==    StatFilter提供JDBC层的统计信息 ");
		druidPlugin.addFilter(new StatFilter());
		log.info("  == Plugins--DruidPlugin ==    WallFilter防御SQL注入攻击 ");
		WallFilter wallFilter=new WallFilter();
		wallFilter.setDbType("h2");
		druidPlugin.addFilter(wallFilter);
		
		log.info("  == Plugins--ActiveRecordPlugin == 配置ActiveRecord插件 ");
		ActiveRecordPlugin arpMain = new ActiveRecordPlugin(DictKeys.db_dataSource_main, druidPlugin);
		//arp.setTransactionLevel(4);//事务隔离级别
		//忽略字段大小写(Container集装箱、sensitive敏感的)
		arpMain.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		// 设置开发模式
		arpMain.setDevMode(getPropertyToBoolean(DictKeys.config_devMode, false)); 
		// 是否显示SQL
		arpMain.setShowSql(getPropertyToBoolean(DictKeys.config_devMode, false)); 
		
		log.info("  == Plugins ==   数据库类型判断 ");
		String db_type = (String) PropertiesPlugin.getParamMapValue(DictKeys.db_type_key);
		if(db_type.equals(DictKeys.db_type_postgresql)){
			log.info("  == Plugins ==    使用数据库类型是 postgresql ");
			arpMain.setDialect(new PostgreSqlDialect());
			
		}else if(db_type.equals(DictKeys.db_type_mysql)){
			log.info("  == Plugins ==    使用数据库类型是 mysql ");
			arpMain.setDialect(new MysqlDialect());
		
		}else if(db_type.equals(DictKeys.db_type_oracle)){
			log.info("  == Plugins ==    使用数据库类型是 oracle ");
			druidPlugin.setValidationQuery("select 1 FROM DUAL"); //指定连接验证语句(用于保存数据库连接池), 这里不加会报错误:invalid oracle validationQuery. select 1, may should be : select 1 FROM DUAL 
			arpMain.setDialect(new OracleDialect());
		}
		
		log.info("  == Plugins ==    添加druidPlugin插件 ");
		me.add(druidPlugin); // 多数据源继续添加
		
		log.info("  == Plugins--TablePlugin == 表扫描注册 ");
		Map<String, ActiveRecordPlugin> arpMap = new HashMap<String, ActiveRecordPlugin>();
		arpMap.put(DictKeys.db_dataSource_main, arpMain); // 多数据源继续添加
		new TablePlugin(arpMap).start();
		me.add(arpMain); // 多数据源继续添加
		
		log.info("  == Plugins--EhCachePlugin ==  EhCache缓存 ");
		me.add(new EhCachePlugin());
		log.info("  == Plugins--SqlXmlPlugin ==   解析并缓存 xml sql ");
		me.add(new SqlXmlPlugin());
		log.info("  == Plugins--ShiroPlugin ==  shiro权限框架 ");
	    me.add(new ShiroPlugin(routes, new MyJdbcAuthzService())); 
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		log.info("  == Interceptors ==   支持使用session ");
		me.add(new SessionInViewInterceptor());
		log.info("  == Interceptors ==   添加shiro的过滤器 ");
		me.add(new ShiroInterceptor());
		log.info("  == Interceptors ==   添加URL拦截 ");
		me.add(new UrlInterceptor());
		
		// 配置开启事物规则
		me.add(new TxByRegex(".*save.*"));
		me.add(new TxByRegex(".*update.*"));
		me.add(new TxByRegex(".*delete.*"));
		me.add(new TxByActionKeys("/ftww/wx/message", "/ftww/wx/message/index"));
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ContextPath"));
		me.add(new SessionIdHandler());
	}
	
	/**
	 * 运行此 main 方法可以启动项目
	 * @param args
	 */
	public static void main(String [] args){
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

}
