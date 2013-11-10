package com.law.chat.service;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.law.chat.client.ChatClient;
import com.law.chat.client.MessageDisplayer;

@RunWith(MockitoJUnitRunner.class)
public class ChatServiceImplTest {
	
	private static final int PORT_NUM = 6948;
	
	private static ChatService chatService;
	
	@BeforeClass
	public static void startServer() throws IOException {
		chatService = new ChatServiceImpl();
		chatService.startServer(PORT_NUM);
	}
	
	@AfterClass
	public static void stopServer() {
		chatService.shutdownServer();
	}
	
	@Test
	public void sendChat() {
		MessageDisplayer messageDisplayer1 = mock(MessageDisplayer.class);
		MessageDisplayer messageDisplayer2 = mock(MessageDisplayer.class);
		MessageDisplayer messageDisplayer3 = mock(MessageDisplayer.class);
		ChatClient client1 = chatService.connectClient(null, PORT_NUM, "user_1", messageDisplayer1);
		ChatClient client2 = chatService.connectClient(null, PORT_NUM, "user_2", messageDisplayer2);
		ChatClient client3 = chatService.connectClient(null, PORT_NUM, "user_3", messageDisplayer3);
		
		client1.sendChat("hello world");
		
		// TODO How to avoid this delay to wait for the socket to send the message?
		try { Thread.sleep(100); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
		verify(messageDisplayer1).displayMessage("user_1", "hello world");
		verify(messageDisplayer2).displayMessage("user_1", "hello world");
		verify(messageDisplayer3).displayMessage("user_1", "hello world");
		client1.logout();
		client2.logout();
		client3.logout();
	}
	
	@Test
	public void sendTell() {
		MessageDisplayer messageDisplayer1 = mock(MessageDisplayer.class);
		MessageDisplayer messageDisplayer2 = mock(MessageDisplayer.class);
		MessageDisplayer messageDisplayer3 = mock(MessageDisplayer.class);
		ChatClient client1 = chatService.connectClient(null, PORT_NUM, "user_1", messageDisplayer1);
		ChatClient client2 = chatService.connectClient(null, PORT_NUM, "user_2", messageDisplayer2);
		ChatClient client3 = chatService.connectClient(null, PORT_NUM, "user_3", messageDisplayer3);
		
		client1.sendTell("user_2", "hello user 2");
		
		// TODO How to avoid this delay to wait for the socket to send the message?
		try { Thread.sleep(100); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
		verify(messageDisplayer1, never()).displayMessage(anyString(), anyString());
		verify(messageDisplayer2).displayMessage("user_1", "(tell user_2) hello user 2");
		verify(messageDisplayer3, never()).displayMessage(anyString(), anyString());
		client1.logout();
		client2.logout();
		client3.logout();
	}
	
	@Test
	public void connectSameName() {
		ChatClient client1 = chatService.connectClient(null, PORT_NUM, "user", null);
		ChatClient client2 = chatService.connectClient(null, PORT_NUM, "user", null);
		client1.logout();
		client2.logout();
	}
	
}
