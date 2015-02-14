package AnywhereServicePublisher;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

import com.tlf.algorithmeOfCompareImage.Compare;
import com.tlf.anywhereservice.imp.TraitementImgWs;
import com.tlf.anywhereservice.inf.TraitementImg;

import database.DBConnector;
import database.DBOperator;

public class AnywherePublisher {
	//L'adresse pour déposer des webservice
	//public static final String URI = "http://10.212.124.254:8080/hello";
	
	//public static final String URI = "http://172.20.10.6:8080/hello";
	//public static final String URI = "http://192.168.1.31:8080/hello";
	public static final String URI = "http://192.168.43.111:8080/hello";		
	public static void main(String[] args){
		TraitementImgWs imp = new TraitementImgWs();
		Endpoint endpoint = Endpoint.publish(URI, imp);
		
//		DBOperator db = new DBOperator();
//		db.uploadPlanFromLocal("O1.jpg", "O1","7.0711897", "43.6159137", "7.0724746", "43.6159137","7.0724746", "43.6151736","7.0711897","43.6151736",1);
		//imp.getIndoorPlan(null);
	}
}
 