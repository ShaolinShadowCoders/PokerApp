package mytcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class NioServer {

	/**
	 * @param args
	 */
	private int port = 20001;
	final makeFlagFalse timerFlag = new makeFlagFalse();
	static makeFlagFalse loginLoopBoolean = new makeFlagFalse();
	private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
	private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
	private static Map<SocketChannel, Integer> clientsMap = new HashMap<SocketChannel, Integer>();
	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;
	private Object gate = new Object();
	private ArrayList<Player> logins = new ArrayList<Player>();
	private int tempBet, pot, move,counter=0,gameSize;
	private String user;
	Player[] gamePlayers;

	public NioServer(int port) {
		this.port = port;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		// serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("·þÎñÆ÷Æô¶¯");
	}

	public void accept() {
		while (true) {
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				System.out.println("receive the connection from "
						+ socketChannel.socket().getInetAddress() + ":"
						+ socketChannel.socket().getPort());

				socketChannel.configureBlocking(false);
				synchronized (gate) {
					selector.wakeup();
					socketChannel.register(selector, SelectionKey.OP_READ);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}

	public void messageService() throws InterruptedException {

		synchronized (gate) {
		}
		try {
			int n = selector.select();
			if (n == 0) {
			} else {
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				for (SelectionKey key : selectionKeys) {
					try {
						if (key.isReadable()) {
							receive_message(key);
						}
					} catch (Exception e) {
						try {
							if (key != null) {
								key.cancel();
								key.channel().close();
							}
						} catch (Exception ex) {
							e.printStackTrace();
						}
					}
				}
				selectionKeys.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void receive_message(SelectionKey key) {
		SocketChannel socketChannel = null;
		ServerMessage message = null;
		ServerMessage sendMessage = new ServerMessage();
		socketChannel = (SocketChannel) key.channel();
		rBuffer.clear();
		try {
			int count = socketChannel.read(rBuffer);
			if (count > 0) {
				rBuffer.flip();
				message = ServerMessage.byte2Message(rBuffer.array());
				System.out.println("Receive from"
						+ socketChannel.socket().getInetAddress() + " : "
						+ message.getb());
				switch (message.getb()) {

				case (1): /* login */
					int valid = DB.idCheck(message.getUsername(),
							message.getPassword());
					if (valid == 1) {
						logins.add(new Player(message.getUsername(), message
								.getPassword(), socketChannel));
					}
					sendMessage.setb((byte) 2);
					sendMessage.setValid(valid);
					send(sendMessage, socketChannel);
					break;
				case (3): /* ready screen */
					if (timerFlag.flag) {
						if (message.getReady() == 1) {
							if (clientsMap.size() < 6) {
								clientsMap.put(socketChannel,
										clientsMap.size() + 1);
								sendMessage.setb((byte) 3);
								sendMessage.setReady(1);
								send(sendMessage, socketChannel);

							} else {
								sendMessage.setb((byte) 3);
								sendMessage.setReady(0);
								send(sendMessage, socketChannel);
							}
						} else {
							sendMessage.setb((byte) 3);
							sendMessage.setReady(0);
							send(sendMessage, socketChannel);
						}
						Timer timer = new Timer();

						System.out.println("flag is " + timerFlag.flag);
						TimerTask task = new TimerTask() {
							public void run() {
								timerFlag.falsify();
								System.out.println("flag now is "
										+ timerFlag.flag);
							}
						};
						timer.schedule(task, 5 * 1000);

					} else {
						sendMessage.setb((byte) -1); /*-1 implies that game is currently in progress*/
						send(sendMessage, socketChannel);
					}
					break;

				case (4):/* dealing out cards */
					if (timerFlag.flag == true) {
						sendMessage.setb((byte) -2);/*message to wait for other players*/
						send(sendMessage, socketChannel);
					} else {
						loginLoopBoolean.falsify();
					}
					break;
				case (5): /* handling a bet */
					tempBet = message.getMinBet();
					move = message.getMove();
					user = message.getUsername();
					break;
				case (6): /*determining who won the game*/ 
					if(counter == (gamePlayers.length - 1)){
						set_hand_credentials(sendMessage, socketChannel);
						ArrayList<Player> winners = determine_winner(gamePlayers);
						sendMessage.setb((byte)7);
						if(winners.size() == 1){
							Player winner = winners.get(0);
							sendMessage.setUsername(winner.userName);
							sendMessage.setScore(winner.score);
							sendMessage.setHand(winner.hand);/*highest card in winning hand*/
							sendMessage.setHigh(winner.high);/*highest card in winner's hand*/
							broadcast_message_all(sendMessage);
						}else{ 
							broadcast_message_all(sendMessage,winners);
						}
						
					}else{
						set_hand_credentials(message,socketChannel);
						counter++;
					}
					
					
				}
			}/* end of if(count=0) */
		} catch (Exception e) {
			e.printStackTrace();
			key.cancel();
			try {
				socketChannel.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void gamePlay(Map<SocketChannel, Integer> clientsMap)
			throws IOException, InterruptedException {
		
		Dealer dealer = new Dealer(); /*create the dealer and the deck*/
		dealer.createDeck();
		
		ServerMessage sendMessage = new ServerMessage(); /*Initialize new message*/
		
		if (!clientsMap.isEmpty()) {
			Set<SocketChannel> clientSet = clientsMap.keySet();
			gameSize = clientSet.size();
			SocketChannel[] players = new SocketChannel[gameSize];
			clientSet.toArray(players);
			gamePlayers = new Player[gameSize];

			// transfer clients to a fixed 'Player' array
			for (int i1 = 0; i1 < gameSize; i1++) {
				for (int j = 0; j < logins.size(); j++) {
					if (players[i1] == logins.get(j).channel) {
						gamePlayers[i1] = logins.get(j);
						break;
					}
				}
			}

			// Deal Initial Cards
			for (int k = 0; k < gamePlayers.length; k++) {
				sendMessage.setb((byte) 4);
				sendMessage.setNumCards(2);
				sendMessage.setCard1(dealer.dealCard(dealer.deck));
				sendMessage.setCard2(dealer.dealCard(dealer.deck));
				send(sendMessage, gamePlayers[k].channel);
			}

			// send out initial message for whose turn it is
			messageService();
			broadcast_message_all(gamePlayers, 0, 7, pot, 0, null);

			// round of betting
			betting(gamePlayers, 0);
			System.out.println("Made it through the first round of betting :)");
			// deal flop
			dealFlop(gamePlayers, sendMessage, dealer);
			// betting again
			Thread.sleep(1000);
			broadcast_message_all(gamePlayers, 0, 7, pot, 0, null);
			System.out.println("Past broad_cast message");
			betting(gamePlayers, 0);
			// deal turn
			deal_single_pub_card(gamePlayers, sendMessage, dealer);
			// betting
			Thread.sleep(1000);
			broadcast_message_all(gamePlayers, 0, 7, pot, 0, null);
			betting(gamePlayers, 0);
			// deal river
			deal_single_pub_card(gamePlayers, sendMessage, dealer);
			//bet one last time
			Thread.sleep(1000);
			broadcast_message_all(gamePlayers, 0, 7, pot, 0, null);
			betting(gamePlayers, 0);
			// determine winner
			// send results to everyone
			for(int i=0;i<gameSize;i++)
				messageService();
			// loginService();
		}
	}

	public void betting(Player[] players, int turn)
			throws InterruptedException, IOException {
		int size = players.length;
		int activePlayers = 0;

		for (int i = 0; i < size; i++)
			if (!players[i].fold)
				activePlayers++;

		int fair_n_square = 0;
		int minBet = 0;

		while (fair_n_square != activePlayers) {
			if (!players[turn].fold) {
				messageService();
				switch (move) {
				case 1:
					fair_n_square++;
					minBet = tempBet;
					pot+= minBet;
					break;
				case 2:
					fair_n_square = 1;
					minBet = tempBet;
					pot+= minBet;
					break;
				case 3:
					players[turn].fold();
					activePlayers--;
					break;
				}
				turn = (turn + 1) % size;
				if (fair_n_square != activePlayers)
					broadcast_message_all(players, turn, move, pot, minBet,
							user);
				else
					broadcast_message_all(players, -1, move, pot, minBet,
							user);
			} else {
				turn = (turn + 1) % size;
				broadcast_message_all(players, turn, 3, pot, minBet,
						user);
			}
		}
	}

	public void broadcast_message_all(Player[] players, int turn, int move,int pot, int minBet, String userName) throws IOException {
		ServerMessage sendMessage = new ServerMessage();
		int numPlayers = players.length;
		for (int k = 0; k < numPlayers; k++) {
			sendMessage.setb((byte) 5);
			sendMessage.setUsername(userName);
			sendMessage.setMove(move);
			sendMessage.setMinBet(minBet);
			if (k == turn)
				sendMessage.setTurn(1);
			else
				sendMessage.setTurn(0);
			sendMessage.setPot(pot);
			send(sendMessage, players[k].channel);
		}
	}
	
	public void broadcast_message_all(ServerMessage message) throws IOException{
		for(int i = 0; i<gameSize;i++){
			send(message, gamePlayers[i].channel);
		}
	}
	
	public void broadcast_message_all(ServerMessage message, ArrayList<Player> winners) throws IOException{
		String string =  "The winners are: ";
		for(int i=0;i<winners.size();i++){
			string += winners.get(i).userName;
			if(i != winners.size()-1)
				string += ", ";
		}
		message.setString(string);
		broadcast_message_all(message);
	}

	public static class makeFlagFalse {
		boolean flag;
		public makeFlagFalse() {
			this.flag = true;
		}
		public void falsify() {
			flag = false;
		}
		public void makeTrue() {
			flag = true;
		}
	}

	public void send(ServerMessage message, SocketChannel currentPlayer)throws IOException {
		sBuffer.clear();
		sBuffer.put(message.Message2Byte());
		sBuffer.flip();
		currentPlayer.write(sBuffer);
	}
	
	public void dealFlop(Player[] gamePlayers,ServerMessage sendMessage,Dealer dealer) throws IOException{
		
		int pubCard1 = dealer.dealCard(dealer.deck);
		int pubCard2 = dealer.dealCard(dealer.deck);
		int pubCard3 = dealer.dealCard(dealer.deck);
		
		for (int k = 0; k < gamePlayers.length; k++) {
			sendMessage.setb((byte) 4);
			sendMessage.setNumCards(3);
			sendMessage.setpubCard1(pubCard1);
			sendMessage.setpubCard2(pubCard2);
			sendMessage.setpubCard3(pubCard3);
			send(sendMessage, gamePlayers[k].channel);
		}
	}
	
	public void deal_single_pub_card(Player[] gamePlayers,ServerMessage sendMessage,Dealer dealer) throws IOException{
		int sin_pub_card = dealer.dealCard(dealer.deck);
		
		for (int k = 0; k < gamePlayers.length; k++) {
			sendMessage.setb((byte) 4);
			sendMessage.setNumCards(1);
			sendMessage.setpubCard(sin_pub_card);
			send(sendMessage, gamePlayers[k].channel);
		}
	}
	
	public ArrayList<Player> determine_winner(Player[] players){
		//for loop analyzing every player's hands and setting a winner with the highest hand
		Player winner = gamePlayers[0];
		ArrayList<Player> winners = new ArrayList<Player>();
		winners.add(winner);
		for(int i=1;i<gameSize;i++){
			if(gamePlayers[i].score > winner.score){
				winner = gamePlayers[i];
				winners.clear();
				winners.add(winner);
			}else if(gamePlayers[i].score == winner.score){
				if(gamePlayers[i].hand > winner.hand){
					winner = gamePlayers[i];
					winners.clear();
					winners.add(winner);
				}else if(gamePlayers[i].hand == winner.hand){
					if(gamePlayers[i].high > winner.high){
						winner = gamePlayers[i];
						winners.clear();
						winners.add(winner);
					}else if(gamePlayers[i].high == winner.high)
						winners.add(gamePlayers[i]);
				}	
			}
				
		}
		return winners;
	}
	
	public void set_hand_credentials(ServerMessage message, SocketChannel socketChannel){
		for(int i=0;i<gameSize;i++){
			if(socketChannel == gamePlayers[i].channel){
				gamePlayers[i].score = message.getScore();
				gamePlayers[i].hand = message.getHand();
				gamePlayers[i].high = message.getHigh();
				gamePlayers[i].chipCount = message.getChipCount();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException,IOException {
		final NioServer server = new NioServer(20002);
		Thread accept = new Thread() {
			public void run() {
				server.accept();
			}
		};
		accept.start(); /*start the thread to accept clients (always runs)*/
		while (loginLoopBoolean.flag == true)
			server.messageService();
		server.gamePlay(clientsMap);
	}

}
