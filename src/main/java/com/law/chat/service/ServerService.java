/*
 * Created on Apr 3, 2012
 */
package com.law.chat.service;

import java.io.IOException;

/**
 * Interface for chat server services.
 * 
 * @author Lloyd
 */
public interface ServerService {
	
	/**
	 * Start a chat server on the current host, using the given port.
	 * 
	 * @param port - Port that the server will listen on.
	 * 
	 * @throws IOException if the server can not be started.
	 */
	void startServer(int port) throws IOException;
	
	/**
	 * If the chat server has been started, shut it down. Nothing happens if
	 * it has not been started.
	 */
	void shutdownServer();
	
}
