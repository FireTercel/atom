package com.ftww.basic.controller;

import java.util.List;

import com.ftww.basic.annotation.Controller;
import com.ftww.basic.model.Button;
import com.ftww.basic.model.Company;
import com.ftww.basic.model.Function;
import com.ftww.basic.model.Role;
import com.ftww.basic.model.User;
import com.ftww.basic.plugin.shiro.core.SubjectKit;
import com.jfinal.core.ActionKey;

@Controller(controllerKey = "/ftww/basic/role")
public class RoleController extends BaseController{
	
	public void index(){
		User user = User.dao.findById(1);
		user.put("userinfo", user.findUserInfo());
		Company company = user.findCompany();
		company.put("position", company.findPosition());
		user.put("company", company);
		user.put("departmentList",user.findDepartments());
		Role role = Role.dao.findById(1);
		List<Function> functionList = role.findFunctions();
		for(Function function : functionList){
			List<Button> buttonList = function.findButtonsByRoleId(role.getId());
				function.put("buttonList", buttonList);
		}
		
		role.put("functionList",functionList);
		role.put("userList", role.findUsers());
		
		setAttr("user", user);
		setAttr("role",role);
		render("index.html");
	}
	
}
