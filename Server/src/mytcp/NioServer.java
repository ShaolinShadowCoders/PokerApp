package mytcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
	final makeFlagFalse flagClass = new makeFlagFalse();
	// 解码buffer
	// private Charset cs = Charset.forName("gbk");
	/* 接受数据缓冲区 */
	private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
	/* 发送数据缓冲区 */
	private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
	/* 映射客户端channel */
	private static Map<SocketChannel, Integer> clientsMap = new HashMap<SocketChannel, Integer>();

	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;
	private Object gate = new Object();

	public NioServer(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
		try {
			init();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void init() throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		// serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动");
	}

	public void accept() {
		while (true) {
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				System.out.println("receive the connection from "
						+ socketChannel.socket().getInetAddress() + ":"
						+ socketChannel.socket().getPort());
				Timer timer = new Timer();
				
				System.out.println("flag is " + flagClass.flag);
				TimerTask task = new TimerTask(){
					public void run(){
						flagClass.falsify();
						System.out.println("flag now is " + flagClass.flag);
					}
				};
				timer.schedule(task, 60*1000);
				socketChannel.configureBlocking(false);
				// ByteBuffer buffer=ByteBuffer.allocate(1024);
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
		
		while (flagClass.flag ==true) {
			synchronized (gate) {
			}
			try {
				int n = selector.select();
				if (n == 0)
					continue;
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				for (SelectionKey key : selectionKeys) {
					try {
						if (key.isReadable()) {
							handle_receive_login(key);
						}
					} catch (Exception e) {
						// TODO: handle exception
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void gameService() {
		Boolean flag = true;
		while (flag == true) {
			synchronized (gate) {
			}
			try {
				int n = selector.select();
				if (n == 0)
					continue;
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				for (SelectionKey key : selectionKeys) {
					try {
						if (key.isReadable()) {
							// handle_receive_gameplay(key);
						}
					} catch (Exception e) {
						// TODO: handle exception
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handle_receive_login(SelectionKey key) {
		SocketChannel socketChannel = null;
		GamePlayer message = null;
		GamePlayer sendMessage = new GamePlayer();
		socketChannel = (SocketChannel) key.channel();
		rBuffer.clear();
		try {
			int count = socketChannel.read(rBuffer);
			if (count > 0) {
				rBuffer.flip();
				message = GamePlayer.byte2Message(rBuffer.array());
				System.out.println("Receive from"
						+ socketChannel.socket().getInetAddress() + " : "
						+ message.getb() + "," + message.getUsername() + ","
						+ message.getPassword());
				if (message.getb() == 1) { // Insert DB Code Here
					
					// this code could be put directly as argument into
					// sendMessage.setValid but separate valid variable enhances
					// readability
					int valid = DB.idCheck(message.getUsername(),
							message.getPassword());
					sendMessage.setb((byte) 2);
					sendMessage.setValid(valid);
					sBuffer.clear();
					sBuffer.put(sendMessage.Message2Byte());
					sBuffer.flip();
					socketChannel.write(sBuffer);
					}
				else if (message.getb() == 3){
					
					if(message.getReady() == 1){
						if(clientsMap.size() < 6 /*&& flag=false*/){
							clientsMap.put(socketChannel, clientsMap.size() + 1);
							sendMessage.setb((byte) 3);
							sendMessage.setReady(1);
							sBuffer.clear();
							sBuffer.put(sendMessage.Message2Byte());
							sBuffer.flip();
							socketChannel.write(sBuffer);
						}
							
						else{
							sendMessage.setb((byte) 3);
							sendMessage.setReady(0);
							sBuffer.clear();
							sBuffer.put(sendMessage.Message2Byte());
							sBuffer.flip();
							socketChannel.write(sBuffer);
						}
							
					}		
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			key.cancel();
			try {
				socketChannel.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	
	

	
	 
	 
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final NioServer server = new NioServer(20001);
		Thread accept = new Thread() {
			public void run() {
				server.accept();
			}
		};
		accept.start();
		server.loginService(/*int time*/);
		//while(clientsMap.size() >= 2){
			//int players =clientsMap.size();
			//Hand hand = new Hand(players,clientsMap);
			//server.loginService(10)
		//}
		
		
	}
	
	public class makeFlagFalse{
		boolean flag;
		
		public makeFlagFalse() {
			// TODO Auto-generated constructor stub
			this.flag = true;
		}
		public void falsify(){
			flag = false;
		}
	}

}
