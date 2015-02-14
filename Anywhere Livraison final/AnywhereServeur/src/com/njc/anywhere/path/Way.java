package com.njc.anywhere.path;

import java.util.List;

public class Way {
	
	private String id;
	private List<Node> nodeList;

	
	public void setId(String id)
	{
		this.id=id;
	}
	public String getId()
	{
		return id;
	}
	
	public void setNodeList(List<Node> nodeList)
	{
		this.nodeList=nodeList;
	}
	public List<Node> getNodeList()
	{
		return nodeList;
	}

}
