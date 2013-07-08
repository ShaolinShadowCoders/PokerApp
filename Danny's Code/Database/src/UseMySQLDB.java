import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UseMySQLDB {

	private UseMySQLDB() {

		// TODO Auto-generated constructor stub
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/testpoker", "root", "");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return con;
	}

}