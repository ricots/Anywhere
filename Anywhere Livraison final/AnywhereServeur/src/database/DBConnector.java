package database;

import java.sql.*;

public class DBConnector {
	
	public Connection connecter() {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=Anywhere";
		String userName = "tao";
		String userPwd = "tao88601501";
		Connection dbConn = null;
		try {
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName,
					userPwd);
			System.out.println("connection succès");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("connection failure");
		}
		return dbConn;

	}
}
