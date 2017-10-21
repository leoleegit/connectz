package org.connectz.com.demos.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TcpClient().connect();
	}

	public void connect(){
		 try {
             String host  = "io.connectz.cn";
             int    port  = 5086;
             Socket socket=new Socket(host,port);
             
             OutputStream outputStream=socket.getOutputStream(); 
             
             PrintWriter printWriter=new PrintWriter(outputStream); 
             printWriter.print("服务端你好，我是Balla_兔子");
             printWriter.flush();
             socket.shutdownOutput();
             
             InputStream inputStream=socket.getInputStream();//获取一个输入流，接收服务端的信息
             InputStreamReader inputStreamReader=new InputStreamReader(inputStream);//包装成字符流，提高效率
             BufferedReader bufferedReader=new BufferedReader(inputStreamReader);//缓冲区
             String info="";
             String temp=null;
             while((temp=bufferedReader.readLine())!=null){
                 info+=temp;
                 System.out.println("客户端接收服务端发送信息："+info);
             }

             bufferedReader.close();
             inputStream.close();
             printWriter.close();
             outputStream.close();
             socket.close();
         } catch (UnknownHostException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
	}
}
