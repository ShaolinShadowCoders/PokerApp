package mytcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.lang.model.element.Element;

public class ServerMessage {
	private byte b;
	private String username,password,string;
	private int minBet,valid,ready,card1,card2,turn,move,pot,pubCard1,pubCard2,pubCard3,single_pub_card,numCards,score,hand,high,chipCount;
	
	
	public byte[] Message2Byte() throws IOException{
		//sends a message to the client
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.write(b);
        
        switch (b) {
        	case 1://Login
        		dos.writeUTF(username);
        		dos.writeUTF(password);
        		break;
        	case 2://Check Password
        		dos.write(valid);
        		break;
        	case 3://Client 'Ready' to play
        		dos.write(ready);
        		break;
        	case 4://Updated
        		dos.write(numCards);
        		switch(numCards){
        			case 1:
                		dos.write(single_pub_card);
                		break;
        			case 2:
        				dos.write(card1);
            			dos.write(card2);
            			break;
        			case 3:
        				dos.write(pubCard1);
                		dos.write(pubCard2);
                		dos.write(pubCard3);
                		break;
        		}
        		break;
        	case 5://send out turn and who did what bet-wise
        		if(username != null)
        			dos.writeUTF(username);
        		else
        			dos.writeUTF("no one");
        		dos.write(move);
        		dos.write(minBet);
        		dos.write(turn);
        		dos.write(pot);
        		break;
        	case 7:
        		if(username != null) /*if there's only one winner*/
        			dos.writeUTF(username);
        		else
        			dos.writeUTF(string);
        		dos.write(score);
        		dos.write(hand);
        		dos.write(high);
        		break;
        	default:
				break;
		}
        
        messagebyte=baos.toByteArray();// 将写入的数据转换成字节数组 
        
        dos.close();  
        baos.close();  
        
        return messagebyte;
	}
	
	public static ServerMessage byte2Message(byte[] messagebyte) throws IOException{
		ServerMessage mymessage=new ServerMessage();
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
        	
        	
        	case 5: //receive bet and move from client
        		mymessage.setMinBet(dis.read());
        		mymessage.setMove(dis.read());
        		mymessage.setUsername(dis.readUTF());
        		break;
        }
    
        
        
        return mymessage;
	}
	
	public void setb(byte b){
		this.b=b;
	}
	
	public void setUsername(String username){
		this.username=username;
	}
	
	public void setPassword(String strps){
		this.password=strps;
	}
	
	public void setReady(int ready){
		this.ready=ready;
	}
	
	public void setValid(int valid){
		this.valid=valid;
	}
	
	public void setCard1(int card1) {
		this.card1 = card1;
	}
	
	public void setCard2(int card2) {
		this.card2 = card2;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public void setMove(int move) {
		this.move = move;
	}
	
	public void setMinBet(int minBet) {
		this.minBet = minBet;
	}
	
	public void setPot(int pot) {
		this.pot =pot;
	}
	
	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
	
	public void setpubCard1(int pubCard1) {
		this.pubCard1 = pubCard1;
	}

	public void setpubCard2(int pubCard2) {
		this.pubCard2 = pubCard2;
	}

	public void setpubCard3(int pubCard3) {
		this.pubCard3 = pubCard3;
	}

	public void setpubCard(int single_pub_card) {
		this.single_pub_card = single_pub_card;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setHand(int hand){
		this.hand = hand;
	}
	
	public void setHigh(int high){
		this.high = high;
	}
	
	public void setChipCount(int chipCount){
		this.chipCount = chipCount;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public byte getb(){
		return b;
	}
	
	public String getUsername(){
		return username;
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
	
	public int getTurn(){
		return turn;
	}
	
	public int getMove(){
		return this.move;
	}
	
	public int getMinBet(){
		return minBet;
	}

	public int getPot(){
		return pot;
	}

	public int getPubCard1() {
		return this.pubCard1;
	}

	public int getPubCard2() {
		return this.pubCard2;
	}

	public int getPubCard3() {
		return this.pubCard3;
	}

	public int getSinPubCard() {
		return this.single_pub_card;
	}
	
	public int getNumCards(){
		return this.numCards;
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

	public int getChipCount() {
		return this.chipCount;
	}

	public String getString(){
		return this.string;
	}
	

	
	
}
