/*
 * Created on Apr 1, 2008
 */
package com.law.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.law.chat.ChatMessage;
import com.law.network.DataLine;
import com.law.network.SocketHandler;
import com.law.network.SocketHandler.Mode;
import com.law.network.SocketReadProcessor;
import com.law.network.SocketShutdownListener;

/**
 * Server-side chat handler.
 * 
 * <p> Handle a chat server by instantiating a server socket and running a
 * separate thread to wait for client sockets. For each client socket accepted
 * a SocketHandler thread is started using MODE_READ to read messages from the
 * client. The message is then sent to all of the other clients (or just one
 * other client if the message is a tell).
 * 
 * <p> The chat server uses one thread plus one additional thread for each chat
 * client that joins.
 * 
 * @author lweber
 */
public class ChatServer
extends Thread
implements SocketReadProcessor, SocketShutdownListener {
	
	static private ChatServer serverSingleton;
	
	private ServerSocket serverSocket;
	
	private Map<String, SocketHandler> clientSocketHandlers =
		new HashMap<String, SocketHandler>();
	
	/**
	 * Private constructor - call startServer() to get the singleton chat
	 * server object.
	 * 
	 * @throws IOException if an I/O error occurs when opening the socket.
	 */
	private ChatServer(int portNum) throws IOException {
		try {
			serverSocket = new ServerSocket(portNum);
		}
		catch (IOException e) {
			// TODO logging
//			logErr(getClass().getName() + ": Could not listen on port "
//					+ portNum + ": " + e);
			throw e;
		}
	}
	
	/**
	 * Create the singleton ChatServer object and begin waiting for clients.
	 * 
	 * <p> If the server singleton has already been started it won't be started
	 * again.
	 * 
	 * @param portNum - The port number that the server will listen on.
	 * 
	 * @throws IOException if the server can not be started.
	 */
	synchronized static public ChatServer startServer(int portNum)
	throws IOException {
		if (serverSingleton != null) {
//			logAndThrowException(
//					"ChatServer.startServer(): cannot start the server, it is already started.");
		}
		else {
			serverSingleton = new ChatServer(portNum);
			serverSingleton.start();
		}
		
		return serverSingleton;
	}
	
	/**
	 * Send a message to the clients, close the server socket, stop all the
	 * client socket handlers, and null the singleton.
	 */
	synchronized static public void shutdownServer() {
		if (serverSingleton != null) {
			DataLine chatMsg = new DataLine(ChatMessage.SRVR_MSG);
			chatMsg.addInfo("The chat server is shutting down.");
			serverSingleton.writeToAllSockets(chatMsg.toString());
			
			for (SocketHandler sh : serverSingleton.clientSocketHandlers.values()) {
				sh.stopHandler();
			}
			
			try {
				// This will cause accept() to throw an exception and run() to exit.
				serverSingleton.serverSocket.close();
				System.out.println("Chat Server " + serverSingleton.getName() + " shutdown.");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			serverSingleton = null;
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				addChatClient(clientSocket);
			}
			catch (IOException e) {
//				logErr(getClass().getName() + ": serverSocket.accept() failed: " + e);
			}
		}
	}
	
	/**
	 * Add a socket to the list of chat client sockets being handled by
	 * this server.
	 * 
	 * @param clientSocket - 
	 */
	synchronized private void addChatClient(Socket clientSocket) {
		try {
			SocketHandler clientHandler = new SocketHandler(clientSocket, Mode.MODE_READ, this, this);
			
			// Protocol: Before doing anything else, read the user's name from the chat client.
			String clientName = clientHandler.readLine();
			
			DataLine chatMsg = new DataLine(ChatMessage.SRVR_MSG);
			chatMsg.addInfo("'" + clientName + "' has joined the chat server.");
			writeToAllSockets(chatMsg.toString());
			
			clientSocketHandlers.put(clientName, clientHandler);
			clientHandler.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	synchronized private void writeToAllSockets(String data) {
		for (SocketHandler sh : clientSocketHandlers.values()) {
			try { sh.writeLine(data); }
			catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	synchronized private void writeToOneSocket(String data, String toName) {
		SocketHandler sh = clientSocketHandlers.get(toName);
		if (sh != null) {
			try { sh.writeLine(data); }
			catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * Process messages sent to the server by a client.
	 * 
	 * <p> The socket handler uses MODE_READ so the return value does not matter.
	 * 
	 * @return null.
	 */
	synchronized public String processDataFromSocket(String data, long fromId) {
		DataLine chatMsg = new DataLine(data);
		switch (chatMsg.toType(ChatMessage.class)) {
		case CHAT:
			writeToAllSockets(data);
			break;
			
		case TELL:
			String toName = chatMsg.getInfo(1);
			writeToOneSocket(data, toName);
			break;
			
		case QUIT:
			String quitName = chatMsg.getInfo(0);
			SocketHandler sh = clientSocketHandlers.remove(quitName);
			if (sh != null) {
				sh.stopHandler();
			}
			// TODO This message may be redundant.
			DataLine serverMsg = new DataLine(ChatMessage.SRVR_MSG);
			serverMsg.addInfo("'" + quitName + "' has left the chat server.");
			writeToAllSockets(serverMsg.toString());
			break;
			
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * Called when a client socket handler closes, for example, if a client
	 * closes their window.
	 */
	synchronized public void socketClosing(SocketHandler socketHandler) {
		String clientName = null;
		for (Entry<String, SocketHandler> ent : clientSocketHandlers.entrySet()) {
			if (ent.getValue() == socketHandler) {
				clientName = ent.getKey();
				clientSocketHandlers.remove(clientName);
				
				DataLine chatMsg = new DataLine(ChatMessage.SRVR_MSG);
				chatMsg.addInfo("'" + clientName + "' has left the chat server.");
				writeToAllSockets(chatMsg.toString());
				break;
			}
		}
	}
	
}
