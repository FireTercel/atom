package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_position")
public class Position extends BaseModel<Position> {

	private static final long serialVersionUID = -2266312532402259294L;
	
	public static final Position dao = new Position();

}
