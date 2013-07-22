package mytcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
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

	public NioServer(int port) {
		this.port = port;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		final NioServer server = new NioServer(20001);
		Thread accept = new Thread() {
			public void run() {
				server.accept();
			}
		};
		
		accept.start();
		while (loginLoopBoolean.flag == true) 
			server.loginService();
		server.gamePlay(clientsMap);
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

	public void loginService() throws InterruptedException {
		
		
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
							handle_receive_login(key);
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

	public void handle_receive_login(SelectionKey key) {
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
				case(1):
					
						int valid = DB.idCheck(message.getUsername(),
								message.getPassword());
						sendMessage.setb((byte) 2);
						sendMessage.setValid(valid);
						sendMes sendMes = new sendMes(sendMessage, socketChannel);
						sendMes.send();
						
					break;
				case(2):
					break;
				case (3):
					if(timerFlag.flag){
						if(message.getReady() == 1){
							if(clientsMap.size() < 6 /*&& flag=false*/){
								clientsMap.put(socketChannel, clientsMap.size() + 1);
								sendMessage.setb((byte) 3);
								sendMessage.setReady(1);
								sendMes sendMes1 = new sendMes(sendMessage, socketChannel);
								sendMes1.send();
							}
							
							else{
								sendMessage.setb((byte) 3);
								sendMessage.setReady(0);
								sendMes sendMes1 = new sendMes(sendMessage, socketChannel);
								sendMes1.send();
							}
					
							Timer timer = new Timer();
					
							System.out.println("flag is " + timerFlag.flag);
							TimerTask task = new TimerTask(){
								public void run(){
									timerFlag.falsify();
									System.out.println("flag now is " + timerFlag.flag);
								}
							};
							timer.schedule(task, 20*1000);
						}
					}else{
						sendMessage.setb((byte) -1); /*-1 implies that game is currently in progress*/
						sendMes sendMes1 = new sendMes(sendMessage, socketChannel);
						sendMes1.send();
					}
					break;
				
				case (4):
					System.out.println("Hey it gets here you dum dum");
					if(timerFlag.flag == true){
						sendMessage.setb((byte) -2); /*send message saying "waiting for other players*/
						sendMes sendMes1 = new sendMes(sendMessage, socketChannel);
						sendMes1.send();
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
			Iterator<SocketChannel> iterator=clientSet.iterator();
			SocketChannel currentPlayer;
			while(iterator.hasNext()){
				currentPlayer=iterator.next();  
				sendMessage.setb((byte) 4);
				sendMessage.setCard1(dealer.dealCard(dealer.deck));
				sendMessage.setCard2(dealer.dealCard(dealer.deck));
				sendMes sendMes1 = new sendMes(sendMessage, currentPlayer);
				sendMes1.send();
				System.out.println("Dealt Cards");
			}
			//send who's turn it is
			
			loginService();
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
	
	public class sendMes{
		ServerMessage message;
		SocketChannel currentPlayer;
		
		sendMes(ServerMessage message,SocketChannel currentPlayer){
			this.message = message;
			this.currentPlayer=currentPlayer;
		}
		public void send() throws IOException{
			sBuffer.clear();
			sBuffer.put(message.Message2Byte());
			sBuffer.flip();
			currentPlayer.write(sBuffer);
		}
	}
	
	
}
