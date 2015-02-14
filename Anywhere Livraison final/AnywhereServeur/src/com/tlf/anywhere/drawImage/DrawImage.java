package com.tlf.anywhere.drawImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class DrawImage {
	
	public void draw(List<String>coordinateOfPosition, List<String>coordinateOfPlan, String position){
		BufferedImage buffer = null;
		try {
			buffer=ImageIO.read(new File("C:\\Users\\user\\Desktop\\images\\Plan.jpg"));
	        Graphics g=buffer.getGraphics();
	        g.setColor(Color.orange);

	        
	        double copo[] = {0,0};//0.lon 1.lat
	        for(int i = 0; i<2;i++){
	        	copo[i] = Double.parseDouble(coordinateOfPosition.get(i)); 
	        	//System.out.println(copo[i]);
	        }
	        double copl[] = {0,0,0,0,0,0,0,0};
	        for(int i = 0; i<8;i++){
	        	copl[i] = Double.parseDouble(coordinateOfPlan.get(i)); 
	        	//System.out.println(copl[i]);
	        }
	        int x = (int) (((copo[1]-copl[0])/(copl[2]-copl[0]))*buffer.getWidth());
	        int y = (int) (((copl[1]-copo[0])/(copl[1]-copl[7]))*buffer.getHeight());
	        //System.out.println("x = "+x+" y = "+y);
	        
	        
	        for(int i=1; i < 15;i++){
	        	g.drawOval(x, y, i, i);
	        }
	        g.drawString("Plan "+position, 10,10);
	        g.drawString("Vous etes ici", x,y);
	        
	        
	        
	        FileOutputStream fos=new FileOutputStream("C:\\Users\\user\\Desktop\\images\\newPlan.jpg");  
	        ImageIO.write(buffer, "jpg", fos);
	        fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
