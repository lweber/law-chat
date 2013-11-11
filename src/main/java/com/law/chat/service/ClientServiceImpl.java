/*
 * Created on Apr 3, 2012
 */
package com.law.chat.service;

import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;
import com.law.chat.client.impl.ChatClientImpl;

/**
 * Implementation of chat client services.
 * 
 * @author Lloyd
 */
public class ClientServiceImpl implements ClientService {
	
	public ChatClient connectClient(String host, int port, String userName, MessageDisplayer messageDisplayer) {
		return new ChatClientImpl(host, port, userName, messageDisplayer);
	}
	
}
