package mytcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

	ServerMessage client;

	public DB(ServerMessage client) {
		this.client = client;
	}

	static int idCheck(String username, String password)
			throws SQLException {

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
			if (password.compareTo(pass) == 0)
				return 1;
			else {
				return 0;
			}
		}
	}

	void blindUpdate(String username, int bigSmall) throws SQLException {

		Connection dbConnection = null;
		java.sql.PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE Players SET Chips = Chips - ? "
				+ " WHERE Username = ?";
		int change = 0;
		if (bigSmall == 0)
			change = 5;
		else if (bigSmall == 1)
			change = 10;

		try {
			dbConnection = getConnection();
			preparedStatement = (PreparedStatement) dbConnection
					.prepareStatement(updateTableSQL);

			preparedStatement.setInt(1, change);
			preparedStatement.setString(2, username);

			// execute update SQL statement
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
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
