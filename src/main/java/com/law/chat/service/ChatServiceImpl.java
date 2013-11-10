/*
 * Created on Apr 3, 2012
 */
package com.law.chat.service;

import java.io.IOException;

import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;
import com.law.chat.client.impl.ChatClientImpl;
import com.law.chat.server.ChatServer;

/**
 * Implementation of chat client/server services.
 * 
 * @author Lloyd
 */
public class ChatServiceImpl implements ChatService {
	
	public void startServer(int port) throws IOException {
		ChatServer.startServer(port);
	}
	
	public ChatClient connectClient(String host, int port, String userName, MessageDisplayer messageDisplayer) {
		return new ChatClientImpl(host, port, userName, messageDisplayer);
	}
	
	public void shutdownServer() {
		ChatServer.shutdownServer();
	}
	
}
