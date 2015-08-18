package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_user_info")
public class UserInfo extends BaseModel<UserInfo> {

	private static final long serialVersionUID = 2022392232914610040L;
	
	public static final UserInfo dao = new UserInfo();

}
