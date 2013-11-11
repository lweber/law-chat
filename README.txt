Chat Util

This project is a Java utility for starting a chat server and connecting
clients. Java sockets are used for network communication. A Swing UI package
is also included but does not have to be used.

Two service interfaces make it easy to launch the server and connect a user:

interface ServerService {
	void startServer(int port) throws IOException;
	void shutdownServer();
}

interface ClientService {
	ChatClient connectClient(String host, int port, String userName,
			MessageDisplayer messageDisplayer);
}

The ChatClient interface provides for interaction with the server:

interface ChatClient {
	void sendChat(String chatMessage);
	void sendTell(String toName, String chatMessage);
	void addMessageDisplayer(MessageDisplayer messageDisplayer);
	void removeMessageDisplayer(MessageDisplayer messageDisplayer);
	void logout();
}

This project requires the law-network and law-swing projects on GitHub.
