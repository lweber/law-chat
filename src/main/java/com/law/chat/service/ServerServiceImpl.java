/*
 * Created on Apr 3, 2012
 */
package com.law.chat.service;

import java.io.IOException;

import com.law.chat.server.ChatServer;

/**
 * Implementation of chat server services.
 * 
 * @author Lloyd
 */
public class ServerServiceImpl implements ServerService {
	
	public void startServer(int port) throws IOException {
		ChatServer.startServer(port);
	}
	
	public void shutdownServer() {
		ChatServer.shutdownServer();
	}
	
}
