package net.hb.day27;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DataTransferClient {

	public static void main(String[] args) throws Exception{
		
		Socket socket = new Socket("203.236.209.122", 8888);
		
		System.out.println(socket);
		
		InputStream in = socket.getInputStream();
		OutputStream out = new FileOutputStream("C:\\Mtest\\images\\xyz.gif"); // save name
		
		while(true){
			int data = in.read();
			
			if(data == -1 ){
				break;
			}// end if
			out.write(data);
		}// end while
		
		in.close();
		out.close();
		socket.close();
	}// end main
}// end class
