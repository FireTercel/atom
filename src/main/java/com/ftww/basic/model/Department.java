package com.ftww.basic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.model.treeNode.TreeNode;

/**
 * 部门Model
 * @author FireTercel 2015年8月6日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_department")
public class Department extends BaseModel<Department> implements TreeNode<Department> {

	private static final long serialVersionUID = -8708962897392784647L;
	
	public static final Department dao = new Department();
	
	@Override
	public Long getId() {
		return this.getLong("id");
	}

	@Override
	public Long getParentId() {
		return this.getLong("pid");
	}

	@Override
	public List<Department> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Department> children) {
		this.put("children", children);
	}
	
	/**
	 * 关联查询，查询部门中所有用户
	 * @return
	 */
	public List<User> findUsers(){
		List<DepartmentUser> lists = DepartmentUser.dao.findByDepartmentId(getPKValueLong());
		List<User> userList = new ArrayList<User>();
		for(DepartmentUser list : lists){
			Long userid = list.getLong("userid");
			userList.add(User.dao.findById(userid));
		}
		return userList;
	}
	
	/**
	 * 获得子部门集
	 * @return
	 */
	public List<Department> findChildDepartments(){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", Department.dao.getTableName());
		param.put("column", "pid");
		return find(getSql(sqlId_select, param), this.getPKValueLong());
	}
	
	/**
	 * 查找指定部门的所有叶子部门，保存在List里面。
	 * @param department
	 * @param allChildList
	 */
	public void findAllChildDepartments(Department department, List<Department> allChildList){
		List<Department> childList = department.findChildDepartments();
		if(childList.isEmpty()){
			allChildList.add(department);
		}else {
			for(Department child : childList){
				if(child.findChildDepartments().isEmpty()){
					allChildList.add(child);
				}else{
					findAllChildDepartments(child, allChildList);
				}
			}
		}
	}

	

}
