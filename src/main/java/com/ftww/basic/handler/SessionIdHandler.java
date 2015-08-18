package com.ftww.basic.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jfinal.handler.Handler;

/**
 * 对于没有cookie的时候会传递url会带上sessionId导致action跳入404
 * @author FireTercel 2015年8月4日 
 *<url>/sign_in;jsessionid=7ba49c313a84295770fecbd01e86f116166sc5feg5yhzwis9zayzx492</url>
 *最后发现，调用一下request.getSession();就没有jsessionid了
 */
public class SessionIdHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		//boolean isFromURL = request.isRequestedSessionIdFromURL();
		HttpSession session = request.getSession();  
		/*if (isFromURL) {
			target = target.substring(0, target.indexOf(';'));
		}*/
		nextHandler.handle(target, request, response, isHandled);

	}

}
