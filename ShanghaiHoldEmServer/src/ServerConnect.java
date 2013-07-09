
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

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
					
					boolean validUser = false;
					while(validUser == false){
					
					String usernameString = cArray[i].getString();
					String password = cArray[i].getString();
				
					
					//String inputPass = "abc456";
					//String user = "Brian";
					String dbPass = null;
					try {
						dbPass = idCheck(usernameString);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
					if (password.compareTo(dbPass) == 0){
						cArray[i].sendString("Valid");
						validUser = true;
					}
					else {
						cArray[i].sendString("Invalid");
						validUser = false;
					}
				}
				System.out.println("Worked!\n");
			}
				
				
					
					
					
				//make sure everyone joins the game
				//for(int i = 0; i<players;i++)
					 //cArray[i].joinGame() /*join game is a function where it accepts "ready"*/
				
				
				//for (int i1 = 0; i1 < players; i1++)
					//threadPool[i1].join(20);
				
				
				//move into a new array for the gameplay
				
				
				//check the returned message for "ready" or "not ready" for each player
				//for (int i = 0; i < players; i++)
					//if(cArray[i].status == true){
						
			//		}
				
				//send message from server to each client in gameplay array to move onto the 
				//next screen
				
				////////////WHILE LOOP FOR GAMEPLAY///////////////////////////////////////////////////
				
					//Determine who's big/little blind
						//int little blind
						//int big blind
					
					//Random rand = new Random(); /*random card generator*/
					
					//initialize everyone's cards and send them out to the respective players
						//array list of integers 0-51 to compose the deck//
					//ArrayList<Integer> deck = new ArrayList<Integer>();
						//for(int i1=0; i1<52 ; i1++)
								//deck.add(i1);
					
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

		/*System.out.printf("Blah blah blah\n");
		String inputString = cArray[0].getString();
		System.out.println(inputString);
		cArray[1].sendString(inputString);
		inputString = cArray[1].getString();
		System.out.println(inputString);
		cArray[0].sendString(inputString);
		/*inputString = cArray[0].getString();
		System.out.println(inputString);
		cArray[1].sendString(inputString);
		inputString = cArray[1].getString();
		System.out.println(inputString);
		cArray[0].sendString(inputString);*/

		for (int i = 0; i < players; i++) {
			cArray[i].closeConnect();
		}
		serverSocket.close();
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
		ex.printStackTrace();
	} catch (ClassNotFoundException ex) {
		ex.printStackTrace();
	}
	return con;
}
}

