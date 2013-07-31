package com.example.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyMessage {
	private byte b;
	private String userName,password;
	private int valid,ready,cardOne, cardTwo,turn,bet,minBet,pot;
		
	public byte[] Message2Byte() throws IOException{
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// 字节输出流  
        DataOutputStream dos = new DataOutputStream(baos);// 数据输出流用于包装字节输出流  
        
        dos.write(b);
        
        switch (b) {
    	case 1://Login
    		dos.writeUTF(userName);
    		dos.writeUTF(password);
    		break;
    	case 2://Check Password
    		dos.write(valid);
    		break;
    	case 3://Client 'Ready' to play
    		dos.write(ready);
    	case 4://Check Server Status and Receive Cards;
    		break;
    	case 5: 
    		dos.write(turn);
    		break;
    	case 6://send bet to the server
    		dos.write(bet);
		default:
			break;
        }       
        
        messagebyte=baos.toByteArray();// 将写入的数据转换成字节数组 
        dos.close();  
        baos.close();  
        
        return messagebyte;
	}
	
	
	public static MyMessage byte2Message(byte[] messagebyte) throws IOException{
		
		MyMessage mymessage=new MyMessage();
		ByteArrayInputStream bais = new ByteArrayInputStream(messagebyte);// 字节输入流  
        DataInputStream dis = new DataInputStream(bais);// 数据输入流用于包装字节输入流
        
        
        mymessage.setb(dis.readByte());
        
        switch(mymessage.b){
    	
    		case 1://Login
    			mymessage.setUsername(dis.readUTF());
    			mymessage.setPassword(dis.readUTF());
    			break;
    	
    		case 2://Check Password
    			mymessage.setValid(dis.read());
    			break;
    	
    		case 3://Client 'Ready' to play
    			mymessage.setReady(dis.read());
    			break;
    		case 4://set cards
    			mymessage.setCardOne(dis.read());
    			mymessage.setCardTwo(dis.read());
    			break;
    		case 5://check if it's your turn
    			mymessage.setTurn(dis.read());
    			break;
    		case 6: //receiving updates on the server from the last person that bet
    			mymessage.setUsername(dis.readUTF());
    			mymessage.setBet(dis.read());
    			mymessage.setPot(dis.read());
    			mymessage.setMinBet(dis.read());
    			mymessage.setTurn(dis.read());
    			break;
    		default:
    			break;
        }
               
        return mymessage;
	}
	
	public void setb(byte i){
		this.b=i;
	}
	
	public void setUsername(String userName){
		this.userName=userName;
	}
	
	public void setPassword(String strps){
		this.password=strps;
	}
	
	public void setReady(int ready){
		this.ready = ready;
	}
	
	public void setValid(int valid){
		this.valid=valid;
	}
	
	private void setCardOne(int card){
		this.cardOne = card;
	}
	
	private void setCardTwo(int card){
		this.cardTwo = card;
	}
	
	public void setTurn(int turn){
		this.turn = turn;
	}
	
	
	public void setPot(int pot){
		this.pot = pot;
	}
	
	public void setMinBet(int minBet){
		this.minBet = minBet;
	}
	
	public void setBet(int bet){
		this.bet = bet;
	}
	
	public byte getb(){
		return b;
	}
	
	public String getUsername(){
		return userName;
	}
	
	public String getPassword(){
		return password;
	}
	
	public int getReady(){
		return ready;
	}
	
	public int getValid(){
		return valid;
	}
	
	public int getCardOne(){
		return cardOne;
	}
	
	public int getCardTwo(){
		return cardTwo;
	}
	
	public int getTurn(){
		return turn;
	}
	
	public int getBet(){
		return bet;
	}
	
	public int getPot(){
		return pot;
	}
	
	public int getMinBet(){
		return minBet;
	}
	
}