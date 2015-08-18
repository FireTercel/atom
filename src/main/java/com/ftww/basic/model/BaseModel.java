package com.ftww.basic.model;

import com.ftww.basic.kits.SqlXmlKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Model基础类
 * @author FireTercel
 *
 * @param <M>
 */
public  class BaseModel<M extends Model<M>> extends Model<M> {

	private static final long serialVersionUID = -4816622475317218590L;
	
	private static Logger log=Logger.getLogger(BaseModel.class);
	
	private Table table;
	private String tableName;
	private String[] primaryKey;
	private String modelName;

	private String selectSql;
	private String fromSql;
	private String updateSql;
	private String deleteSql;
	private String dropSql;
	private String countSql;
	
	protected static String blank = " ";
	
	/**
	 * 字段描述：版本号 
	 * 字段类型 ：bigint 
	 */
	public static final String column_version = "version";
	/**
	 * sqlId : platform.baseModel.version
	 * 描述：查询版本号
	 */
	public static final String sqlId_version = "basic.baseModel.version";
	/**
	 * sqlId : platform.baseModel.select
	 * 描述：通用查询
	 */
	public static final String sqlId_select = "basic.baseModel.select";
	/**
	 * sqlId : platform.baseModel.selectCount
	 * 描述：通用查询
	 */
	public static final String sqlId_selectCount = "basic.baseModel.selectCount";
	/**
	 * sqlId : platform.baseModel.update
	 * 描述：通用查询
	 */
	public static final String sqlId_update = "basic.baseModel.update";
	/**
	 * sqlId : platform.baseModel.delete
	 * 描述：通用查询
	 */
	public static final String sqlId_delete = "basic.baseModel.delete";
	
	/**
	 * 获得属性列表
	 */
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
	
	/**
	 * 将Model转为Json
	 * @return
	 */
	public String getJson() {
		return JsonKit.toJson(this);
	}
	
	public List<M> findAll() {
		return find(getSelectSql() + getFromSql());
	}
	
	public List<M> findBy(String where, Object... paras) {
		return find(getSelectSql() + getFromSql() + getWhere(where), paras);
	}
	
	/**
	 * 查询前N个数据
	 * @param topNumber
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<M> findTopBy(int topNumber, String where, Object... paras) {
		return paginate(1, topNumber, getSelectSql(),
				getFromSql() + getWhere(where), paras).getList();
	}
	
	/**
	 * 查询第一个数据
	 * @param where
	 * @param paras
	 * @return
	 */
	public M findFirstBy(String where, Object... paras) {
		return findFirst(getSelectSql() + getFromSql() + getWhere(where), paras);
	}
	
	/**
	 * 全部分页
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<M> paginateAll(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, getSelectSql(), getFromSql());
	}
	
	/**
	 * 条件分页
	 * @param pageNumber
	 * @param pageSize
	 * @param where
	 * @param paras
	 * @return
	 */
	public Page<M> paginateBy(int pageNumber, int pageSize, String where,
			Object... paras) {
		return paginate(pageNumber, pageSize, getSelectSql(), getFromSql()
				+ getWhere(where), paras);
	}
	
	/**
	 * 更新所有
	 * @param set
	 * @param paras
	 * @return
	 */
	public boolean updateAll(String set, Object... paras) {
		return Db.update(getUpdateSql() + getSet(set), paras) > 0;
	}
	
	/**
	 * 条件更新
	 * @param set
	 * @param where
	 * @param paras
	 * @return
	 */
	public boolean updateBy(String set, String where, Object... paras) {
		return Db.update(getUpdateSql() + getSet(set) + getWhere(where), paras) > 0;
	}
	
	/**
	 * 设置所有不可用
	 * @return
	 */
	public boolean deleteAll() {
		return Db.update(getDeleteSql()) > 0;
	}
	
	/**
	 * 条件设置不可用
	 * @param where
	 * @param paras
	 * @return
	 */
	public boolean deleteBy(String where, Object... paras) {
		return Db.update(getDeleteSql() + getWhere(where), paras) > 0;
	}
	
	/**
	 * 删除所有
	 * @return
	 */
	public boolean dropAll() {
		return Db.update(getDropSql()) > 0;
	}
	
	/**
	 * 条件删除
	 * @param where
	 * @param paras
	 * @return
	 */
	public boolean dropBy(String where, Object... paras) {
		return Db.update(getDropSql() + getWhere(where), paras) > 0;
	}
	
	/**
	 * 统计所有
	 * @return
	 */
	public Long countAll() {
		return Db.queryFirst(getCountSql());
	}
	
	/**
	 * 条件统计
	 * @param where
	 * @param paras
	 * @return
	 */
	public Long countBy(String where, Object... paras) {
		return Db.queryFirst(getCountSql() + getWhere(where), paras);
	}
	
	/**
	 * SET语句
	 * @param set
	 * @return
	 */
	protected String getSet(String set) {
		if (set != null && !set.isEmpty()
				&& !set.trim().toUpperCase().startsWith("SET")) {
			set = " SET " + set;
		}
		return set;
	}

	/**
	 * WHERE语句
	 * @param where
	 * @return
	 */
	protected String getWhere(String where) {
		if (where != null && !where.isEmpty()
				&& !where.trim().toUpperCase().startsWith("WHERE")) {
			where = " WHERE " + where;
		}
		return where;
	}
	
	/**
	 * 获取表映射对象
	 * @return 
	 */
	protected Table getTable(){
		if (table == null) {
			table = TableMapping.me().getTable(getClass());
		}
		return table;
	}
	
	/**
	 * 获取表名
	 * @return
	 */
	public String getTableName() {
		if (tableName == null) {
			tableName = getTable().getName();
		}
		return tableName;
	}

	/**
	 * 获取model名称 小写，作为表的 别名
	 * @return
	 */
	public String getModelName() {
		if (modelName == null) {
			byte[] items = getClass().getSimpleName().getBytes();
			items[0] = (byte) ((char) items[0] + ('a' - 'A'));
			modelName = new String(items);
		}
		return modelName;
	}
	
	/**
	 * 是否存在该属性
	 * @param column
	 * @return boolean
	 */
	public boolean hasColumn(String column){
		return getTable().hasColumnLabel(column);
	}
	
	public String getPKName(){
		return getPKNameArr()[0];
	}
	
	/**
	 * 获取主键名称
	 * @return
	 */
	public String[] getPKNameArr(){
		if(primaryKey == null){
			primaryKey = getTable().getPrimaryKey();
		}
		return primaryKey;
	}

	/**
	 * 获取主键名称
	 * @return
	 */
	public String getPKNameStr() {
		String[] pkArr = getPKNameArr();
		StringBuffer pk = new StringBuffer();
		for (int index = 0; index < pkArr.length; index++) {
			pk.append(pkArr[index]);
			if (index != pkArr.length - 1) {
				pk.append(",");
			}
		}
		return pk.toString();
	}
	
	/**
	 * 获取主键值：非复合主键
	 * @return
	 */
	public String getPKValue(){
		String[] pkNameArr = getPKNameArr();
		StringBuffer pk = new StringBuffer();
		for (int index = 0; index < pkNameArr.length; index++) {
			pk.append(this.getStr(pkNameArr[index]));
			if (index != pkNameArr.length - 1) {
				pk.append(",");
			}
		}
		return pk.toString();
	}
	
	/**
	 * 获得主键值，第一个主键。
	 * @return Long
	 */
	public Long getPKValueLong(){
		return this.getLong(getPKName());
	}

	/**
	 * 获取主键值：复合主键
	 * @return
	 */
	public List<Object> getPKValueList(){
		String[] pkNameArr = getTable().getPrimaryKey();
		
		List<Object> pkValueList = new ArrayList<Object>();
		for (String pkName : pkNameArr) {
			pkValueList.add(this.get(pkName));
		}
		
		return pkValueList;
	}
	

	/**
	 * 查询语句SELECT
	 * @return select `model`.* 
	 */
	public String getSelectSql() {
		if (selectSql == null) {
			selectSql = " SELECT `" + getModelName() + "`.* ";
		}
		return selectSql;
	}

	/**
	 * 查询语句FROM
	 * @return from table `model` 
	 */
	public String getFromSql() {
		if (fromSql == null) {
			fromSql = " FROM " + getTableName() + " `" + getModelName() + "` ";
		}
		return fromSql;
	}

	/**
	 * 更新语句UPDATE
	 * @return update table `model` 
	 */
	public String getUpdateSql() {
		if (updateSql == null) {
			updateSql = " UPDATE " + getTableName() + " `" + getModelName()
					+ "` ";
		}
		return updateSql;
	}

	/**
	 * 删除语句UPDATE 设置不可用
	 * @return update table `model` set `model`.deleted_at='new Date()' 
	 */
	public String getDeleteSql() {
		if (deleteSql == null) {
			deleteSql = " UPDATE " + getTableName() + " `" + getModelName()
					+ "` SET `" + getModelName() + "`.deleted_at='"
					+ new Date() + "' ";
		}
		return deleteSql;
	}

	/**
	 * 删除语句DELETE
	 * @return delete from table 
	 */
	public String getDropSql() {
		if (dropSql == null) {
			dropSql = " DELETE FROM " + getTableName() + " ";
		}
		return dropSql;
	}

	/**
	 * 统计语句 SELECT COUNT(*)
	 * @return select count(*) count from table `model` 
	 */
	public String getCountSql() {
		if (countSql == null) {
			countSql = " SELECT COUNT(*) count FROM " + getTableName() + " `"
					+ getModelName() + "` ";
		}
		return countSql;
	}

	/**
	 * 获取SQL，固定SQL
	 * @param sqlId
	 * @return
	 */
	protected String getSql(String sqlId){
		return SqlXmlKit.getSql(sqlId);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,Object> param){
		return SqlXmlKit.getSql(sqlId, param);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param 查询参数
     * @param list 用于接收预处理的值
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,String> param,LinkedList<Object> list){
		return SqlXmlKit.getSql(sqlId, param, list);
	}
	
	
	/**
	 * 根据i18n参数查询获取哪个字段的值
	 * @param i18n
	 * @return
	 */
	protected String i18n(String i18n){
		return null;
	}
	
	/**
	 * 重写save方法
	 */
	public boolean save(){
		//设置主键值
		/*String[] pkArr = getTable().getPrimaryKey();
		for (String pk : pkArr) {
			this.set(pk, ToolUtils.getUuidByJdk(true)); // 设置主键值（UUID类型主键）
		}*/
		// 是否需要乐观锁控制
		if(hasColumn(column_version)){
			// 初始化乐观锁版本号
			this.set(column_version, Long.valueOf(0));
		}
		return super.save();
	}
	
	/**
	 * 重写save方法
	 */
	/*public boolean save(Map<String, Object> pkMap) {
		Set<String> pkSet = pkMap.keySet();
		for (String pk : pkSet) {
			this.set(pk, pkMap.get(pk)); // 设置主键值
		}
		
		if(getTable().hasColumnLabel(column_version)){ // 是否需要乐观锁控制
			this.set(column_version, Long.valueOf(0)); // 初始化乐观锁版本号
		}
		
		return super.save();
	}*/
	
	/**
	 * 重写update方法
	 */
	@SuppressWarnings("unchecked")
	public boolean update(){
		boolean hasVersion = hasColumn(column_version);
		// 是否需要乐观锁控制，表是否有version字段
		if(hasVersion){
			Set<String> modifyFlag = null;
			try {
				Field field = null;
				if(this.getClass().getSuperclass().getName().endsWith("BaseModelCache")){
					field = this.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("modifyFlag");
				}else{
					field = this.getClass().getSuperclass().getSuperclass().getDeclaredField("modifyFlag");
				}
				field.setAccessible(true);
				Object object = field.get(this);
				if(null != object){
					modifyFlag = (Set<String>) object;
				}
				field.setAccessible(false);
			} catch (NoSuchFieldException | SecurityException e) {
				log.error("业务Model类必须继承BaseModel");
				e.printStackTrace();
				throw new RuntimeException("业务Model类必须继承BaseModel");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				log.error("BaseModel访问modifyFlag异常");
				e.printStackTrace();
				throw new RuntimeException("BaseModel访问modifyFlag异常");
			}
			boolean versionModify = modifyFlag.contains(column_version); // 表单是否包含version字段
			if(versionModify){
				Long versionForm = getNumber(column_version).longValue() + 1; // 表单中的版本号
				return super.updateByVersion(versionForm); // ***** 增加updateByVersion方法 ***** //
			}
		}
		return super.update();
	}
	
	/**
	 * 针对Oracle做特殊处理
	 */
	public Date getDate(String attr){
		Object obj=this.get(attr);
		if(null == obj){
			return null;
		}
		return (Date) obj;
	}
	
}
