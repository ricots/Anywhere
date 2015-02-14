package database;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class DBOperator {
	// connector de base de donnée
	DBConnector dbcon;
	Connection conn;

	public DBOperator() {
		dbcon = new DBConnector();
		conn = dbcon.connecter();

	}

	public void upload(byte[] buffer, String lon, String lat, String name ,String region) {
		try {
			String sql = "insert into "+region+" values(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			/*
			 * File f =new File("C:/Users/user/Desktop/1.jpg"); FileInputStream
			 * input= new FileInputStream(f);
			 */
			System.out.println("Region: "+ region);
			
			System.out.println("lon: "+ lon);
			System.out.println("lat: "+ lat);
			System.out.println("name: "+ name);


			ByteArrayInputStream input = new ByteArrayInputStream(buffer);
			
			ps.setString(1, name);
			ps.setBinaryStream(2, input);

			ps.setString(3, lon);
			ps.setString(4, lat);
			
			
			ps.executeUpdate();
			ps.close();
			input.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	
	
	public void uploadImageFromLocal(String lon, String lat, String name, String region, String fileName) {
		try {
			String sql = "insert into "+region+" values(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			//PreparedStatement ps = conn.prepareStatement("insert into ? values(?,?,?,?)");
	
			
			  File f =new File("C:/Users/user/Desktop/"+fileName); 
			  FileInputStream input= new FileInputStream(f);
			 

			//ByteArrayInputStream input = new ByteArrayInputStream(buffer);
			ps.setString(1, name);
			ps.setBinaryStream(2, input);

			ps.setString(3, lon);
			ps.setString(4, lat);

			ps.executeUpdate();
			ps.close();
			input.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	
	public void uploadPlanFromLocal(String fileName, String ID, String alat,String alon, String blat,String blon, String clat,String clon, String dlat,String dlon, int floor) {
		try {
			String sql = "insert into PolytechNiceSophiaPlan values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			//PreparedStatement ps = conn.prepareStatement("insert into ? values(?,?,?,?)");
	
			
			  File f =new File("C:/Users/user/Desktop/images/"+fileName); 
			  FileInputStream input= new FileInputStream(f);
			 

			//ByteArrayInputStream input = new ByteArrayInputStream(buffer);
			
			ps.setString(1, ID);
			ps.setString(2, alat);
			ps.setString(3, alon);
			ps.setString(4, blat);
			ps.setString(5, blon);
			ps.setString(6, clat);
			ps.setString(7, clon);
			ps.setString(8, dlat);
			ps.setString(9, dlon);
			
			ps.setBinaryStream(10, input);
			ps.setInt(11, floor);

			ps.executeUpdate();
			ps.close();
			input.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	
	

	public boolean getImage(String lon, String lat, String region) {
		boolean modified = false;
		byte[] image = null;
		try {
			// String sql="select * from Images where long";
			//String sql = "select * from Images where longitude = '43.615' and latitude = '7.072'";
			//PreparedStatement ps = conn.prepareStatement(sql);
			
			System.out.println(lon+" "+lat);
			//PreparedStatement ps = conn.prepareStatement("select * from "+region +" where longitude = ? and latitude = ?");
			PreparedStatement ps = conn.prepareStatement("select * from "+region +" where longitude like ? and latitude like ?");
			ps.setString(1, "%" + lon + "%");
			ps.setString(2, "%" + lat + "%");
		
			// ps.setString(1, "cute");
			ResultSet rs = ps.executeQuery();
			byte[] b = new byte[10240 * 8];

			int i = 0;
			
			
			while (rs.next()) {
				InputStream in = rs.getBinaryStream("image");
				in.read(b);
				// File f=new File("D:/"+i+".jpg");
				File f = new File("C:\\Users\\user\\Desktop\\images\\temp.jpg");
				
				if(f.exists()){
					f.delete();
				}
						
				FileOutputStream out = new FileOutputStream(f);
				out.write(b, 0, b.length);
				out.close();
				
				modified = true;
				
			}
			// String
			// sql="select * from Images where longitude = "+lon+"and latitude = "+"lat";
			/*
			 * String sql=
			 * "select * from Images where longitude = '1.2345' and latitude = '2.9876'"
			 * ; PreparedStatement ps=conn.prepareStatement(sql); ResultSet
			 * rs=ps.executeQuery(); image = new byte[10240*8]; InputStream
			 * in=rs.getBinaryStream("image"); in.read(image);
			 */
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return modified;
	}
	
	
	public List<String> getPlanCoordinates(String id) {

		List<String> coordinates = new ArrayList<String>();
		byte[] image = null;
		try {
			
			PreparedStatement ps = conn.prepareStatement("select * from PolytechNiceSophiaPlan where ID = ?");
			ps.setString(1, id);
		
			// ps.setString(1, "cute");
			ResultSet rs = ps.executeQuery();
			byte[] b = new byte[10240 * 8];

			int i = 0;

			while (rs.next()) {
				InputStream in = rs.getBinaryStream("image");
				in.read(b);
				File f = new File("C:\\Users\\user\\Desktop\\images\\Plan.jpg");
				
				if(f.exists()){
					f.delete();
				}
						
				FileOutputStream out = new FileOutputStream(f);
				out.write(b, 0, b.length);
				out.close();
				
				coordinates.add(rs.getString("Alat"));
				coordinates.add(rs.getString("Alon"));
				coordinates.add(rs.getString("Blat"));
				coordinates.add(rs.getString("Blon"));
				coordinates.add(rs.getString("Clat"));
				coordinates.add(rs.getString("Clon"));
				coordinates.add(rs.getString("Dlat"));
				coordinates.add(rs.getString("Dlon"));
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return coordinates;
	}
	
	
	
	
	public ResultSet getAllImgs(String region) {
		String sql = "select * from "+region;
		ResultSet rs = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		
	}
}
