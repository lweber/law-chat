/*
 * Created on Apr 3, 2012
 */
package com.law.chat.service;

import java.io.IOException;

import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;

/**
 * Interface for chat client/server services.
 * 
 * @author Lloyd
 */
public interface ChatService {
	
	/**
	 * Start a chat server on the current host, using the given port.
	 * 
	 * @param port - Port that the server will listen on.
	 * 
	 * @throws IOException if the server can not be started.
	 */
	void startServer(int port) throws IOException;
	
	/**
	 * Connect to the chat server on the given host and port.
	 * 
	 * @param host - Host machine that the server is running on.
	 * @param port - Port that the server is listening on.
	 * @param userName - User name to connect as.
	 * @param messageDisplayer - Interface for displaying messages to the client.
	 * 
	 * @return Interface for client interaction with the chat server.
	 */
	ChatClient connectClient(String host, int port, String userName,
			MessageDisplayer messageDisplayer);
	
	/**
	 * If the chat server has been started, shut it down. Nothing happens if
	 * it has not been started.
	 */
	void shutdownServer();
	
}
