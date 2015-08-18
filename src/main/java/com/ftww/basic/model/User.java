package com.ftww.basic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

/**
 * 用户Model
 * @author FireTercel 2015年7月29日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_user")
public class User extends BaseModel<User> {

	private static final long serialVersionUID = -7337979166914979909L;
	
	public static final User dao = new User();
	
	/**
	 * 查询用户详细信息
	 * @return
	 */
	public UserInfo findUserInfo(){
		Long userinfoid = getLong("userinfoid");
		if(userinfoid != null){
			return UserInfo.dao.findById(userinfoid);
		}
		return null;
	}

	/**
	 * 查询用户所有部门
	 * @return
	 */
	public List<Department> findDepartments(){
		List<DepartmentUser> lists = DepartmentUser.dao.findByUserId(getPKValueLong());
		List<Department> departmentList = new ArrayList<Department>();
		for(DepartmentUser list : lists){
			Long departmentid = list.getLong("departmentid");
			departmentList.add(Department.dao.findById(departmentid));
		}
		return departmentList;
	}

	/**
	 * 查询用户入职信息
	 * @return
	 */
	public Company findCompany(){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", Company.dao.getTableName());
		param.put("column", "userid");
		Company company = Company.dao.findFirst(getSql(sqlId_select, param), getPKValueLong());
		return company;
	}
	
	/**
	 * 查询用户所有角色
	 * @return
	 */
	public List<Role> findRoles(){
		List<RoleUser> lists = RoleUser.dao.findByUserId(getPKValueLong());
		List<Role> roleList = new ArrayList<Role>();
		for(RoleUser list : lists){
			Long roleid = list.getLong("roleid");
			roleList.add(Role.dao.findById(roleid));
		}
		return roleList;
	}
	

}
