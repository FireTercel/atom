package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_role_button")
public class RoleButton extends BaseModel<RoleButton> {

	private static final long serialVersionUID = -1427471391089502207L;
	
	public static final RoleButton dao = new RoleButton();
	
	public static final String sqlId_byRoleFunction = "basic.roleButton.byRoleFunction";
	
	

}
