package com.ftww.basic.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftww.basic.model.Department;
import com.ftww.basic.model.User;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 部门业务层
 * @author FireTercel 2015年8月6日 
 *
 */
public class DepartmentService extends BaseService {
	
private static Logger log = LoggerFactory.getLogger(DepartmentService.class);
	
	public static final DepartmentService service = Enhancer.enhance(DepartmentService.class, Tx.class);
	
	/**
	 * 查询下属部门所有用户
	 * @param department
	 * @return childUserList
	 */
	public List<User> findChildUsers(Department department){
		List<User> childUserList = new ArrayList<User>();
		List<Department> childList = department.findChildDepartments();
		for(Department child : childList){
			List<User> userList = child.findUsers();
			childUserList.addAll(userList);
		}
		return childUserList;
		
	}

}
