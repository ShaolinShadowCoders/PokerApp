package mytcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GamePlayer {
	private byte b;
	private String username;
	private String password;
	private String type; 
	int chips;
	int minBet;
	private boolean valid;
	private boolean ready;
	
	public byte[] Message2Byte() throws IOException{
		
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// 字节输出流  
        DataOutputStream dos = new DataOutputStream(baos);// 数据输出流用于包装字节输出流  
        
        dos.write(b);
        dos.writeUTF(username);
        dos.writeUTF(password);
        dos.writeUTF(type);
        messagebyte=baos.toByteArray();// 将写入的数据转换成字节数组 
        
        dos.close();  
        baos.close();  
        
        return messagebyte;
	}
	
	public static GamePlayer byte2Message(byte[] messagebyte) throws IOException{
		GamePlayer mymessage=new GamePlayer();
		ByteArrayInputStream bais = new ByteArrayInputStream(messagebyte);// 字节输入流  
        DataInputStream dis = new DataInputStream(bais);// 数据输入流用于包装字节输入流
        
        mymessage.setb(dis.readByte());
        mymessage.setUsername(dis.readUTF());
        mymessage.setPassword(dis.readUTF());
        mymessage.setType(dis.readUTF());
        
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
	
	public void setType(String type){
		this.type=type;
	}
	
	public void setValid(boolean valid){
		this.valid=valid;
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
	
	public String getType(){
		return type;
	}
	
	public Boolean getValid(){
		return valid;
	}
}
