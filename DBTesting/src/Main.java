import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Main {

	public static void main(String[] args) {
		String inputPass = "abc456";
		String user = "Brian";
		String dbPass = null;
		try {
			dbPass = idCheck(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
//		if (inputPass.compareTo(dbPass) == 0)
			System.out.println("It actually worked.");
	//	else {
		//	System.out.println("Password does not match");
		//}

	}

	private static String idCheck(String username) throws SQLException {

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
					"jdbc:mysql://localhost:3306/testpoker", "root", "123");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		return con;
	}
}
