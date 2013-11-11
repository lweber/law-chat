/*
 * Created on Apr 3, 2012
 */
package com.law.chat.service;

import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;

/**
 * Interface for chat client services.
 * 
 * @author Lloyd
 */
public interface ClientService {
	
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
	
}
