package com.ftww.basic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.kits.ValidateKit;
import com.ftww.basic.model.treeNode.TreeNode;
import com.ftww.basic.plugin.ParamInitPlugin;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 角色
 * @author FireTercel 2015年7月28日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_role")
public class Role extends BaseModel<Role> implements TreeNode<Role>{

	private static final long serialVersionUID = 649615534144391273L;
	
	public static final Role dao = new Role();

	@Override
	public Long getId() {
		return this.getLong("id");
	}

	@Override
	public Long getParentId() {
		return this.getLong("pid");
	}

	@Override
	public List<Role> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Role> children) {
		this.put("children", children);
	}
	
	/**
	 * 获得角色的操作权限。
	 * @return
	 */
	public List<Function> findFunctions(){
		String functions = getStr("functionids");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("param", functions);
		return Function.dao.find(getSql(Function.sqlId_byRole, param));
	}
	
	/**
	 * 根据functionid，查询指定function对应role有哪些button。
	 * @param functionid
	 * @return
	 */
	public List<Button>findButtonsByFunctionId(Long functionid){
		RoleButton rb = RoleButton.dao.findFirst(getSql(RoleButton.sqlId_byRoleFunction), getPKValueLong(), functionid);
		if( ! ValidateKit.isNullOrEmpty(rb)){
			String buttonids = rb.getStr("buttonids");
			return Button.dao.findByRoleButton(buttonids);
		}else {
			return null;
		}
	}
	
	/**
	 * 查询角色所有用户
	 * @return
	 */
	public List<User> findUsers(){
		List<RoleUser> lists = RoleUser.dao.findByRoleId(getPKValueLong());
		List<User> userList = new ArrayList<User>();
		for(RoleUser list : lists){
			Long userid = list.getLong("userid");
			userList.add(User.dao.findById(userid));
		}
		return userList;
	}
	
	/**
	 * 获得子角色集
	 * @return
	 */
	public List<Role> findChildRoles(){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", Role.dao.getTableName());
		param.put("column", "pid");
		return find(getSql(sqlId_select, param), this.getPKValueLong());
	}
	
	/**
	 * 添加或更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		CacheKit.put(DictKeys.cache_name_system, ParamInitPlugin.cacheStart_role + id, Role.dao.findById(id));
	}
	
	/**
	 * 删除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		CacheKit.remove(DictKeys.cache_name_system, ParamInitPlugin.cacheStart_role + id);
	}
	
	/**
	 * 获取缓存
	 * @param id
	 * @return
	 */
	public Role cacheGet(Long id){
		Role role = CacheKit.get(DictKeys.cache_name_system, ParamInitPlugin.cacheStart_role + id);
		return role;
	}

}
