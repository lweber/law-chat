/*
 * Created on Feb 24, 2008
 */
package com.law.chat.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.law.chat.client.MessageDisplayer;

/**
 * MessageComponent is the scrolling text pane that shows all the chat
 * messages.
 * 
 * @author Lloyd
 */
@SuppressWarnings("serial")
public class MessageScrollComponent extends JComponent implements MessageDisplayer {
	
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	private StyledDocument doc;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	
	/**
	 * Constructor.
	 * 
	 * @param l - KeyListener - may be null if none desired. This just allows
	 *  the cursor to appear in the edit field when the return key is hit after
	 *  clicking on the editor pane.
	 */
	MessageScrollComponent(KeyListener l) {
		super();
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		
		textPane.addKeyListener(l);
		
		scrollPane = new JScrollPane(
				textPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		doc = textPane.getStyledDocument();
		
		// Styles
		Style serverStyle = doc.addStyle("serverStyle", null);
		StyleConstants.setBold(serverStyle, true);
		StyleConstants.setForeground(serverStyle, new Color(90, 30, 130));
		
		Style chatNameStyle = doc.addStyle("chatNameStyle", serverStyle);
		StyleConstants.setBold(chatNameStyle, true);
		StyleConstants.setForeground(chatNameStyle, Color.GREEN.darker());
		
		Style chatMsgStyle = doc.addStyle("chatMsgStyle", null);
		StyleConstants.setForeground(chatMsgStyle, Color.GREEN.darker());
		
		add(scrollPane);
	}
	
	Style addStyle(String styleName, Style parentStyle) {
		return doc.addStyle(styleName, parentStyle);
	}
	
	@Override
	public void setBackground(Color c) {
		textPane.setBackground(c);
	}
	
	public void displayMessage(String userName, String msg) {
		appendChat(userName, msg);
	}
	
	public void displayServerMessage(String msg) {
		appendMessage(msg, "serverStyle");
	}
	
	synchronized public void appendMessage(String msg, String style) {
		try {
			String insertStr = "> " + msg + NEW_LINE;
			doc.insertString(doc.getLength(), insertStr, doc.getStyle(style));
			scrollToBottom();
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	synchronized private void appendChat(String name, String msg) {
		try {
			String nameStr = "> [" + name + "] ";
			String msgStr = msg + NEW_LINE;
			doc.insertString(doc.getLength(), nameStr, doc.getStyle("chatNameStyle"));
			doc.insertString(doc.getLength(), msgStr, doc.getStyle("chatMsgStyle"));
			scrollToBottom();
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void scrollToBottom() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JScrollBar sb = scrollPane.getVerticalScrollBar();
				sb.setValue(sb.getMaximum());
			}
		});
	}
	
	public void clear() {
		try {
			doc.remove(0, doc.getLength());
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
}
