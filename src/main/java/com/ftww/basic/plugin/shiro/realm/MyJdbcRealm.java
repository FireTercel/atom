package com.ftww.basic.plugin.shiro.realm;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.ftww.basic.kits.ValidateKit;
import com.ftww.basic.model.Function;
import com.ftww.basic.model.Role;
import com.ftww.basic.model.User;
import com.ftww.basic.model.UserInfo;
import com.ftww.basic.plugin.shiro.core.SubjectKit;

/**
 * 自定义Realm 认证、授权。
 * @author FireTercel 2015年7月13日 
 *
 */
public class MyJdbcRealm extends AuthorizingRealm {

	/**
	 * 登陆调用认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		//userToken.setRememberMe(true);
		User user = null;
		String username = userToken.getUsername();
		user = User.dao.findFirstBy("`user`.username = ?", username);
		if(!ValidateKit.isNullOrEmpty(user)){
			UserInfo userinfo = user.findUserInfo();
			if(!ValidateKit.isNullOrEmpty(userinfo) && userinfo.getDate("deleted_at") == null) {
				user.put("userinfo", userinfo);
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getStr("password"), getName());
				return info;
			}else{
				return null;
			}
		}else {
			return null;
		}
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName = ((User) principals.fromRealm(getName()).iterator().next()).get("username");
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		//角色集合
		Set<String> roleSet=new LinkedHashSet<String>();
		//操作集合
		Set<String> functionSet=new LinkedHashSet<String>();
		List<Role> roles = null;
		User user = User.dao.findFirstBy("`user`.username = ?", loginName);
		if(!ValidateKit.isNullOrEmpty(user)){
			UserInfo userinfo = user.findUserInfo();
			if(!ValidateKit.isNullOrEmpty(userinfo) && userinfo.getDate("deleted_at") == null) {
				//遍历用户所有角色
				roles = user.findRoles();
			}else {
				SubjectKit.getSubject().logout();
			}
		}else {
			SubjectKit.getSubject().logout();
		}
		loadRole(roleSet, functionSet, roles);
		info.setRoles(roleSet);
		info.setStringPermissions(functionSet);
		return info;
	}
	
	/**
	 * 遍历角色是否可用
	 * @param roleSet
	 * @param functionSet
	 * @param roles
	 */
	private void loadRole(Set<String> roleSet, Set<String> functionSet, List<Role> roles){
		List<Function> functions;
		for(Role role : roles) {
			if(role.getDate("deleted_at") == null) {
				roleSet.add(role.getStr("value"));	//如：R_ADMIN
				functions = role.findFunctions();
				loadAuth(functionSet, functions);
			}
		}
	}
	
	/**
	 * 遍历操作是否可用
	 * @param functionSet
	 * @param functions
	 */
	private void loadAuth(Set<String> functionSet, List<Function> functions) {
		for(Function function : functions) {
			if(function.getDate("deleted_at") == null) {
				functionSet.add(function.getStr("value"));		//如：P_SYSTEM
			}
		}
	}
	
	/**
	 * 更新用户授权信息缓存.
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(Object principal){
		SimplePrincipalCollection principals=new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo(){
		Cache<Object,AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
