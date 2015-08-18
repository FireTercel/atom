package com.ftww.basic.controller;

import com.ftww.basic.annotation.Controller;
import com.ftww.basic.model.User;
import com.ftww.basic.plugin.shiro.core.SubjectKit;

@Controller(controllerKey = "/ftww/basic/signin")
public class SigninController extends BaseController{
	
	public void index(){
		User user = SubjectKit.getUser();
		if( null != user){
			redirect("/ftww/basic/role");
		}else {
			render("/ftww/signin/signin.html");
		}
	}
	
}
