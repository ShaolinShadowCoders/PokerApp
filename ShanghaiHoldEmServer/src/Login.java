import java.io.IOException;
import java.sql.SQLException;


public class Login {
	
	public Login(){
		
	}
	
	/*public ServerHello validateUser(ServerHello client) throws NumberFormatException, IOException{
		
		
	}*/

	public ServerHello validateUser(ServerHello client) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		
		boolean validUser = false;
		while(validUser == false){
		
		 client.username = client.getString();
		 client.password = client.getString();
	
		
		//String inputPass = "abc456";
		//String user = "Brian";
		
		
		DB clientdataDb = new DB(client);
		String dbPass = null;
		try {
			dbPass = clientdataDb.idCheck(client.username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		if (client.password.compareTo(dbPass) == 0){
			client.sendString("Valid");
			validUser = true;
		}
		else {
			client.sendString("Invalid");
			validUser = false;
		}
	}
	System.out.println("Worked!\n");
		
		return client;
		
	}
	
	
}
