package com.ftww.basic.plugin.web;

import javax.servlet.http.HttpServletRequest;

import com.ftww.basic.kits.ReturnKit;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class UrlInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		//webRoot
		controller.setAttr("_webRootPath", request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath());
		inv.invoke();
		
		if( ! ReturnKit.isJson(controller)) {
			controller.setAttr("_localParas", request.getQueryString());
			controller.setAttr("_localUri", inv.getActionKey());
		}

	}

}
