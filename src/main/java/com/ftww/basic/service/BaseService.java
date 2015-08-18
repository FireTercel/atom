package com.ftww.basic.service;

import com.ftww.basic.common.SplitPage;
import com.ftww.basic.kits.SqlXmlKit;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Map;

/**
 * 基础业务层
 * @author FireTercel 2015年8月6日 
 *
 */
public class BaseService {
	
	private static Logger log = LoggerFactory.getLogger(BaseService.class);
	
	public static final BaseService service = Enhancer.enhance(BaseService.class, Tx.class);
	
	/**
	 * 分页
	 * @param dataSource
	 * @param splitPage
	 * @param selectContent
	 * @param fromSqlId
	 */
	public void splitPageBase(String dataSource, SplitPage splitPage, String selectContent, String fromSqlId){
		//接收返回值对象
		StringBuilder fromSqlSb = new StringBuilder();
		LinkedList<Object> paramValue = new LinkedList<Object>();
		
		//调用生成form sql，并构建paramValue
		String sql = SqlXmlKit.getSql(fromSqlId, splitPage.getQueryParam(), paramValue);
		fromSqlSb.append(sql);
		
		//行级过滤
		rowFilter(fromSqlSb);
		
		//排序
		String orderColunm = splitPage.getOrderColunm();
		String orderMode = splitPage.getOrderMode();
		if( null != orderColunm && !orderColunm.isEmpty() && null != orderMode && !orderMode.isEmpty()) {
			fromSqlSb.append(" order by ").append(orderColunm).append(" ").append(orderMode);
		}
		
		String formSql = fromSqlSb.toString();
		
		Page<?> page = Db.use(dataSource).paginate(splitPage.getPageNumber(), splitPage.getPageSize(), selectContent, formSql, paramValue.toArray());
		
		splitPage.setTotalPage(page.getTotalPage());
		splitPage.setTotalRow(page.getTotalRow());
		splitPage.setList(page.getList());
		splitPage.compute();
	}
	/**
	 * 行级过滤
	 * @param formSqlSb
	 */
	public void rowFilter(StringBuilder fromSqlSb){
		
	}
	
	/**
	 * 分页
	 * @param dataSource
	 * @param splitPage
	 * @param selectSqlId
	 * @param fromSqlId
	 */
	public void splitPageBySqlId(String dataSource, SplitPage splitPage, String selectSqlId, String fromSqlId){
		String selectSql = getSql(selectSqlId);
		splitPageBase(dataSource, splitPage, selectSql, fromSqlId);
	}
	
	public String getSql(String sqlId){
		return SqlXmlKit.getSql(sqlId);
	}
	
	public String getSql(String sqlId, Map<String, Object> param){
		return SqlXmlKit.getSql(sqlId, param);
	}
	
	public String getSql(String sqlId, Map<String, String> param, LinkedList<Object> list){
		return SqlXmlKit.getSql(sqlId, param, list);
	}

}
