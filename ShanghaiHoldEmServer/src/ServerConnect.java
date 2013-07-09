


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;


//ATTEMP TO CHANGE
public class ServerConnect {
  public static void main(String[] args) throws IOException,
			InterruptedException {
		ServerSocket serverSocket = null;
		//This is how we could change files 
		try {
			serverSocket = new ServerSocket(20001);
		} catch (IOException e) {
			System.err.println(e.getMessage());

			System.exit(-1);
		}
		int players = 2;
		ServerHello[] cArray = new ServerHello[players];
		Thread[] threadPool = new Thread[players];

		for (int i = 0; i < players; i++) {
			Socket clientServer = serverSocket.accept();
			System.out.println("Connection number " + (i + 1)
					+ " is connected on port 20001.");
			cArray[i] = new ServerHello(clientServer);
			threadPool[i] = new Thread(cArray[i]);
			threadPool[i].start();

		}
		
		
		for (int i = 0; i < players; i++)
			threadPool[i].join();
		
		//code goes here
		
		//login
		for(int i=0;i<players;i++){
			String usernameString = cArray[i].getString();
			String password = cArray[i].getString();
		}
		
		//While loop with function call to "checkUserName/Pass"
			boolean validUser = false;
			while(validUser == false){
				//check database
				
					//Select password where username = ? 
			
			//or create into database
			
			//if successful, end loop
			
			//Create error (try catch) 
				//send error message back to try again
		//end loop for unsuccessful password
			
		//send message sending client to the next screen
		//client[i].username = username
		//client[i].password = password
		
			
			
			
		//make sure everyone joins the game
		//for(int i = 0; i<players;i++){
			//client[i].joinGame() /*join game is a function where it accepts "ready"
		//}
		
		//for (int i = 0; i < players; i++)
		//	threadPool[i].join();
		
		//check the returned message for "ready" or "not ready" for each player
		//move into a new array for the gameplay
		
		//send message from server to each client in gameplay array to move onto the 
		//next screen
		
		////////////WHILE LOOP FOR GAMEPLAY///////////////////////////////////////////////////
		
			//Determine who's big/little blind
				//int little blind
				//int big blind
			
			//Random rand = new Random(); /*random card generator*/
			
			//initialize everyone's cards and send them out to the respective players
				//array list of integers 0-51 to compose the deck//
			ArrayList<Integer> deck = new ArrayList<Integer>();
				for(int i1=0; i1<52 ; i1++)
						deck.add(i1);
			
				////Give everyone their cards/////
					//int temp1; temp2;
					//String[] cardsStrings = new String[gameplayers];
					//for(i = 0; i<gamePlayers ; i ++){
							// temp1= rand.nextInt() % deck.length;
							//deck.remove(temp1);
							// temp2= rand.nextInt() % deck.length;
							//deck.remove(temp2);
							//String cards[i] = "" + temp1 + " " + temp2;
						//}
				////betting////
				////Send initial flop////
				////betting////
				
				

		}
		
		
		System.out.printf("Blah blah blah\n");
		String inputString = cArray[0].getString();
		System.out.println(inputString);
		cArray[1].sendString(inputString);
		inputString = cArray[1].getString();
		System.out.println(inputString);
		cArray[0].sendString(inputString);
		
		for (int i = 0; i < players; i++) {
			cArray[i].closeConnect();
		}
		serverSocket.close();
	}
  
  
  
  public static String idCheck(String username) {
		try {
			int name2;

			Connection conn = getConnection();
			String dpProcedure = "drop procedure if exists show_movies";

			Statement st = conn.createStatement();
			st.execute(dpProcedure);

			String otProcedure = "create procedure idCheck() begin select chips from players where id="
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

