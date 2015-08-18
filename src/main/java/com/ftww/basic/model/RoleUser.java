package com.ftww.basic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_role_user")
public class RoleUser extends BaseModel<RoleUser> {

	private static final long serialVersionUID = 876325497351626179L;
	
	public static final RoleUser dao = new RoleUser();
	
	/**
	 * 查询角色所有用户
	 * @param roleid
	 * @return
	 */
	public List<RoleUser> findByRoleId(Long roleid) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", getTableName());
		param.put("column", "roleid");
		return find(getSql(sqlId_select,param), roleid);
	}
	
	/**
	 * 查询用户所有角色
	 * @param userid
	 * @return
	 */
	public List<RoleUser> findByUserId(Long userid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", getTableName());
		param.put("column", "userid");
		return find(getSql(sqlId_select,param), userid);
	}

}
