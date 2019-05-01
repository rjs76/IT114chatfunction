import java.io.*;
import java.util.*;

public class DataObject implements Serializable{

	private String message;
	String type; // possible values: connect, disconnect, message, privateMessage, usernames
	String user;
	ArrayList<String>usernames;

	DataObject(){
		message = "";
		usernames = new ArrayList<String>();
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String inMessage){
		message = inMessage;
	}
}
