/*
 * Created on Mar 4, 2012
 */
package com.law.chat.client;

/**
 * Interface for displaying messages from a chat server.
 * 
 * @author Lloyd
 */
public interface MessageDisplayer {
	
	/**
	 * Display a message from a given user.
	 * 
	 * @param userName - The user's name.
	 * @param msg - The message to display.
	 */
	void displayMessage(String userName, String msg);
	
	/**
	 * Display a message from the chat server.
	 * 
	 * @param msg - The message to display.
	 */
	void displayServerMessage(String msg);
	
}
