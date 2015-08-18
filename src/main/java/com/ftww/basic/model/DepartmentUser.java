package com.ftww.basic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_department_user")
public class DepartmentUser extends BaseModel<DepartmentUser> {

	private static final long serialVersionUID = 8012215931599788793L;
	
	public static final DepartmentUser dao = new DepartmentUser();
	
	/**
	 * 查询用户所有部门
	 * @param userid
	 * @return
	 */
	public List<DepartmentUser> findByUserId(Long userid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", getTableName());
		param.put("column", "userid");
		return find(getSql(sqlId_select,param), userid);
	}
	
	/**
	 * 查询部门中所有用户
	 * @param departmentid
	 * @return
	 */
	public List<DepartmentUser> findByDepartmentId(Long departmentid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", getTableName());
		param.put("column", "departmentid");
		return find(getSql(sqlId_select,param), departmentid);
	}

}
