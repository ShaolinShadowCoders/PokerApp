package mytcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {
	private byte b;
	private String str;
	public byte[] Message2Byte() throws IOException{
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// 字节输出流  
        DataOutputStream dos = new DataOutputStream(baos);// 数据输出流用于包装字节输出流  
        dos.write(b);
        dos.writeUTF(str);
        messagebyte=baos.toByteArray();// 将写入的数据转换成字节数组 
        dos.close();  
        baos.close();  
        return messagebyte;
	}
	public static Message byte2Message(byte[] messagebyte) throws IOException{
		Message message=new Message();
		//byte []temp=null;
		ByteArrayInputStream bais = new ByteArrayInputStream(messagebyte);// 字节输入流  
        DataInputStream dis = new DataInputStream(bais);// 数据输入流用于包装字节输入流
        message.setb(dis.readByte());
        message.setstr(dis.readUTF());
        /*dis.read(temp);
        message.setstr(temp.toString());*/
        return message;
	}
	public void setb(byte b){
		this.b=b;
	}
	public void setstr(String str){
		this.str=str;
	}
	public byte getb(){
		return b;
	}
	public String getStr(){
		return str;
	}
}
