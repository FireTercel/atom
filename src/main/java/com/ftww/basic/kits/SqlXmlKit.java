package com.ftww.basic.kits;

import org.apache.log4j.Logger;
import org.beetl.core.BeetlKit;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理SQL Map
 * @author FireTercel
 *说明：加载sql map中的sql到map中，并提供动态长度sql处理
 */
public class SqlXmlKit {
	
	protected static final Logger log= Logger.getLogger(SqlXmlKit.class);
	
	/**
	 * 保存xml中所有的sql语句
	 */
	private static final Map<String ,String> sqlMap=new HashMap<String,String>();
	
	/**
	 * 过滤掉的sql关键字
	 */
	private static final List<String> badKeyWordList=new ArrayList<String>();
	
	/**
	 * 加载关键字到List
	 */
	static {
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
		badKeyWordList.addAll(Arrays.asList(badStr.split("\\|")));
	}
	
	/**
	 * sql查询关键字过滤效验 防止注入
	 * @param queryStr
	 * @return
	 */
	public static boolean keywordVali(String queryStr){
		//统一转为小写
		queryStr = queryStr.toLowerCase();
		for(String badKeyWord:badKeyWordList){
			//indexOf()方法，如果匹配不到 return -1
			if(queryStr.indexOf(badKeyWord)>=0){
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * 获取SQL，固定SQL
	 * @param sqlId
	 * @return
	 */
	public static String getSql(String sqlId){
		String sql = sqlMap.get(sqlId);
		if(null ==sql || sql.isEmpty()){
			log.error("sql语句不存在：sql id是" + sqlId);
		}
		return sql.replaceAll("[\\s]{2,}", " ");
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public static String getSql(String sqlId,Map<String,Object> param){
		String sqlTemplete = sqlMap.get(sqlId);
		if(null== sqlTemplete || sqlTemplete.isEmpty()){
			log.error("sql语句不存在：sql id是" + sqlId);
		}
		String sql = BeetlKit.render(sqlTemplete, param);
		Set<String> keySet = param.keySet();
		for(String key:keySet){
			if(param.get(key) == null){
				break;
			}
			String value = (String) param.get(key);
			value= value.replace("'", "").replace(";","").replace("--", "");
			sql = sql.replace("#"+key+"#", value);
		}
		return sql.replaceAll("[\\s]{2,}", " ");
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param 查询参数
     * @param list 用于接收预处理的值
	 * @return
	 */
	public static String getSql(String sqlId,Map<String,String> param,LinkedList<Object> list){
		String sqlTemplete = sqlMap.get(sqlId);
		if(null == sqlTemplete || sqlTemplete.isEmpty()){
			log.error("sql语句不存在：sql id是" + sqlId);
    	}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Set<String> paramKeySet = param.keySet();
		
		for(String paramKey : paramKeySet){
			paramMap.put(paramKey, (Object)param.get(paramKey));
		}
		String sql = BeetlKit.render(sqlTemplete, paramMap);
		
		// 匹配模式为 #'%$names$%'#
		Pattern pattern = Pattern.compile("#[\\w\\d\\$\\'\\%\\_]+#");
		// 匹配模式为 $names$
		Pattern pattern2 = Pattern.compile("\\$[\\w\\d\\_]+\\$");
		
		Matcher matcher = pattern.matcher(sql);
		
		while(matcher.find()){
			// 得到的结果形式：#'%$names$%'#
			String clounm =  matcher.group(0);
			
			Matcher matcher2=  pattern2.matcher(clounm);
			matcher2.find();
			// 得到的结果形式：$names$
			String clounm2 = matcher2.group(0); 
			
			String clounm3 = clounm2.replace("$", "");
			
			//  数值型，可以对应处理int、long、bigdecimal、double等等
			if(clounm.equals("#"+clounm2+"#")){
				String val = (String) param.get(clounm3);
				
				try{
					Integer.parseInt(val);
					sql=sql.replace(clounm, val);
				}catch(NumberFormatException e){
					log.error("查询参数值错误，整型值传入了字符串，非法字符串是：" + val);
					return null;
				}
				
			}
			//  字符串，主要是字符串模糊查询、日期比较的查询
			else{
				String val = (String) param.get(clounm3);
				
				String clounm4 = clounm.replace("#", "").replace("'", "").replace(clounm2, val);
				list.add(clounm4);
				
				sql=sql.replace(clounm, "?");
			}
		}
		return sql.replace("[\\s]{2,}", " ");
	}
	
	/**
	 * 清除加载的SQL语句
	 */
	public static void destroy(){
		sqlMap.clear();
	}
	
	/**
	 * 初始化加载sql语句到map。PS：新版的uib采用缓存存储这些sql语句，可以借鉴
	 * @param isInit
	 */
	public static synchronized void init(boolean isInit){
		//检查路径是否正确
		String classRootPath = SqlXmlKit.class.getClassLoader().getResource("").getFile();
		try{
			classRootPath = java.net.URLDecoder.decode(classRootPath,"UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			log.error("初始化加载sql：获取classRootPath异常");
		}
		File file = new File(classRootPath);
		List<File> files = new ArrayList<File>();
		
		findFiles(file,files);
		SAXReader reader = new SAXReader();
		String fileName = null;
		try{
			for(File xmlfile:files){
				fileName = xmlfile.getName();
				Document doc = reader.read(xmlfile);
				Element root = doc.getRootElement();
				String namespace = root.attributeValue("namespace");
				
				if(null == namespace || namespace.trim().isEmpty()){
					log.error("sql xml文件" + fileName + "的命名空间不能为空");
					continue;
				}
				
				for(Iterator<?> iterTemp = root.elementIterator();iterTemp.hasNext();){
					Element element =(Element) iterTemp.next();
					if(element.getName().toLowerCase().equals("sql")){
						String id = element.attributeValue("id");
						if(null == id || id.trim().isEmpty()){
							log.error("sql xml文件" + fileName + "的存在没有id的sql语句");
							continue;
						}
						
						String sql = element.getText();
						if(null == sql || sql.trim().isEmpty()){
							log.error("sql xml文件" + fileName + "的存在没有内容的sql语句");
							continue;
						}
						
						String key = namespace +"."+ id;
						if(isInit && sqlMap.containsKey(key)){
							log.error("sql xml文件" + fileName + "的sql语句" + key + "的存在重复命名空间和ID");
							continue;
						} else if(sqlMap.containsKey(key)){
							log.error("sql xml文件" + fileName + "的sql语句" + key + "的存在重复命名空间和ID");
						}
						
						sql =  sql.replace("[\\s]{2,}", " ");
						sqlMap.put(key, sql);
						log.debug("sql加载, sql file = " + fileName + ", sql key = " + key + ", sql content = " + sql);
						
					}
				}
			}
		}catch(DocumentException e){
			log.error("sql xml文件" + fileName + "解析异常");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归查找文件
	 * @param baseFile
	 * @param sqlXmlFiles
	 */
	private static void findFiles(File baseFile,List<File> sqlXmlFiles){
		if(!baseFile.isDirectory()){
			if(baseFile.getName().endsWith(".sql.xml")){
				sqlXmlFiles.add(baseFile);
			}
		}else{
			File [] fileList = baseFile.listFiles();
			for(File file:fileList){
				if(file.isDirectory()){
					findFiles(file,sqlXmlFiles);
				}else{
					if(file.getName().endsWith(".sql.xml")){
						sqlXmlFiles.add(file);
					}
				}
			}
		}
	}
	

}
