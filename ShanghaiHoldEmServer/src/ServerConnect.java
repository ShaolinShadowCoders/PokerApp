
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
		int players = 1;
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
		
		Login login = new Login();
		for(int i=0;i<players;i++){
			cArray[i] =((Login) login).validateUser(cArray[i]);
		}
		
		//make sure everyone joins the game
		for(int i = 0; i<players;i++)
			cArray[i].status= cArray[i].joinGame(); /*join game is a function where it accepts "ready"*/
					
		for (int i1 = 0; i1 < players; i1++)
			threadPool[i1].join(10);
				
				
		ArrayList<ServerHello> gameplay = new ArrayList<ServerHello>();
		//check if 
		for(int i = 0; i<players;i++)
			if(cArray[i].status == true)
				
				gameplay.add(cArray[i]); //move into a new array for the gameplay
				players = gameplay.size();
				
				
				
				//send message from server to each client in gameplay array to move onto the 
				//next screen with cards 
				
				
				////////////WHILE LOOP FOR GAMEPLAY///////////////////////////////////////////////////
				
					//Determine who's big/little blind
						//int little blind
						//int big blind
					
					
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
		for (int i = 0; i < players; i++) {
			cArray[i].closeConnect();
		}
		serverSocket.close();
	}

  
  
  
}

