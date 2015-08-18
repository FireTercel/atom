package com.ftww.basic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_button")
public class Button extends BaseModel<Button> {
	private static final long serialVersionUID = -1658524522662527499L;
	
	public static final Button dao = new Button();
	
	public static final String sqlId_byRoleButton = "basic.button.byRoleButton";
	
	/**
	 * 根据FunctionId，获得该Function的所有button
	 * @param functionid
	 * @return List<Button>
	 */
	public List<Button> findByFunction(Long functionid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("table", getTableName());
		param.put("column", "functionid");
		return find(getSql(sqlId_select,param), functionid);
	}
	
	/**
	 * 根据Function的IDs，获得某Role的Buttons
	 * @param buttonids
	 * @return List<Button>
	 */
	public List<Button> findByRoleButton(String buttonids){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("param", buttonids);
		return find(getSql(sqlId_byRoleButton,param));
	}

}
