package com.ftww.basic.service;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftww.basic.model.User;
import com.ftww.basic.model.UserInfo;
import com.ftww.basic.plugin.shiro.core.SubjectKit;
import com.ftww.basic.plugin.shiro.hasher.HasherInfo;
import com.ftww.basic.plugin.shiro.hasher.HasherKit;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.tx.Tx;

public class UserService extends BaseService {
	
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	
	public static final UserService service = Enhancer.enhance(UserService.class, Tx.class);
	
	public void save(User user, String password, UserInfo userInfo){
		log.info("新建用户："+user.getStr("username"));
		//加密 hasher 盐
		HasherInfo hasher = HasherKit.hash("123456");
		user.set("password", hasher.getHashResult());
		user.set("salt", hasher.getSalt());
		user.set("hasher", hasher.getHasher().value());
		
		//保存用户信息
		userInfo.set("created_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		userInfo.set("created_userid", SubjectKit.getUser().getPKValueLong());
		userInfo.save();
		
		user.set("userinfoid", userInfo.getPKValueLong());
		user.set("logincount", 0);
		user.save();
		
	}

}
