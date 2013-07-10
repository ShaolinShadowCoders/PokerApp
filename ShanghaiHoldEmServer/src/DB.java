import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


public class DB {
	
	ServerHello client;
	public DB(ServerHello client){
		this.client = client;
	}
	String idCheck(String username) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String pass = null;
		String selectSQL = "SELECT Password FROM Players WHERE Username = ?";

		try {
			dbConnection = getConnection();
			preparedStatement = (PreparedStatement) dbConnection
					.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);

			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				pass = rs.getString("Password");
				System.out.println("pass in DB : " + pass);

			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
			return pass;
		}
	}

public static Connection getConnection() {
	Connection con = null;
	try {
		Class.forName("com.mysql.jdbc.Driver");
		con = (Connection) DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/testpoker", "root", "");
	} catch (SQLException ex) {
		ex.printStackTrace();
	} catch (ClassNotFoundException ex) {
		ex.printStackTrace();
	}
	return con;
}






}
