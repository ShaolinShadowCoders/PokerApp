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

public class NioServer {

	/**
	 * @param args
	 */
	private int port = 20001;
	// 解码buffer
	// private Charset cs = Charset.forName("gbk");
	/* 接受数据缓冲区 */
	private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
	/* 发送数据缓冲区 */
	private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
	/* 映射客户端channel */
	private Map<SocketChannel, Integer> clientsMap = new HashMap<SocketChannel, Integer>();

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
				socketChannel.configureBlocking(false);
				// ByteBuffer buffer=ByteBuffer.allocate(1024);
				synchronized (gate) {
					selector.wakeup();
					socketChannel.register(selector, SelectionKey.OP_READ);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void loginService() {
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
							handle_receive_gameplay(key);
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
		Message message = null;
		Message sendMessage = new Message();
		socketChannel = (SocketChannel) key.channel();
		rBuffer.clear();
		try {
			int count = socketChannel.read(rBuffer);
			if (count > 0) {
				rBuffer.flip();
				message = Message.byte2Message(rBuffer.array());
				System.out.println("Receive from"
						+ socketChannel.socket().getInetAddress() + " : "
						+ message.getb() + "," + message.getUsername() + ","
						+ message.getPassword());
				if (message.getType().equals("login")) { // Insert DB Code Here
					clientsMap.put(socketChannel, clientsMap.size() + 1);
					// this code could be put directly as argument into
					// sendMessage.setValid but separate valid variable enhances
					// readability
					Boolean valid = DB.idCheck(message.getUsername(),
							message.getPassword());
					sendMessage.setb((byte) 1);
					sendMessage.setValid(valid);
					sBuffer.clear();
					sBuffer.put(sendMessage.Message2Byte());
					sBuffer.flip();
					socketChannel.write(sBuffer);
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

	public void handle_receive_gameplay(SelectionKey key) {
		SocketChannel socketChannel = null;
		Message message = null;
		Message sendMessage = new Message();
		socketChannel = (SocketChannel) key.channel();
		rBuffer.clear();
		try {
			int count = socketChannel.read(rBuffer);
			if (count > 0) {
				rBuffer.flip();
				message = Message.byte2Message(rBuffer.array());
				System.out.println("Receive from"
						+ socketChannel.socket().getInetAddress() + " : "
						+ message.getb() + "," + message.getUsername() + ","
						+ message.getPassword());
				if (message.getStr().equals("send back")) {
					sendMessage.setb((byte) 2);
					sendMessage.setstr("get message from server");
					sBuffer.clear();
					// sBuffer.get(sendMessage.Message2Byte());
					sBuffer.put(sendMessage.Message2Byte());
					sBuffer.flip();
					socketChannel.write(sBuffer);
				} else if (message.getStr().equals("send to others")) {// Ask
																		// Lou
																		// about
																		// this
																		// code
					if (!clientsMap.isEmpty()) {
						// for(Map.Entry<SocketChannel,Integer> entry :
						// clientsMap.entrySet())
						Set<SocketChannel> clientSet = clientsMap.keySet();
						Iterator<SocketChannel> iterator = clientSet.iterator();
						while (iterator.hasNext()) {
							SocketChannel temp = iterator.next();
							if (!socketChannel.equals(temp)) {
								sendMessage.setb((byte) 2);
								sendMessage
										.setstr("get message from other player( ip:"
												+ socketChannel.socket()
														.getInetAddress()
												+ ",+ port:"
												+ socketChannel.socket()
														.getPort());
								sBuffer.clear();
								// sBuffer.get(sendMessage.Message2Byte());
								sBuffer.put(sendMessage.Message2Byte());
								sBuffer.flip();
								// 输出到通道
								temp.write(sBuffer);
							}
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final NioServer server = new NioServer(20001);
		Thread accept = new Thread() {
			public void run() {
				server.accept();
			}
		};
		accept.start();
		server.loginService();
		/*
		 * Message message=new Message();
		 * 
		 * message.setb(((byte)1)); message.setstr("send back!");
		 * System.out.println(message.getb()+";"+message.getString()); try{ byte
		 * []be=message.Message2Byte(); Message m1=Message.byte2Message(be);
		 * System.out.println(m1.getb()+";"+m1.getString()); }catch(Exception
		 * e){ e.printStackTrace(); }
		 */
	}

}
