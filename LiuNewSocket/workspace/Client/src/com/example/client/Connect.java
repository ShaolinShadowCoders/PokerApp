package com.example.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

//Here is my test. w00t w00t!!!
//another test

public class Connect {
	SocketChannel socketChannel=null;
	public ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
	ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
	Selector selector;
	public Connect() {
		// TODO Auto-generated constructor stub
		try {
			socketChannel=SocketChannel.open();
			SocketAddress remoteAddress=new InetSocketAddress("58.198.95.225", 4322);
			socketChannel.connect(remoteAddress);
			socketChannel.configureBlocking(false);
			System.out.println("与服务器的连接建立成功");
		    selector=Selector.open();
		    //play();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void play() throws IOException{
		socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		while(true){
			if(selector.select()==0)
				continue;
			Set readyKeySet=selector.selectedKeys();
			Iterator<SelectionKey> iterator=readyKeySet.iterator();
			SelectionKey key=null;
			while(iterator.hasNext()){
				
				key=(SelectionKey) iterator.next();
				if(key.isReadable()){
					receiveBuffer.clear();
					socketChannel.read(receiveBuffer);
					receiveBuffer.flip();
					
				}else if (key.isWritable()) {
					sendBuffer.flip(); //把极限设为位置
			        socketChannel.write(sendBuffer);
			        sendBuffer.compact();
				}
			}
		}
	}
}
