/*
 * Created Mar 10, 2012
 */
package com.law.chat.client;

/**
 * Interface for client-side interaction with a chat server.
 * 
 * @author Lloyd
 */
public interface ChatClient {
	
	/**
	 * Send a chat message to the server.
	 * 
	 * @param chatMessage - The message.
	 */
	void sendChat(String chatMessage);
	
	/**
	 * Send a chat message intended for one recipient to the server.
	 * 
	 * @param toName - Name of the recipient client.
	 * @param chatMessage - The message.
	 */
	void sendTell(String toName, String chatMessage);
	
	/**
	 * 
	 * @param messageDisplayer
	 */
	void addMessageDisplayer(MessageDisplayer messageDisplayer);
	
	/**
	 * 
	 * @param messageDisplayer
	 */
	void removeMessageDisplayer(MessageDisplayer messageDisplayer);
	
	/**
	 * Logout and disconnect from the chat server.
	 */
	void logout();
	
}
