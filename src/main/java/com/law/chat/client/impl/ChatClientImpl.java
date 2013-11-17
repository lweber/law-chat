/*
 * Created on Apr 1, 2008
 */
package com.law.chat.client.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.law.chat.ChatMessage;
import com.law.chat.MessageProcessor;
import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;
import com.law.network.DataLine;
import com.law.network.SocketHandler;
import com.law.network.SocketHandler.Mode;

/**
 * Client-side chat handler.
 * 
 * @author Lloyd
 */
public class ChatClientImpl implements ChatClient {
	
	private String userName;
	private MessageProcessor messageProcessor;
	private SocketHandler socketWriter;
	private SocketHandler socketReader;
	
	/**
	 * Constructor - connects to the chat server.
	 * 
	 * <p> Handle a chat client by instantiating and starting two SocketHandler
	 * threads. The first thread uses MODE_WRITE to write the user's messages
	 * to the chat server. The second thread uses MODE_READ to receive other
	 * user's messages and send them to the socket read processor to display
	 * them or do whatever else may be needed.
	 * 
	 * @param hostName - Host/machine name for the chat server.
	 * @param portNum - Port number for the chat server.
	 * @param userName - Name of the user who is connecting.
	 * @param messageDisplayer - Displayer of messages from the chat server.
	 */
	public ChatClientImpl(String hostName, int portNum, String userName,
			MessageDisplayer messageDisplayer) {
		
		this.userName = userName;
		try {
			Socket clientSocket = new Socket(hostName, portNum);
			
			// The writer thread writes this user's message to the chat server.
			socketWriter = new SocketHandler(clientSocket, Mode.MODE_WRITE, null, null);
			// Protocol: Before doing anything else, send the user's name to the chat server.
			socketWriter.writeLine(userName);
			socketWriter.start();
			
			// The reader thread displays other user's messages.
			messageProcessor = new MessageProcessor();
			messageProcessor.addMessageDisplayer(messageDisplayer);
			socketReader = new SocketHandler(clientSocket, Mode.MODE_READ, messageProcessor, null);
			socketReader.start();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addMessageDisplayer(MessageDisplayer messageDisplayer) {
		messageProcessor.addMessageDisplayer(messageDisplayer);
	}
	
	public void removeMessageDisplayer(MessageDisplayer messageDisplayer) {
		messageProcessor.removeMessageDisplayer(messageDisplayer);
	}
	
	public void sendChat(String chatMessage) {
		new DataLine(ChatMessage.CHAT)
				.addInfo(userName)
				.addInfo(chatMessage)
				.sendTo(socketWriter);
	}
	
	public void sendTell(String toName, String chatMessage) {
		new DataLine(ChatMessage.TELL)
				.addInfo(userName)
				.addInfo(toName)
				.addInfo(chatMessage)
				.sendTo(socketWriter);
	}
	
	public void logout() {
		try {
			new DataLine(ChatMessage.QUIT)
					.addInfo(userName)
					.writeTo(socketWriter);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		socketReader.stopHandler();
		socketWriter.stopHandler();
	}
	
}
