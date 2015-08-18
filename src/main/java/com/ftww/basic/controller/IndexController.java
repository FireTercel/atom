package com.ftww.basic.controller;

import com.ftww.basic.annotation.Controller;
import com.ftww.basic.model.User;
import com.ftww.basic.plugin.shiro.core.SubjectKit;

@Controller(controllerKey = "/")
public class IndexController extends BaseController{
	
	public void index(){
		redirect("/ftww/basic/signin");
	}
	

}
