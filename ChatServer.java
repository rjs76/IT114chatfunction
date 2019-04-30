import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{  
	public static void main(String[] args ){  
		ArrayList<ChatHandler>handlers = new ArrayList<ChatHandler>();
		try{  
			ServerSocket s = new ServerSocket(3000);
			for(;;){
				Socket incoming = s.accept();
				new ChatHandler(incoming, handlers).start();
			}
		}catch (Exception e){  
			System.out.println(e);
		} 
	} 
}	  
	  
class ChatHandler extends Thread{
	DataObject myObject = null;
	private Socket incoming;
	ArrayList<ChatHandler>handlers; 
	ObjectInputStream in;
	ObjectOutputStream out;
	
	public ChatHandler(Socket i, ArrayList<ChatHandler>h){
		incoming = i;
		handlers = h;
		handlers.add(this);
	}
	public void run(){
		try{
			in = new ObjectInputStream(incoming.getInputStream());
			out = new ObjectOutputStream(incoming.getOutputStream());

			boolean done = false;
			while (!done){  
				DataObject objIn = (DataObject)in.readObject();
				if (objIn == null){
					done = true;
				}else{
					for(ChatHandler h : handlers){
						h.out.writeObject(objIn);
					}
					if (objIn.getMessage().trim().equals("BYE")){ 
						done = true;
					}
				}
			}
			incoming.close();
		}catch (Exception e){  
			System.out.println(e);
		}finally{
			handlers.remove(this);
		} 
	} 
}
