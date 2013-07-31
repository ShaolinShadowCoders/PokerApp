package mytcp;

import java.io.IOException;
import java.net.InetSocketAddress;
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
	private int tempBet,pot;
	
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
				if (n == 0){}
				else{
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
				System.out.println("Receive from"+ socketChannel.socket().getInetAddress() + " : "+ message.getb());
				switch(message.getb()){
		
				case(1): /*login*/
						int valid = DB.idCheck(message.getUsername(),
								message.getPassword());
						if(valid == 1){
							logins.add(new Player(message.getUsername(),message.getPassword(),socketChannel));
						}
						sendMessage.setb((byte) 2);
						sendMessage.setValid(valid);
						send(sendMessage, socketChannel);
						
						
					break;
				case (3): /*ready screen*/
					if(timerFlag.flag){
						if(message.getReady() == 1){
							if(clientsMap.size() < 6){
								clientsMap.put(socketChannel, clientsMap.size() + 1);
								sendMessage.setb((byte) 3);
								sendMessage.setReady(1);
								 send(sendMessage, socketChannel);
								
							}else{
								sendMessage.setb((byte) 3);
								sendMessage.setReady(0);
								send(sendMessage, socketChannel);
							}
						}else{
							sendMessage.setb((byte) 3);
							sendMessage.setReady(0);
							send(sendMessage, socketChannel);
						}
							Timer timer = new Timer();
					
							System.out.println("flag is " + timerFlag.flag);
							TimerTask task = new TimerTask(){
								public void run(){
									timerFlag.falsify();
									System.out.println("flag now is " + timerFlag.flag);
								}
							};
							timer.schedule(task, 3*1000);
						
					}else{
						sendMessage.setb((byte) -1); /*-1 implies that game is currently in progress*/
						send(sendMessage, socketChannel);
					}
					break;
				
				case (4):/*dealing out cards*/
					if(timerFlag.flag == true){
						sendMessage.setb((byte) -2); /*send message saying "waiting for other players*/
						send(sendMessage, socketChannel);
					}else{
						loginLoopBoolean.falsify();
					}
					break;
				
				}	
			}/*end of if(count=0)*/
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
	
	
	public void gamePlay(Map<SocketChannel, Integer> clientsMap) throws IOException, InterruptedException{
		Dealer dealer = new Dealer();
		dealer.createDeck();
		ServerMessage sendMessage = new ServerMessage();
		if(!clientsMap.isEmpty()){  
			Set<SocketChannel> clientSet = clientsMap.keySet();
			int size = clientSet.size();
			
			SocketChannel[] players = new SocketChannel[size];
			clientSet.toArray(players);
			Player[] gamePlayers = new Player[size];
			
			//transfer clients to a fixed 'Player' array
			for(int i1=0;i1<size;i1++){
				for(int j=0;j<logins.size();j++){ 
					if(players[i1] == logins.get(j).channel){
						gamePlayers[i1] = logins.get(j);
						break;
					}
				}
			}
			
			//Deal Cards
			for(int k=0;k<gamePlayers.length;k++){
				sendMessage.setb((byte) 4);
				sendMessage.setCard1(dealer.dealCard(dealer.deck));
				sendMessage.setCard2(dealer.dealCard(dealer.deck));
				send(sendMessage, gamePlayers[k].channel);
			}
			
			
			//Send whose turn it is initially
			int turn = 0;
			for(int k=0;k<gamePlayers.length;k++){
				sendMessage.setb((byte) 5);
				if(k==turn)
					sendMessage.setTurn(1);
				else
					sendMessage.setTurn(0);
				send(sendMessage, gamePlayers[k].channel);
			}
			
			//round of betting
			betting(gamePlayers,0);
			
			//deal flop
				//send cards to every client
			//betting again
			betting(gamePlayers,0);
			//deal turn
				//send cards to every client
			//betting
			//deal river
				//send cards to every client
			//determine winner
				//send results to everyone
			//loginService();
		}
	}
	
	public void betting(Player[] players,int turn) throws InterruptedException, IOException{
		int size = players.length;
		int fair_n_square = 0;
		int minBet = 0;
		
		while(fair_n_square != size){
			turn %= size;
			if(!players[turn].fold){
				messageService();
				players[turn].bet = tempBet;
				
				if(players[turn].bet>minBet)
					fair_n_square =1;
				else if(players[turn].bet == minBet)
					fair_n_square++;
				else size--;
				
				minBet = handle_bet(players[turn],minBet);
				broadcast_message_all(players,players[turn].bet,pot,minBet,players[turn].userName,++turn);
			}else broadcast_message_all(players,-1,pot,minBet,players[turn].userName,++turn);
		}
	}
	
	
	public int handle_bet(Player player,int minBet){
		
		if(player.bet != -1){
			player.chipCount -= player.bet;
			pot +=player.bet;
			if(player.bet>minBet)
				minBet = player.bet;
		}else player.fold = true;
		return minBet;
	}
	
	public void broadcast_message_all(Player[] players,int bet,int pot,int minBet,String userName,int turn) throws IOException{
		ServerMessage sendMessage = new ServerMessage();
		for(int k=0;k<players.length;k++){
			sendMessage.setb((byte) 6);
			sendMessage.setUsername(userName);
			sendMessage.setBet(bet);
			sendMessage.setPot(pot);
			sendMessage.setMinBet(minBet);
			send(sendMessage, players[k].channel);
		}
	}
	
	public static class makeFlagFalse{
		boolean flag;
		public makeFlagFalse() {
			this.flag = true;
		}
		public void falsify(){
			flag = false;
		}
		public void makeTrue(){
			flag = true;
		}
	}
	
		public void send(ServerMessage message,SocketChannel currentPlayer) throws IOException{
			sBuffer.clear();
			sBuffer.put(message.Message2Byte());
			sBuffer.flip();
			currentPlayer.write(sBuffer);
		}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		final NioServer server = new NioServer(20001);
		Thread accept = new Thread() {
			public void run() {
				server.accept();
			}
		};
		
		accept.start();
		while (loginLoopBoolean.flag == true) 
			server.messageService();
		server.gamePlay(clientsMap);
	}
	
}
