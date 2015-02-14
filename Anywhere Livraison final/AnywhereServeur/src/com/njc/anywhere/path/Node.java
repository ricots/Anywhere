package com.njc.anywhere.path;

public class Node {

	private String id;
	private double lat;
	private double lon;
	
	
	
	public void setId(String id)
	{
		this.id=id;
	}
	public void setLat(double lat)
	{
		this.lat=lat;
	}
	public void setLon(double lon)
	{
		this.lon=lon;
	}
	
	public double getLat()
	{
		return lat;
	}
	
	public double getLon()
	{
		return lon;
	}
	public String getId()
	{
		return id;
	}
}
