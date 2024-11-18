package connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {
	public static Connection getConnection() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/amazoninventoryproject", "root", "root");
		System.out.println("Connection build successfully");
		
		return con;
	}
}

