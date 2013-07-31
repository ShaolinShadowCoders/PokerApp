package com.example.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyMessage {
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
	public static MyMessage byte2Message(byte[] messagebyte) throws IOException{
		MyMessage mymessage=new MyMessage();
		//byte []temp=null;
		ByteArrayInputStream bais = new ByteArrayInputStream(messagebyte);// 字节输入流  
        DataInputStream dis = new DataInputStream(bais);// 数据输入流用于包装字节输入流
        mymessage.setb(dis.readByte());
        mymessage.setstr(dis.readUTF());
        /*dis.read(temp);
        message.setstr(temp.toString());*/
        return mymessage;
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