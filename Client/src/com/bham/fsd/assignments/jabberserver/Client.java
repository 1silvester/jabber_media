package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Platform;

public class Client implements Runnable {
	
	private Socket clientSocket;
	private String host = "localhost";
	private static final int PORT_NUMBER = 44444;
	private ObjectOutputStream oos;
  	private ObjectInputStream ois;
  	private JabberMessage request;
  	private JabberMessage response;
  	
  	public Client() {
  		try {
			clientSocket = new Socket(host, PORT_NUMBER);
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
  	}

	public void run() {
		while (true) {
			try {
				//System.out.println("READING.....");
				response = (JabberMessage)ois.readObject();
				//System.out.println(response.getMessage());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected() {
		try {
			return clientSocket.isConnected();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getResponse() {
		return response.getMessage();
	}
	
	public ArrayList<ArrayList<String>> getData() {
		return response.getData();
	}
	
	public void sendMessage (String message) {
		try {
			request = new JabberMessage(message);
			oos.writeObject(request);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
