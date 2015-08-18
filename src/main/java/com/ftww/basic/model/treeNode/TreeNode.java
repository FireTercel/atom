package com.ftww.basic.model.treeNode;

import java.util.List;

/**
 * Created by wangrenhui on 14-4-12.
 */
public interface TreeNode<T> {
	
	public Long getId();
	
	public Long getParentId();
	
	public List<T> getChildren();
	
	public void setChildren(List<T> children);

}
