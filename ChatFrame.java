import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class ChatFrame extends Frame{
	public ChatFrame(){
		setSize(500, 500);
		setTitle("Chat Frame");
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		//added this to chatpanel 
		add(new ChatPanel(this), BorderLayout.CENTER);
		setVisible(true);
	}
	public static void main(String[] args){
		new ChatFrame();
	}
}

class ChatPanel extends Panel implements ActionListener, Runnable{  //INCOMPLETE!!!
	TextArea ta;
	TextField tf;
	Button connect, disconnect;
	Thread thread;
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	DataObject d1,d2;
	String username;
	ChatFrame newchat;
	boolean connected;
	
	// added chatframe 
	public ChatPanel(ChatFrame chatting){
		//added this line making the newchat frame being equal to the new chat chatting
		
			this.newchat= chatting;
			setLayout(new BorderLayout());
			tf = new TextField();
			tf.addActionListener(this);
			ta = new TextArea();
			add(tf, BorderLayout.NORTH);
			Panel p1 = new Panel();
			p1.setLayout(new BorderLayout());
			p1.add(ta, BorderLayout.CENTER);
			java.awt.List list = new java.awt.List();
			p1.add(list, BorderLayout.WEST);
			add(p1, BorderLayout.CENTER);
			connect = new Button("Connect");
			connect.addActionListener(this);
			disconnect = new Button("Disconnect");
			disconnect.setEnabled(false);
			disconnect.addActionListener(this);
			Panel p2 = new Panel();
			p2.add(connect);
			p2.add(disconnect);
			add(p2, BorderLayout.SOUTH);
		
			
			
		
		
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == connect){
			if(!connected){
				try{
					s = new Socket("127.0.0.1", 3000);
					oos = new ObjectOutputStream(s.getOutputStream());
					ois = new ObjectInputStream(s.getInputStream());
					thread = new Thread(this);
					thread.start();
					connected = true;
					connect.setEnabled(false);
					System.out.println("Connected!!!");
				}catch(UnknownHostException uhe){
					System.out.println(uhe.getMessage());
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
			
		}else if(ae.getSource() == disconnect){
			if(connected){
				

			}
		}else if(ae.getSource() == tf){
			
			if(connected){
				try{
					String temp = tf.getText();
					d1 = new DataObject();
					d1.setMessage(temp);
					oos.writeObject(d1);
					tf.setText("");
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			// added else statment 				
			
				
			}
			else {
					String usernames = tf.getText();
					newchat.setTitle(usernames);
				}
		}
	}
	public void run(){
		while(connected){
			try{
				d2 = (DataObject)ois.readObject();
				String temp = d2.getMessage();
				ta.append(temp + "\n");	

			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}catch(ClassNotFoundException cnfe){
				System.out.println(cnfe.getMessage());
			}
		}
	}
}
