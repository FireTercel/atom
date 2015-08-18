package com.ftww.basic.model;

import java.util.List;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.kits.ValidateKit;
import com.ftww.basic.model.treeNode.TreeNode;

/**
 * 功能
 * @author FireTercel 2015年7月28日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_function")
public class Function extends BaseModel<Function> implements TreeNode<Function> {

	private static final long serialVersionUID = -2828220582173514897L;
	
	public static final Function dao = new Function();
	
	public static final String sqlId_byRole = "basic.function.byRole";

	@Override
	public Long getId() {
		return this.getLong("id");
	}

	@Override
	public Long getParentId() {
		return this.getLong("pid");
	}

	@Override
	public List<Function> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Function> children) {
		this.put("children", children);
	}
	
	/**
	 * 查询该Function的所有Buttons
	 * @return
	 */
	public List<Button> findButtons(){
		return Button.dao.findByFunction(getPKValueLong());
	}
	
	/**
	 * 根据roleid，查询指定role对应function可以有哪些button。
	 * @param roleid
	 * @return List<Button>
	 */
	public List<Button> findButtonsByRoleId(Long roleid){
		RoleButton rb = RoleButton.dao.findFirst(getSql(RoleButton.sqlId_byRoleFunction),roleid,getPKValueLong());
		if( ! ValidateKit.isNullOrEmpty(rb)){
			String buttonids = rb.getStr("buttonids");
			return Button.dao.findByRoleButton(buttonids);
		}else{
			return null;
		}
		
	}

}
