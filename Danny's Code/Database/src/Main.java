import java.io.DataInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class Main {
	public static void main(String[] args) throws IOException {
		Connection con = getConnection();
		String i;
		DataInputStream in = new DataInputStream(System.in);

		System.out.println("Enter player you want total for:");
		i = in.readLine();
		int foo = Integer.parseInt(i);

		getTotal(foo);
		makeTable();

	}

	public static void makeTable() {
		try {

			Connection conn = getConnection();
			/*
			 * String dpProcedure = "drop procedure if exists make_table";
			 * 
			 * Statement st = conn.createStatement(); st.execute(dpProcedure);
			 */
			String otProcedure = "create procedure make_table() begin CREATE TABLE playerss(id MEDIUMINT AUT0_INCREMENT, chips MEDIUMINT, PRIMARY KEY (id)); end";

			Statement st2 = conn.createStatement();
			st2.execute(otProcedure);

			CallableStatement cs = conn.prepareCall("{call make_table()}");

			ResultSet rs = null;
			// rs = cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void getTotal(int id) {
		try {
			int name2;

			Connection conn = getConnection();
			String dpProcedure = "drop procedure if exists show_movies";

			Statement st = conn.createStatement();
			st.execute(dpProcedure);

			String otProcedure = "create procedure show_movies() begin select chips from players where id="
					+ id + "; end";

			Statement st2 = conn.createStatement();
			st2.execute(otProcedure);

			CallableStatement cs = conn.prepareCall("{call show_movies()}");

			ResultSet rs = cs.executeQuery();

			while (rs.next()) {

				name2 = rs.getInt(1);
				System.out.println(name2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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