/*
 * Created on Mar 4, 2012
 */
package com.law.chat;

import java.util.ArrayList;
import java.util.List;

import com.law.chat.client.MessageDisplayer;
import com.law.network.DataLine;
import com.law.network.SocketReadProcessor;

/**
 * Client-side message processor to process data that comes in from the chat
 * server.
 * 
 * @author Lloyd
 */
public class MessageProcessor implements SocketReadProcessor {
	
	private List<MessageDisplayer> messageDisplayers = new ArrayList<MessageDisplayer>();
	
	/**
	 * Constructor.
	 */
	public MessageProcessor() {
	}
	
	public void addMessageDisplayer(MessageDisplayer messageDisplayer) {
		if (messageDisplayer != null) {
			messageDisplayers.add(messageDisplayer);
		}
	}
	
	public void removeMessageDisplayer(MessageDisplayer messageDisplayer) {
		messageDisplayers.remove(messageDisplayer);
	}
	
	/**
	 * Parse the data and request each message displayer to display the message.
	 * 
	 * @return The empty string.
	 */
	public String processDataFromSocket(String data, long fromId) {
		
		// Called client-side, when the server sends a message.
		DataLine chatMsg = new DataLine(data);
		
		switch (chatMsg.toType(ChatMessage.class)) {
		case CHAT:
		case TELL:
			String userName = chatMsg.getInfo(0);
			String msg;
			if (chatMsg.isType(ChatMessage.TELL)) {
				String toName = chatMsg.getInfo(1);
				msg = chatMsg.getInfo(2);
				msg = "(tell " + toName + ") " + msg;
			}
			else {
				msg = chatMsg.getInfo(1);
			}
			for (MessageDisplayer mp : messageDisplayers) {
				mp.displayMessage(userName, msg);
			}
			break;
		case SRVR_MSG:
			msg = chatMsg.getInfo(0);
			for (MessageDisplayer mp : messageDisplayers) {
				mp.displayServerMessage(msg);
			}
			break;
		default:
			break;
		}
		
		return "";
	}
	
}
