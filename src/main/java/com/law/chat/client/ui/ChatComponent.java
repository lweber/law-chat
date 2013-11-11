/*
 * Created on Jan 20, 2007
 */
package com.law.chat.client.ui;

import static com.law.swingutil.ui.GridBagConstraintsBuilder.gbcBuilder;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.Style;

import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;

/**
 * Client-side chat component UI with scrolling chat history and text input
 * field for entering chat messages.
 * 
 * @author Lloyd
 */
@SuppressWarnings("serial")
public class ChatComponent
extends JComponent
implements MessageDisplayer, KeyListener {
	
	private Frame ownerFrame;
	private MessageScrollComponent mc;
	private JTextField editField;
	private ChatClient chatClient;
	private boolean hideEditField;
	private boolean inChat;
	
	/**
	 * Construct a chat component UI with scrolling chat history and text
	 * input line.
	 * 
	 * <p> If hideEditField is true then the edit field will initially be hidden
	 * and hitting the enter key will show it, and the edit field will be hidden
	 * after messages are sent. If hideEditField is false then the edit field
	 * will always be visible.
	 * 
	 * @param hideEditField - Whether to hide the edit field when not in use.
	 * @param ownerFrame - Will request focus if or when the edit field is
	 *  hidden.
	 */
	public ChatComponent(boolean hideEditField, Frame ownerFrame) {
		super();
		
		this.hideEditField = hideEditField;
		this.ownerFrame = ownerFrame;
		
		inChat = !hideEditField;
		
		setLayout(new GridBagLayout());
		
		add(mc = new MessageScrollComponent(this), gbcBuilder(0, 0).weightX(1).weightY(1).fillBoth().build());
		
		editField = new JTextField();
		editField.addKeyListener(this);
		editField.setVisible(inChat);
		
		add(editField, gbcBuilder(0, 1).fillHoriz().build());
		
		setVisible(true);
	}
	
	/**
	 * Set the chat client that will send our chat messages to the server.
	 * 
	 * @param chatClient - The chat client.
	 */
	public void setChatClient(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	public Style addStyle(String styleName, Style parentStyle) {
		return mc.addStyle(styleName, parentStyle);
	}
	
	public void setUserColor(String userName, Color c) {
		mc.setUserColor(userName, c);
	}
	
	@Override
	public void setBackground(Color c) {
		mc.setBackground(c);
		editField.setBorder(BorderFactory.createLineBorder(c));
	}
	
	public void appendMessage(String msg, String style) {
		mc.appendMessage(msg, style);
	}
	
	public void displayMessage(String userName, String msg) {
		mc.displayMessage(userName, msg);
	}
	
	public void displayServerMessage(String msg) {
		mc.displayServerMessage(msg);
	}
	
	public void clear() {
		mc.clear();
		editField.setText(null);
	}
	
	public void setOwnerFrame(Frame ownerFrame) {
		this.ownerFrame = ownerFrame;
	}
	
	public void keyTyped(KeyEvent e) {
		
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			
			if (inChat) {
				Document d = editField.getDocument();
				
				String chatMsg = "";
				try { chatMsg = d.getText(0, d.getLength()); }
				catch (Exception ex) {}
				
				if (chatMsg.length() > 0) {
					if (chatClient != null) {
						chatClient.sendChat(chatMsg);
					}
					else {
						displayMessage("NO CONNECTION", chatMsg);
					}
				}
				
				try { d.remove(0, d.getLength()); }
				catch (Exception ex) {}
			}
			
			if (hideEditField) {
				inChat = !inChat;
				editField.setVisible(inChat);
				validate();
			}
			
			if (inChat) {
				editField.requestFocus();
			}
			else if (ownerFrame != null) {
				ownerFrame.requestFocus();
			}
			
		}
	}
	
	public void keyPressed(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
}
