package com.tlf.anywhereservice.inf;

import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.*;

@WebService
public interface TraitementImg {
	public String ditBonjour(String nameOfUser);
	
	public String uploadImage(String image,String lon, String lat,String name, String region);
	public String downloadImage(String lon, String lat, String region);
	
	public String getCoordinate(String image, String region);
}
