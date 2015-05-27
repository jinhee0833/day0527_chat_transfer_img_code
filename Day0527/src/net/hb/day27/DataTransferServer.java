package net.hb.day27;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DataTransferServer {
public static void main(String[] args) throws Exception{
		
		ServerSocket server = new ServerSocket(8888);
		
		InputStream in = new FileInputStream("C:\\Mtest\\images\\aaa.gif"); // send file name
		
		System.out.println(" ready --------------------");
		
		Socket clientSocket = server.accept();
		
		OutputStream out = clientSocket.getOutputStream();
		
		System.out.println(clientSocket);
		
		while(true){
			int data = in.read();
			
			if(data == -1){
				break;
			}// end if
			out.write(data);
		}// end while
		
		out.flush();
		out.close();
		in.close();
		clientSocket.close();
		server.close();
	}// end main
}// end clas
