package com.tlf.anywhereservice.imp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.codec.binary.Base64;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import com.njc.anywhere.path.Node;
import com.njc.anywhere.path.Path;
import com.tlf.algorithmeOfCompareImage.Compare;
import com.tlf.anywhere.drawImage.DrawImage;
import com.tlf.anywhereservice.inf.TraitementImg;

import database.DBOperator;

@WebService
public class TraitementImgWs implements TraitementImg {

	private DBOperator dbope;
	private Compare compare;
	private DrawImage dimage;
	
	public TraitementImgWs(){
		dbope = new DBOperator();
		compare = new Compare();
		dimage = new DrawImage();
	}
	
	@Override
	public String ditBonjour(String nameOfUser) {
		// TODO Auto-generated method stub
		return "Hello " + nameOfUser;
	}

	public String uploadImage(String image,String lon, String lat,String name, String region) {
		System.out.println("upload image");
		FileOutputStream fos = null;
		try {
			byte[] buffer = new BASE64Decoder().decodeBuffer(image); 
			
			dbope.upload(buffer,lon,lat,name, region);
			return "upload succeed!" ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "upload failure！";
	}

	public String downloadImage(String lon, String lat, String region) {
		System.out.println("download image");
		boolean modified = dbope.getImage(lon,lat, region);
		String srcUrl = "C:\\Users\\user\\Desktop\\images\\"; 
		String fileName = "temp.jpg"; 
		String uploadBuffer = null;
		try {
			FileInputStream fis = new FileInputStream(srcUrl + fileName);
			
			if(!modified){
				fis = new FileInputStream(srcUrl + "notFind.jpg");
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			uploadBuffer = new String(Base64.encodeBase64(baos.toByteArray()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return uploadBuffer;
	}
	
	public String getCoordinate(String image, String region){
		//long startTime=System.currentTimeMillis();
		long endTime;
		List<String> coordinate = new ArrayList<String>();
		String cor = null;
		try {
			byte[] buffer = new BASE64Decoder().decodeBuffer(image);
			FileOutputStream fos = null;
			String toDir = "C:\\Users\\user\\Desktop\\images\\";
			File destDir = new File(toDir);
			if (!destDir.exists())
				destDir.mkdir();
			fos = new FileOutputStream(new File(destDir, "AnywhereDeparture.jpg")); 
			fos.write(buffer);
			fos.flush();
			fos.close();
		
			System.out.println("getCoordinate");
		
			ResultSet rs = dbope.getAllImgs(region);
			
			coordinate = compare.getCoordinates(rs);
			
			//endTime=System.currentTimeMillis();
			//System.out.println("Run time of getCoordinate "+(endTime-startTime)+"ms"); 
			
			
			if(coordinate.size()>0){
				cor = coordinate.get(0)+","+coordinate.get(1);
			}else{
				return "-1,-1";
			}
 		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cor;
	}
	
	
	
	public String getIndoorPlan(String image){
		List<String> coordinateOfPosition = new ArrayList<String>();
		String cor = null;
		String uploadBuffer = null;
		try {
			byte[] buffer = new BASE64Decoder().decodeBuffer(image);
			FileOutputStream fos = null;
			String toDir = "C:\\Users\\user\\Desktop\\images\\";
			File destDir = new File(toDir);
			if (!destDir.exists())
				destDir.mkdir();
			fos = new FileOutputStream(new File(destDir, "AnywhereDeparture.jpg")); 
			fos.write(buffer);
			fos.flush();
			fos.close();
		
			System.out.println("getIndoorPlan");
			
			ResultSet rs = dbope.getAllImgs("PolytechNiceSophiaIndoor");
			coordinateOfPosition = compare.getImageIdAndCoordinates(rs);
			
			
//			coordinateOfPosition.add("43.6156961");   //lontitude
//			coordinateOfPosition.add("7.0721658");    //latitude
//			coordinateOfPosition.add("O1");
			
			if(coordinateOfPosition.size()>0){
				
				
				/*Node departure = new Node();
				departure.setLon(43.6156457);
				departure.setLat(7.0720535);
				
				Node destination = new Node();
				destination.setLon(43.6154735);
				destination.setLat(7.0717771);
				
				Path path = new Path();
				List<Node> nodes = path.getWay(7.0720535, 43.6156457, 7.0717771, 43.6154735);
				
				System.out.println(nodes.size());*/
				

				
				List<String> coordinateOfPlan = dbope.getPlanCoordinates(coordinateOfPosition.get(2));
				dimage.draw(coordinateOfPosition, coordinateOfPlan, coordinateOfPosition.get(2));	
				
				FileInputStream fis = new FileInputStream("C:\\Users\\user\\Desktop\\images\\newPlan.jpg");
				
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer1 = new byte[1024];
				int count = 0;
				while ((count = fis.read(buffer1)) >= 0) {
					baos.write(buffer1, 0, count);
				}
				uploadBuffer = new String(Base64.encodeBase64(baos.toByteArray()));
				
			}else{
				System.out.println("No image similaire stored in database");
				return "0";
			}
 		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uploadBuffer;
	}

}
