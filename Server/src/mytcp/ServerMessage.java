package mytcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerMessage {
	private byte b;
	private String username;
	private String password; 
	int chips;
	int minBet;
	private int valid;
	private int ready;
	private int turn;
	private int card1,card2;
		
	public byte[] Message2Byte() throws IOException{
		
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// 字节输出流  
        DataOutputStream dos = new DataOutputStream(baos);// 数据输出流用于包装字节输出流  
        
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
        	case 4:
        		dos.write(card1);
        		dos.write(card2);
        		break;
        	case 5:
        		dos.write(turn);
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
		// TODO Auto-generated method stub
		this.card1 = card1;
	}
	
	public void setCard2(int card2) {
		// TODO Auto-generated method stub
		this.card2 = card2;
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

	
}
