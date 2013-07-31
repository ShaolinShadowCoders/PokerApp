package mytcp;

import java.nio.channels.SocketChannel;

public class Player {
	int chipCount,bet;
	String userName,passWord;
	SocketChannel channel;
	boolean turn,fold;
	
	Player(){
		this.turn = false;
		this.fold = false;
		this.chipCount = 500;
	}
	
	Player(String userName, String passWord, SocketChannel channel){
		this.userName = userName;
		this.passWord = passWord;
		this.channel = channel;
		this.turn = false;
		this.fold = false;
		this.chipCount = 500;
	}
	
	public void fold(){
		this.fold = true;
	}
	
	public void turn(boolean turn){
		this.turn = turn;
	}
	
	public int setBet(int bet){
		chipCount -=bet;
		return bet;
	}
	
	
}
