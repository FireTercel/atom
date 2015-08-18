package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_company")
public class Company extends BaseModel<Company> {

	private static final long serialVersionUID = -5038352905662993783L;
	
	public static final Company dao = new Company();
	
	/**
	 * 查询用户职位信息
	 * @return
	 */
	public Position findPosition(){
		return Position.dao.findById(getLong("positionid"));
	}

}
