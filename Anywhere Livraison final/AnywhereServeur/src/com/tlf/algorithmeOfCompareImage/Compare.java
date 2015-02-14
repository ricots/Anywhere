package com.tlf.algorithmeOfCompareImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;




public class Compare {
	

	public int compare(Mat img1, Mat img2){
		
		 FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
		 DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		 DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

		 //first image
		 //Mat img1 = Highgui.imread("1.jpg");
		 Mat descriptors1 = new Mat();
		 MatOfKeyPoint keypoints1 = new MatOfKeyPoint();

		 detector.detect(img1, keypoints1);
		 descriptor.compute(img1, keypoints1, descriptors1);

		 //second image
		 //Mat img2 = Highgui.imread("2.jpg");
		 Mat descriptors2 = new Mat();
		 MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

		 detector.detect(img2, keypoints2);
		 descriptor.compute(img2, keypoints2, descriptors2);

		 //matcher should include 2 different image's descriptors
		 MatOfDMatch  matches = new MatOfDMatch();        
		 matcher.match(descriptors1,descriptors2,matches);
		 
		 List<DMatch> myList = matches.toList();

		 java.util.Iterator<DMatch> itr = myList.iterator(); 
		 java.util.Iterator<DMatch> itr1 = myList.iterator(); 

		 double max_dist = 0; double min_dist = 100;
		   for( int i = 0; i < descriptors1.rows(); i++ )   {
			  double dist = itr.next().distance;
		      if( dist < min_dist ) min_dist = dist;
		      if( dist > max_dist ) max_dist = dist;
		   }
		   System.out.println("-- Max dist :  \n"+max_dist );
		   System.out.println("-- Min dist :  \n"+ min_dist );
		 
		   DMatch dm;
		   List<DMatch> goodMatch = new ArrayList<DMatch>();
		   for( int i = 0; i < descriptors1.rows(); i++ )   {
			     dm = itr1.next();
			     if(dm.distance <= 40){
			      goodMatch.add(dm);
			      }
			   }
		  //System.out.println(goodMatch.size());
		   return goodMatch.size();
		   
		   
	}
	public double max(double d1, double d2){
		return d1>=d2? d1:d2;
	}
	
	public List<String> getCoordinates(ResultSet rs){
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat orig = Highgui.imread("C:\\Users\\user\\Desktop\\images\\AnywhereDeparture.jpg");
		Mat toComp;
		byte[] b = new byte[10240 * 8];
		
		
		//si match<100, on considère que l'on n'a pas trouvé image similaire 
		int maxMatch = 50;
		List<String> coordinate = new ArrayList<String>();
		try {
			while (rs.next()) {
				System.out.println("Comparing image...");
				InputStream in = rs.getBinaryStream("image");
				in.read(b);
				// File f=new File("D:/"+i+".jpg");
				File f = new File("C:\\Users\\user\\Desktop\\images\\AnywhereImageStoredInDB.jpg");
				FileOutputStream out = new FileOutputStream(f);
				out.write(b, 0, b.length);
				out.close();
				
				toComp = Highgui.imread("C:\\Users\\user\\Desktop\\images\\AnywhereImageStoredInDB.jpg");
				
				int temp = compare(orig, toComp);System.out.println("match value: "+temp);
				if(temp > maxMatch){
					maxMatch = temp;
					coordinate.clear();
					coordinate.add(rs.getString("longitude"));
					coordinate.add(rs.getString("latitude"));
					
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coordinate;

	}
	
	
public List<String> getImageIdAndCoordinates(ResultSet rs){
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat orig = Highgui.imread("C:\\Users\\user\\Desktop\\images\\AnywhereDeparture.jpg");
		Mat toComp;
		byte[] b = new byte[10240 * 8];
		
		
		//si match<100, on considère que l'on n'a pas trouvé image similaire 
		int maxMatch = 50;
		List<String> coordinateID = new ArrayList<String>();
		try {
			while (rs.next()) {
				System.out.println("Comparing image...");
				InputStream in = rs.getBinaryStream("image");
				in.read(b);
				// File f=new File("D:/"+i+".jpg");
				File f = new File("C:\\Users\\user\\Desktop\\images\\AnywhereImageStoredInDB.jpg");
				FileOutputStream out = new FileOutputStream(f);
				out.write(b, 0, b.length);
				out.close();
				
				toComp = Highgui.imread("C:\\Users\\user\\Desktop\\images\\AnywhereImageStoredInDB.jpg");
				
				int temp = compare(orig, toComp);System.out.println("match value: "+temp);
				if(temp > maxMatch){
					maxMatch = temp;
					coordinateID.clear();
					coordinateID.add(rs.getString("longitude"));
					coordinateID.add(rs.getString("latitude"));
					coordinateID.add(rs.getString("ID"));
					
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coordinateID;

	}
}
