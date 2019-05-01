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
	boolean connected;
	ChatFrame cf;
	java.awt.List list;

	public ChatPanel(ChatFrame chatter){
			this.cf = chatter;
			setLayout(new BorderLayout());
			tf = new TextField();
			tf.addActionListener(this);
			ta = new TextArea();
			add(tf, BorderLayout.NORTH);
			Panel p1 = new Panel();
			p1.setLayout(new BorderLayout());
			p1.add(ta, BorderLayout.CENTER);
			list = new java.awt.List();
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
					disconnect.setEnabled(true);
					System.out.println("Connected!!!");
					d1 = new DataObject();
					d1.type = "connect";
					d1.user = username;
					oos.writeObject(d1);
				}catch(UnknownHostException uhe){
					System.out.println(uhe.getMessage());
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
		}else if(ae.getSource() == disconnect){
			if(connected){
				try{
				d1 = new DataObject();
				d1.user = username;
				d1.type = "disconnect";
				oos.writeObject(d1);
				connected = false;
				connect.setEnabled(true);
				disconnect.setEnabled(false);
				s.close();
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}


			}
		}else if(ae.getSource() == tf){
			if(connected){
				try{
					String temp = tf.getText();
					d1 = new DataObject();
					d1.setMessage(username + ": " + temp);
					d1.type = "message";
					oos.writeObject(d1);
					tf.setText("");
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}else{
				username = tf.getText();
				cf.setTitle(username);
			}
		}
	}


	public void run(){
		while(connected){
			try{
				d2 = (DataObject)ois.readObject();
				System.out.println(d2.type);
				System.out.println(d2.getMessage());

				if(d2.type.equals("message")){
					System.out.println("inside message!");
					String temp = d2.getMessage();
					ta.append(temp + "\n");
				}
				else if(d2.type.equals("connect")){
					ta.append(d2.user + " has connected!\n");
					list.add(d2.user);
				}
				else if(d2.type.equals("disconnect")){
					ta.append(d2.user + " has disconnected!\n");
					list.remove(d2.user);
				}
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}catch(ClassNotFoundException cnfe){
				System.out.println(cnfe.getMessage());
			}
		}
	}
}
