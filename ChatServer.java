import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
	public static void main(String[] args ){
		ArrayList<ChatHandler>handlers = new ArrayList<ChatHandler>();
		ArrayList<String>usernames = new ArrayList<String>();
		try{
			ServerSocket s = new ServerSocket(3000);
			for(;;){
				Socket incoming = s.accept();
				new ChatHandler(incoming, handlers, usernames).start();
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
	ArrayList<String>usernames;
	ObjectInputStream in;
	ObjectOutputStream out;


	public ChatHandler(Socket i, ArrayList<ChatHandler>h, ArrayList<String> u) throws IOException{
		incoming = i;
		handlers = h;
		handlers.add(this);
		usernames = u;
		/*DataObject f = new DataObject();
		f.usernames = u;
		this.out.writeObject(f);*/


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
				}
				else{
					if(objIn.type.equals("connect")){
						usernames.add(objIn.user);
						System.out.println(usernames);
					}
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
