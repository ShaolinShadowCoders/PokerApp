package com.example.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PublicKey;

public class MyMessage {
	private byte b;
	private String userName,password,string;
	private int valid,ready,cardOne, cardTwo,turn,bet,minBet,pot,pubCard1,pubCard2,pubCard3,singlePubCard, move, numCards, score, hand, high,chipCount;
		
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
    	case 5://sending a bet 
    		dos.write(minBet);
    		dos.write(move);
    		dos.writeUTF(userName);
    		break;
    	case 6://Sending score request
    		dos.writeUTF(userName);
    		dos.write(score);
    		dos.write(hand);
    		dos.write(high);
    		dos.write(chipCount);
    		break;
		default:
			break;
        }      
        messagebyte=baos.toByteArray();
        dos.close();  
        baos.close();  
        
        return messagebyte;
	}
	
	
	public static MyMessage byte2Message(byte[] messagebyte) throws IOException{
		//receiving message from the server
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
    		case 4://set cards //UPDATED
    			mymessage.setNumCards(dis.read());
    			switch (mymessage.getNumCards()){
    				case 1:
    					mymessage.setSinPubCard(dis.read());
    					break;
    				case 2:
    					mymessage.setCardOne(dis.read());
    					mymessage.setCardTwo(dis.read());
    					break;
    				case 3:
    					mymessage.setPubCard1(dis.read());
    					mymessage.setPubCard2(dis.read());
    					mymessage.setPubCard3(dis.read());
    					break;
    			}
    			break;
    		case 5://bet information
    			mymessage.setUsername(dis.readUTF());
    			mymessage.setMove(dis.read());
    			mymessage.setMinBet(dis.read());
    			mymessage.setTurn(dis.read());
    			mymessage.setPot(dis.read());
    			break;
    		case 6:
    			break;
    		case 7:
    			mymessage.setUsername(dis.readUTF());
    			mymessage.setScore(dis.read());
    			mymessage.setHand(dis.read());
    			mymessage.setHigh(dis.read());
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
	
	public void setMove(int move){
		this.move = move;
	}
	
	public void setBet(int bet){
		this.bet = bet;
	}
	
	public void setPubCard1(int pubCard1){
		 this.pubCard1 = pubCard1;
	}
	
	public void setPubCard2(int pubCard2){
		 this.pubCard2 = pubCard2;
	}
	
	public void setPubCard3(int pubCard3){
		this.pubCard3 = pubCard3;
	}
	
	public void setSinPubCard(int sinPubCard){
		this.singlePubCard = sinPubCard;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	public void setHand(int hand){
		this.hand = hand;
	}
	
	public void setHigh(int high){
		this.high = high;
	}
	
	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
	
	public void setChipCount(int chipCount) {
		this.chipCount = chipCount;
	}
	
	public void setString(String string){
		this.string = string;
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
	
	public int getMove(){
		return this.move;
	}
	
	public int getPubCard1(){
		return this.pubCard1;
	}
	
	public int getPubCard2(){
		return this.pubCard2;
	}
	
	public int getPubCard3(){
		return this.pubCard3;
	}
	
	public int getSinPubCard(){
		return this.singlePubCard;
	}

	public int getScore(){
		return this.score;
	}
	public int getHand(){
		return this.hand;
	}
	
	public int getHigh(){
		return this.high;
	}
	
	public int getNumCards(){
		return this.numCards;
	}
	
	public int getChipCount(){
		return this.chipCount;
	}

	public String getString() {
		return this.string;
	}
}